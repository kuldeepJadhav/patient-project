package com.hospitalmanagement.patientmanagement.dao;

import com.hospitalmanagement.patientmanagement.model.Cohort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CohortRepository extends JpaRepository<Cohort, Long> {}