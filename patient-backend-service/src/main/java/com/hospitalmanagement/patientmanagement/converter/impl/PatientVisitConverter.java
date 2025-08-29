package com.hospitalmanagement.patientmanagement.converter.impl;

import com.hospitalmanagement.patientmanagement.converter.Converter;
import com.hospitalmanagement.patientmanagement.dto.PatientVisitDto;
import com.hospitalmanagement.patientmanagement.model.Patient;
import com.hospitalmanagement.patientmanagement.model.PatientVisit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientVisitConverter implements Converter<PatientVisit, PatientVisitDto> {

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public PatientVisitDto toDto(PatientVisit visit) {
        if (visit == null) {
            return null;
        }
        PatientVisitDto dto = new PatientVisitDto();
        dto.setId(visit.getId());
        dto.setVisitedOn(visit.getVisitedOn());
        dto.setDoctorId(visit.getDoctorId());
        dto.setDiagnosis(visit.getDiagnosis());
        dto.setTreatment(visit.getTreatment());
        dto.setVisitNotes(visit.getVisitNotes());
        dto.setCreatedOn(visit.getCreatedOn());
        dto.setUpdatedOn(visit.getUpdatedOn());
        dto.setPatientId(visit.getPatient().getId());
        return dto;
    }

    @Override
    public PatientVisit fromDto(PatientVisitDto dto) {
        if (dto == null) {
            return null;
        }
        Patient reference = entityManager.getReference(Patient.class, dto.getPatientId());
        return PatientVisit.builder()
                .id(dto.getId())
                .visitedOn(dto.getVisitedOn())
                .doctorId(dto.getDoctorId())
                .diagnosis(dto.getDiagnosis())
                .treatment(dto.getTreatment())
                .visitNotes(dto.getVisitNotes())
                .patient(reference)
                .build();
    }
} 