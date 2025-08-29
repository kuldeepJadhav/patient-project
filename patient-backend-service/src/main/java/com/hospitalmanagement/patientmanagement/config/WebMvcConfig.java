package com.hospitalmanagement.patientmanagement.config;

import com.hospitalmanagement.patientmanagement.interceptor.UserPopulatorInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserPopulatorInterceptor userPopulatorInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(userPopulatorInterceptor).order(Ordered.LOWEST_PRECEDENCE).addPathPatterns("/api/v1/**").excludePathPatterns("/actuator/**");
    }

}
