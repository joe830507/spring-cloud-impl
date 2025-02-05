package com.example.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.security.JwtAuthenticationGatewayFilterFactory;

@Configuration
public class GatewayRoutesConfig {

    private final JwtAuthenticationGatewayFilterFactory jwtAuthFilterFactory;

    public GatewayRoutesConfig(JwtAuthenticationGatewayFilterFactory jwtAuthFilterFactory) {
        this.jwtAuthFilterFactory = jwtAuthFilterFactory;
    }

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes().route("order_route", r -> r.path("/orders/**")
                // Apply our custom JWT Gateway filter before routing
                .filters(f -> f.filter(jwtAuthFilterFactory
                        .apply(new JwtAuthenticationGatewayFilterFactory.Config())))
                .uri("lb://order-service")).build();
    }
}
