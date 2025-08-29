package com.hospitalmanagement.patientmanagement.converter.impl;

import com.hospitalmanagement.patientmanagement.converter.Converter;
import com.hospitalmanagement.patientmanagement.dto.PatientDto;
import com.hospitalmanagement.patientmanagement.model.Patient;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientConverter implements Converter<Patient, PatientDto> {

    @Override
    public PatientDto toDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        return PatientDto.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .address(patient.getAddress())
                .contactNumber(patient.getContactNumber())
                .email(patient.getEmail())
                .city(patient.getCity())
                .state(patient.getState())
                .bloodGroup(patient.getBloodGroup())
                .emergencyContactName(patient.getEmergencyContactName())
                .emergencyContactPhone(patient.getEmergencyContactPhone())
                .createdBy(patient.getCreatedBy())
                .createdOn(patient.getCreatedOn())
                .updatedOn(patient.getUpdatedOn())
                .status(patient.getStatus())
                .build();
    }

    @Override
    public Patient fromDto(PatientDto dto) {
        if (dto == null) {
            return null;
        }
        return Patient.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(dto.getGender())
                .address(dto.getAddress())
                .contactNumber(dto.getContactNumber())
                .email(dto.getEmail())
                .city(dto.getCity())
                .state(dto.getState())
                .bloodGroup(dto.getBloodGroup())
                .emergencyContactName(dto.getEmergencyContactName())
                .emergencyContactPhone(dto.getEmergencyContactPhone())
                .createdBy(dto.getCreatedBy())
                .status(dto.getStatus())
                .build();
    }
} 