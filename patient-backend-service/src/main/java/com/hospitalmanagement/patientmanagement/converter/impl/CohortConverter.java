package com.hospitalmanagement.patientmanagement.converter.impl;

import com.hospitalmanagement.patientmanagement.converter.Converter;
import com.hospitalmanagement.patientmanagement.dto.CohortDto;
import com.hospitalmanagement.patientmanagement.model.Cohort;
import org.springframework.stereotype.Component;

@Component
public class CohortConverter implements Converter<Cohort, CohortDto> {

    @Override
    public CohortDto toDto(Cohort cohort) {
        if (cohort == null) {
            return null;
        }
        return CohortDto.builder()
                .id(cohort.getId())
                .cohortName(cohort.getCohortName())
                .cohortDescription(cohort.getCohortDescription())
                .cohortCriteria(cohort.getCohortCriteria())
                .createdOn(cohort.getCreatedOn())
                .updatedOn(cohort.getUpdatedOn())
                .build();
    }

    @Override
    public Cohort fromDto(CohortDto dto) {
        if (dto == null) {
            return null;
        }
        return Cohort.builder()
                .id(dto.getId())
                .cohortName(dto.getCohortName())
                .cohortDescription(dto.getCohortDescription())
                .cohortCriteria(dto.getCohortCriteria())
                .build();
    }
} 