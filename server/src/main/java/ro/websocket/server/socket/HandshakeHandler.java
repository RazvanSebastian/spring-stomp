package ro.websocket.server.socket;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.WebUtils;
import ro.websocket.server.rest.security.jwt.JwtService;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class HandshakeHandler extends DefaultHandshakeHandler {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        final ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;
        final String accessToken = Optional.ofNullable(WebUtils.getCookie(servletServerRequest.getServletRequest(), "access_token"))
                .map(Cookie::getValue)
                .orElseThrow(() -> new BadCredentialsException("Access token not found"));
        return getAuthentication(accessToken);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String accessToken) {
        DecodedJWT jwt = jwtService.decodeToken(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwt.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), null, userDetails.getAuthorities());
    }

}
