package com.hospitalmanagement.patientmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.hospitalmanagement.patientmanagement")
public class ApplicationService {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationService.class, args);
    }

}