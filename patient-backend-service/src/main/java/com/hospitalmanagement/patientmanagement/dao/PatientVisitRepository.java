package com.hospitalmanagement.patientmanagement.dao;

import com.hospitalmanagement.patientmanagement.model.PatientVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientVisitRepository extends JpaRepository<PatientVisit, Long> {

    Page<PatientVisit> findAllByOrderByVisitedOnDesc(Pageable pageable);

    Page<PatientVisit> findByPatient_IdOrderByVisitedOnDesc(Long patientId, Pageable pageable);
}
