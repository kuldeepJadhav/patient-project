package com.hospitalmanagement.gateway.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalmanagement.gateway.dto.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Objects;

@Configuration
public class RouteConfiguration {

    @Value("${auth-service.url:http://localhost:9094}")
    private String authServiceUrl;

    @Value("${patient-backend-service.url:http://localhost:9092}")
    private String patientBackendUrl;
    
    @Autowired
    private AuthService authService;

    private static String AUTHORIZATION = "Authorization";
    private static String BEARER = "Bearer";

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        System.out.println("=== Configuring Gateway Routes ===");
        System.out.println("Auth service URL: " + authServiceUrl);
        System.out.println("Patient backend URL: " + patientBackendUrl);
        
        return builder.routes()
                .route("test_route", r -> r.path("/gateway/test/**")
                        .filters(f -> f.rewritePath("/gateway/test/(?<segment>.*)", "/${segment}"))
                        .uri("http://httpbin.org"))
                .route("auth_route", r -> r.path("/gateway/auth/**")
                        .filters(f -> f.rewritePath("/gateway/(?<segment>.*)", "/${segment}"))
                        .uri(authServiceUrl))
                .route("patient_backend_route", r -> r.path("/gateway/api/**")
                        .filters(f -> {
                            System.out.println("Applying filters to patient backend route");
                            return f.filter((exchange, chain) -> validateToken(exchange, chain))
                                    .rewritePath("/gateway/(?<segment>.*)", "/${segment}");
                        })
                        .uri(patientBackendUrl))
                .build();
    }

    private Mono<Void> validateToken(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authorization = request.getHeaders().getFirst(AUTHORIZATION);
        
        System.out.println("=== Token Validation Debug ===");
        System.out.println("Request path: " + request.getPath());
        System.out.println("Authorization header: " + authorization);
        
        if (Objects.isNull(authorization) || !authorization.startsWith(BEARER + " ")) {
            System.out.println("Authorization header is null or doesn't start with 'Bearer '");
            return authorizationErrorResponse(exchange);
        }
        
        String token = authorization.substring((BEARER + " ").length());
        System.out.println("Extracted token: " + token.substring(0, Math.min(token.length(), 20)) + "...");
        
        Mono<UserBean> userBeanMono = authService.validateToken(token);
        return userBeanMono.flatMap(user -> {
            System.out.println("Token validation successful for user: " + user.getUsername());
            if (Objects.isNull(user)) {
                System.out.println("UserBean is null after validation");
                return authorizationErrorResponse(exchange);
            }
            return chain.filter(exchange);
        }).onErrorResume(e -> {
            System.err.println("Error during token validation: " + e.getMessage());
            return authorizationErrorResponse(exchange);
        });

    }

    private Mono<Void> authorizationErrorResponse(ServerWebExchange exchange) {
        ByteBuffer byteBuffer;
        try {
            byteBuffer = ByteBuffer.wrap((new ObjectMapper()).writeValueAsBytes(Map.of("msg", "Authentication Info is missing")));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        DataBuffer dataBuffer = new DefaultDataBufferFactory().wrap(byteBuffer);
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatusCode.valueOf(401));
        response.getHeaders().set("Content-Type", "application/json");
        return exchange.getResponse().writeWith(Flux.just(dataBuffer));
    }
}
