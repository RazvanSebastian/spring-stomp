package ro.websocket.server.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import ro.websocket.server.rest.security.jwt.JwtService;

@Configuration
public class WebsocketConfig {

    @Bean
    public HandshakeHandler handshakeHandler(JwtService jwtService, UserDetailsService userDetailsService) {
        return new HandshakeHandler(jwtService, userDetailsService);
    }
}
