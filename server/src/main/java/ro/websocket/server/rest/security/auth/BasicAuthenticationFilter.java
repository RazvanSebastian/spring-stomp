package ro.websocket.server.rest.security.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import ro.websocket.server.rest.security.jwt.JwtService;

import java.io.IOException;
import java.util.Map;

public class BasicAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper mapper = new ObjectMapper();
    private final JwtService jwtService;

    public BasicAuthenticationFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager, JwtService jwtService) {
        super(defaultFilterProcessesUrl, authenticationManager);
        this.jwtService = jwtService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        final Map<String, String> body = mapper.readValue(request.getInputStream(),
                new TypeReference<>() {
                });
        if (body.containsKey("username") && body.containsKey("password")) {
            return this.getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(body.get("username"), body.get("password")));
        }
        throw new BadCredentialsException("Invalid credentials");
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        final String token = this.jwtService.generateToken(authResult.getName());
        Cookie cookie = new Cookie("access_token", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, failed.getMessage());
    }
}
