package com.hospitalmanagement.patientmanagement.service.impl;

import com.hospitalmanagement.patientmanagement.converter.impl.PatientVisitConverter;
import com.hospitalmanagement.patientmanagement.dao.PatientVisitRepository;
import com.hospitalmanagement.patientmanagement.dto.PatientVisitDto;
import com.hospitalmanagement.patientmanagement.model.PatientVisit;
import com.hospitalmanagement.patientmanagement.service.PatientVistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientVisitServiceImpl implements PatientVistService {

    private final PatientVisitRepository patientVisitRepository;
    private final PatientVisitConverter converter;
    @Autowired
    protected PatientVisitServiceImpl(final PatientVisitRepository repository, final PatientVisitConverter converter) {
        this.patientVisitRepository = repository;
        this.converter = converter;
    }

    public PatientVisitDto create(PatientVisitDto patientDto) {
        PatientVisit patientVisit = converter.fromDto(patientDto);
        PatientVisit saved = patientVisitRepository.save(patientVisit);
        return converter.toDto(saved);
    }

}
