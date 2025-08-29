package com.hospitalmanagement.patientmanagement.interceptor;

import com.hospitalmanagement.patientmanagement.dto.UserBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserPopulatorInterceptor implements HandlerInterceptor {
    @Autowired
    private UserBean userBean;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new InsufficientAuthenticationException("User not authenticated");
        };
        JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken)authentication;
        userBean.setId(jwtAuth.getToken().getClaim("id"));
        userBean.setRoles(new HashSet<>(jwtAuth.getToken().getClaim("roles")));
        userBean.setUsername(authentication.getName());
        return true;
    }
}
