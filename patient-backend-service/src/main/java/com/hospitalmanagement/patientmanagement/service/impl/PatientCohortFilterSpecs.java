package com.hospitalmanagement.patientmanagement.service.impl;

import com.hospitalmanagement.patientmanagement.model.Patient;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class PatientCohortFilterSpecs {
    public static Specification<Patient> dobBetween(final LocalDateTime fromDob, final LocalDateTime toDob) {
        return (root, query, cb) -> {
            if (fromDob == null && toDob == null) return cb.conjunction();
            Path<LocalDateTime> dobPath = root.get("dob"); // LocalDate in entity
            var predicate = cb.conjunction();
            if (fromDob != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(dobPath, fromDob));
            }
            if (toDob != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(dobPath, toDob));
            }
            return predicate;
        };
    }

    /** Age between range */
    public static Specification<Patient> ageBetween(Integer fromAge, Integer toAge) {
        return (root, query, cb) -> {
            if (fromAge == null && toAge == null) return cb.conjunction();

            Path<Integer> agePath = root.get("age");

            if (fromAge != null && toAge != null) {
                return cb.between(agePath, fromAge, toAge);
            } else if (fromAge != null) {
                return cb.greaterThanOrEqualTo(agePath, fromAge);
            } else {
                return cb.lessThanOrEqualTo(agePath, toAge);
            }
        };
    }

    /** Join to visits and filter by diagnosis */
    public static Specification<Patient> diagnosisContains(String diagnosis) {
        return (root, query, cb) -> {
            if (diagnosis == null || diagnosis.isBlank()) return cb.conjunction();

            var visits = root.join("visits", JoinType.INNER);
            query.distinct(true); // avoid duplicate patients

            return cb.like(cb.lower(visits.get("diagnosis")),
                    "%" + diagnosis.toLowerCase() + "%");
        };
    }

    /** Join to visits and filter by doctorId */
    public static Specification<Patient> visitedByDoctor(Long doctorId) {
        return (root, query, cb) -> {
            if (doctorId == null) return cb.conjunction();

            var visits = root.join("visits", JoinType.INNER);
            query.distinct(true); // avoid duplicate patients

            return cb.equal(visits.get("doctorId"), doctorId);
        };
    }

    

}
