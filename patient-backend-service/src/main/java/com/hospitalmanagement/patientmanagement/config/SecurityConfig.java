package com.hospitalmanagement.patientmanagement.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${security.jwt.secret:iXew3rva2D5YlryDewkVWpmCZKBf2sgJgfzHIr2bGtk}")
    private String secret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))

                );
        return http.build();
    }

    /*
     .oauth2ResourceServer(oauth2 -> oauth2
              .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))
          );
     */

    @Bean
    public JwtDecoder jwtDecoder(){
        byte[] keyBytes = Base64.getUrlDecoder().decode(secret);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes); // or HmacSHA512
        // Match the algorithm you used to sign (HS256/384/512)
        return NimbusJwtDecoder.withSecretKey(secretKey)
                .build();
    }

    Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthConverter() {
        return jwt -> {
            Set<String> roles = new HashSet<>((ArrayList)jwt.getClaims().get("roles"));
            Collection<GrantedAuthority> authorities = roles == null ? List.of() :
                    roles.stream()
                            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role) // add ROLE_ prefix
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toSet());
            return new JwtAuthenticationToken(jwt, authorities);
        };
    }

}