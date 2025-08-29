package com.hospitalmanagement.patientmanagement.rest.controller;

import com.hospitalmanagement.patientmanagement.dto.CohortDto;
import com.hospitalmanagement.patientmanagement.service.CohortService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cohorts")
@RequiredArgsConstructor
public class CohortController {
    
    private final CohortService cohortService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_DOCTOR')")
    public ResponseEntity<CohortDto> createCohort(@Valid @RequestBody CohortDto cohortDto) {
        CohortDto finalDto = cohortService.create(cohortDto);
        return new ResponseEntity<>(finalDto, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_DOCTOR')")
    public ResponseEntity<Page<CohortDto>> getAllCohorts(@PageableDefault(size = 20) Pageable pageable) {
        return new ResponseEntity<>(cohortService.getCohorts(pageable), HttpStatus.OK);
    }


} 