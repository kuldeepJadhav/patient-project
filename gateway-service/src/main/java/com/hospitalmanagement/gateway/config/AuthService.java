package com.hospitalmanagement.gateway.config;

import com.hospitalmanagement.gateway.dto.UserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AuthService {

    private final WebClient webClient;

    @Value("${auth-service.url:http://localhost:9094}")
    private String authServiceUrl;

    private String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    public AuthService(WebClient authServiceWebClient) {
        this.webClient = authServiceWebClient;
    }

    public Mono<UserBean> validateToken(String token) {
        System.out.println("=== AuthService.validateToken called ===");
        System.out.println("Token length: " + token.length());
        System.out.println("Calling auth service at: " + authServiceUrl + "/auth/verify");
        
        return webClient.get()
                .uri(uriBuilder  -> uriBuilder
                        .path("/auth/verify")
                        .build())
                .header(AUTHORIZATION_HEADER, "Bearer " + token)
                .retrieve()
                .bodyToMono(UserBean.class);
    }
}
