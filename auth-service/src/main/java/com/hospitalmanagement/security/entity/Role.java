package com.hospitalmanagement.security.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PATIENT_MANAGEMENT_ROLE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 20, unique = true)
    private String name;
    
    @Column(name = "description", nullable = false, length = 255)
    private String description;
} 