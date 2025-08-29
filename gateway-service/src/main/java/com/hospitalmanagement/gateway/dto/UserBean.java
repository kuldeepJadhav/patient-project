package com.hospitalmanagement.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;


@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBean {
    private String username;
}

