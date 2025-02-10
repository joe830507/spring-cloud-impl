package com.example.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.example.filter.JwtAuthenticationGatewayFilterFactory;

@Configuration
public class GatewayRoutesConfig {

        private final RouteConfig routeConfig;
        private final JwtAuthenticationGatewayFilterFactory jwtAuthFilterFactory;

        public GatewayRoutesConfig(RouteConfig routeConfig,
                        JwtAuthenticationGatewayFilterFactory jwtAuthFilterFactory) {
                this.routeConfig = routeConfig;
                this.jwtAuthFilterFactory = jwtAuthFilterFactory;
        }

        @Bean
        public RouteLocator routeLocator(RouteLocatorBuilder builder) {
                RouteLocatorBuilder.Builder routesBuilder = builder.routes();

                routeConfig.getRoutes().forEach(route -> {
                        routesBuilder.route(route.getId(), r -> r
                                        .path(route.getPredicates().get(0).substring(5))
                                        .filters(f -> f.filter(jwtAuthFilterFactory.apply(
                                                        new JwtAuthenticationGatewayFilterFactory.Config())))
                                        .uri(route.getUri()));
                });

                return routesBuilder.build();
        }
}
