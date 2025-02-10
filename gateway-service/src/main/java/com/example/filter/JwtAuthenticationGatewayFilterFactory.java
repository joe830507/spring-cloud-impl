package com.example.filter;

import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import com.example.utilities.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationGatewayFilterFactory
        extends AbstractGatewayFilterFactory<JwtAuthenticationGatewayFilterFactory.Config> {

    private static final Logger log =
            LoggerFactory.getLogger(JwtAuthenticationGatewayFilterFactory.class);

    private final JwtUtil jwtUtil;

    public JwtAuthenticationGatewayFilterFactory(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader =
                    exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            log.debug("Auth header: {}", authHeader);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Mono.fromRunnable(
                        () -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
            }

            String token = authHeader.substring(7);
            Claims claims = jwtUtil.validateToken(token);
            claims.forEach((k, v) -> log.debug("Claim: {} {}", k, v));
            if (claims.isEmpty()) {
                return Mono.fromRunnable(
                        () -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
            }

            String username = jwtUtil.extractUsername(token);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null,
                            Collections.emptyList());

            return ReactiveSecurityContextHolder.getContext().map(context -> {
                context.setAuthentication(authentication);
                return context;
            }).then(chain.filter(exchange));
        };
    }

    @PostConstruct
    public void init() {
        log.info("JWT Authentication filter initialized");
    }

    public static class Config {
    }
}
