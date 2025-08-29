package com.hospitalmanagement.patientmanagement.service;

import com.hospitalmanagement.patientmanagement.dto.CohortDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CohortService {
    CohortDto create(CohortDto cohortDto);
    Page<CohortDto> getCohorts(Pageable pageable);
}
