package ro.websocket.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * If you use SockJS as a fallback over normal HTTP, you should configure a normal HTTP route as well as the websocket Route.
     * https://docs.spring.io/spring-cloud-gateway/reference/spring-cloud-gateway/global-filters.html#websocket-routing-filter
     */
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(predicateSpec -> predicateSpec
                        .path("/api/**")
                        .uri("http://localhost:8080")
                ).build();
    }
}
