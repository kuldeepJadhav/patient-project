package com.hospitalmanagement.patientmanagement.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Set;

@RequestScope
@Getter
@Setter
@Component
public class UserBean {
    private Long id;
    private String username;
    private Set<String> roles;
}
