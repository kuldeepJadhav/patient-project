package com.hospitalmanagement.patientmanagement.service.impl;

import com.hospitalmanagement.patientmanagement.converter.impl.PatientConverter;
import com.hospitalmanagement.patientmanagement.dao.CohortRepository;
import com.hospitalmanagement.patientmanagement.dao.PatientRepository;
import com.hospitalmanagement.patientmanagement.dao.PatientVisitRepository;
import com.hospitalmanagement.patientmanagement.dto.PatientDto;
import com.hospitalmanagement.patientmanagement.dto.UserBean;
import com.hospitalmanagement.patientmanagement.model.Cohort;
import com.hospitalmanagement.patientmanagement.model.CohortFilter;
import com.hospitalmanagement.patientmanagement.model.Patient;
import com.hospitalmanagement.patientmanagement.service.PatientService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientConverter converter;

    private final CohortRepository cohortRepository;

    private final UserBean userBean;
    @Autowired
    public PatientServiceImpl(final PatientRepository patientRepository, final PatientConverter converter, UserBean userBean, CohortRepository cohortRepository) {
       this.patientRepository = patientRepository;
       this.converter = converter;
       this.userBean = userBean;
       this.cohortRepository = cohortRepository;
    }

    public PatientDto create(PatientDto patientDto) {
        Patient patient = converter.fromDto(patientDto);
        Long createdBy = userBean.getId();
        patient.setCreatedBy(createdBy);
        Patient saved = patientRepository.save(patient);
        return converter.toDto(saved);
    }

    public PatientDto update(PatientDto dto, Long id) {
        Optional<Patient> dbEntry = patientRepository.findById(id);
        if (dbEntry.isEmpty())
            throw new EntityNotFoundException("Patient with id " + id + " not found");
        Patient patient = dbEntry.get();
        if (!Objects.isNull(dto.getFirstName())) {
            patient.setFirstName(dto.getFirstName());
        }
        if (!Objects.isNull(dto.getLastName())) {
            patient.setLastName(dto.getLastName());
        }
        if (!Objects.isNull(dto.getAddress())) {
            patient.setAddress(dto.getAddress());
        }
        if(!Objects.isNull(dto.getCity())) {
            patient.setCity(dto.getCity());
        }
        if (!Objects.isNull(dto.getBloodGroup())) {
            patient.setBloodGroup(dto.getBloodGroup());
        }
        if (!Objects.isNull(dto.getContactNumber())) {
            patient.setContactNumber(dto.getContactNumber());
        }
        if (!Objects.isNull(dto.getEmail())) {
            patient.setEmail(dto.getEmail());
        }
        if (!Objects.isNull(dto.getEmergencyContactName())) {
            patient.setEmergencyContactName(dto.getEmergencyContactName());
        }
        if (!Objects.isNull(dto.getEmergencyContactName())) {
            patient.setEmergencyContactName(dto.getEmergencyContactName());
        }
        patientRepository.save(patient);
        return converter.toDto(patient);
    }

    @Override
    public Page<PatientDto> getPatientsForCohortId(Long cohortId) {
        Optional<Cohort> cohortOptional = cohortRepository.findById(cohortId);
        if (cohortOptional.isEmpty())
            new EntityNotFoundException("Cohort not found for id " + cohortId);
        Cohort cohort = cohortOptional.get();
        return this.getPatientsForCohort(cohort.getCohortCriteria());
    }

    public Page<PatientDto> getPatientsForCohort(CohortFilter filter) {
        Specification<Patient> patientSpecification = this.fromFilter(filter);
        Sort.Direction direction = Sort.Direction.fromString(filter.getSortDirection());
        Sort sort = Sort.by(direction, filter.getSortBy() == null ? "dateOfBirth" : filter.getSortBy());
        Pageable of = PageRequest.of(filter.getOffset(), filter.getLimit(), sort);
        List<PatientDto> list = patientRepository.findAll(patientSpecification, of)
                .stream().map(converter::toDto)
                .collect(Collectors.toList());
        return new PageImpl<>(list);
    }

    public Long getCountForFilter(CohortFilter filter) {
        Specification<Patient> patientSpecification = this.fromFilter(filter);
        return patientRepository.count(patientSpecification);
    }

    public PatientDto getById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Patient not found for id " + id));
        return converter.toDto(patient);
    }

    private Specification<Patient> fromFilter(CohortFilter f) {
        return Specification.allOf(
                PatientCohortFilterSpecs.dobBetween(f.getFromDob(), f.getToDob()),
                PatientCohortFilterSpecs.ageBetween(f.getFromAge(), f.getToAge()),
                PatientCohortFilterSpecs.diagnosisContains(f.getDiagnosis()),
                PatientCohortFilterSpecs.visitedByDoctor(f.getDoctorId())
        );
    }

}