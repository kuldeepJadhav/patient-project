package com.hospitalmanagement.patientmanagement.dao;

import com.hospitalmanagement.patientmanagement.model.Patient;
import com.hospitalmanagement.patientmanagement.model.Status;
import com.hospitalmanagement.patientmanagement.model.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for Patient entity.
 * Provides basic CRUD operations and custom query methods.
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

    /**
     * Find patient by email address
     */
    Optional<Patient> findByEmail(String email);

    /**
     * Find patients by first name (case-insensitive)
     */
    List<Patient> findByFirstNameIgnoreCase(String firstName);

    /**
     * Find patients by date of birth range
     */
    List<Patient> findByDateOfBirthBetween(LocalDate startDate, LocalDate endDate);

    /**
     * Find patients by status with pagination
     */
    Page<Patient> findByStatus(Status status, Pageable pageable);
        /**
     * Custom query to find patients created within a date range
     */
    @Query("SELECT p FROM Patient p WHERE p.createdOn BETWEEN :startDateTime AND :endDateTime")
    List<Patient> findByCreatedDateRange(@Param("startDateTime") java.time.LocalDateTime startDateTime, 
                                       @Param("endDateTime") java.time.LocalDateTime endDateTime);
}  