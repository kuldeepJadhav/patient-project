package com.hospitalmanagement.patientmanagement.rest.controller;

import com.hospitalmanagement.patientmanagement.dto.PatientDto;
import com.hospitalmanagement.patientmanagement.dto.PatientVisitDto;
import com.hospitalmanagement.patientmanagement.service.PatientService;
import com.hospitalmanagement.patientmanagement.service.PatientVistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patientvisits")
@RequiredArgsConstructor
public class PatientVisitController {
    final PatientVistService patientVisitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_DOCTOR')")
    public PatientVisitDto createPatient(@Valid @RequestBody PatientVisitDto patientVisitDto) {
        return patientVisitService.create(patientVisitDto);
    }
}
