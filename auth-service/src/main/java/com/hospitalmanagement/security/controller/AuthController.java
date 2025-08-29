package com.hospitalmanagement.security.controller;

import com.hospitalmanagement.security.dto.AuthRequest;
import com.hospitalmanagement.security.dto.AuthResponse;
import com.hospitalmanagement.security.dto.UserBean;
import com.hospitalmanagement.security.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    private static String BEARER = "Bearer";

    @PostMapping
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest authRequest) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(), 
                    authRequest.getPassword()
                )
            );

            if (authentication.isAuthenticated()) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String token = jwtService.generateToken(userDetails.getUsername());
                
                return ResponseEntity.ok(new AuthResponse(token, "Authentication successful"));
            } else {
                return ResponseEntity.status(401).body(new AuthResponse(null, "Authentication failed"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body(new AuthResponse(null, "Invalid credentials"));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<UserBean> verifyToken(@RequestHeader("Authorization") String tokenHeader) {
        try {
            if (!tokenHeader.startsWith(BEARER)) {
                throw new InsufficientAuthenticationException("Token Verification failed");
            }
            String token = tokenHeader.substring(BEARER.length());
            String username = jwtService.extractUsername(token);
            Boolean isValid = jwtService.validateToken(token, username);
            if (!isValid) {
                throw new InsufficientAuthenticationException("Token not valid");
            }
            UserBean userBean = UserBean.builder().username(username).build();
            return ResponseEntity.ok(userBean);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }
} 