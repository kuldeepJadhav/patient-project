package com.hospitalmanagement.patientmanagement.service;

import com.hospitalmanagement.patientmanagement.dto.PatientDto;
import com.hospitalmanagement.patientmanagement.model.CohortFilter;
import org.springframework.data.domain.Page;

public interface PatientService{
    Page<PatientDto> getPatientsForCohort(CohortFilter filter);
    Long getCountForFilter(CohortFilter filter);
    PatientDto getById(Long id);
    PatientDto create(PatientDto patientDto);
    PatientDto update(PatientDto dto, Long id);

    Page<PatientDto> getPatientsForCohortId(Long id);

}
