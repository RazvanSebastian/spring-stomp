package ro.websocket.server.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebsocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
                .simpDestMatchers("/user/**").hasRole("USER")
                .simpDestMatchers("/app/**").authenticated()
                .simpDestMatchers("/all/**").authenticated()
                .simpSubscribeDestMatchers("/user/**").hasRole("USER")
                .simpSubscribeDestMatchers("/all/**").authenticated();

    }

}
