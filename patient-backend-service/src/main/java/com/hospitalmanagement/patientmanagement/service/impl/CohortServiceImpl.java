package com.hospitalmanagement.patientmanagement.service.impl;

import com.hospitalmanagement.patientmanagement.converter.impl.CohortConverter;
import com.hospitalmanagement.patientmanagement.dao.CohortRepository;
import com.hospitalmanagement.patientmanagement.dto.CohortDto;
import com.hospitalmanagement.patientmanagement.model.Cohort;
import com.hospitalmanagement.patientmanagement.service.CohortService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CohortServiceImpl implements CohortService {

    private final CohortRepository cohortRepository;
    private final CohortConverter cohortConverter;

    @Override
    public CohortDto create(CohortDto cohortDto) {
        // Convert DTO to entity
        Cohort cohort = cohortConverter.fromDto(cohortDto);
        
        // Save the entity
        Cohort savedCohort = cohortRepository.save(cohort);
        
        // Convert back to DTO and return
        return cohortConverter.toDto(savedCohort);
    }

    public Page<CohortDto> getCohorts(Pageable pageable) {
        return cohortRepository.findAll(pageable).map(cohortConverter::toDto);
    }
}
