package com.hospitalmanagement.patientmanagement.dao;

import com.hospitalmanagement.patientmanagement.model.Cohort;
import com.hospitalmanagement.patientmanagement.model.CohortFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
class CohortRepositoryTest {

    @Autowired
    private CohortRepository cohortRepository;
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void testSaveCohort() {

        LocalDateTime from = LocalDateTime.parse("2025-08-20 16:30:45", dateTimeFormatter);
        LocalDateTime to = LocalDateTime.parse("2025-08-27 16:30:45", dateTimeFormatter);
        CohortFilter filter = CohortFilter.builder()
                .fromDob(from)
                .toDob(to)
                .build();
        Cohort cohort = Cohort.builder()
                .cohortName("High Risk Patients")
                .cohortDescription("Patients flagged as high risk based on criteria")
                .cohortCriteria(filter)
                .build();

        Cohort saved = cohortRepository.saveAndFlush(cohort);
        Long id = saved.getId();
        Cohort dbObject = cohortRepository.getReferenceById(id);
        assertNotNull(dbObject.getId());
        assertEquals("High Risk Patients", dbObject.getCohortName());
        assertNotNull(dbObject.getCreatedOn());
        assertNotNull(dbObject.getUpdatedOn());
        assertEquals(from.toString(), dbObject.getCohortCriteria().getFromDob().toString());
        assertEquals(to.toString(), dbObject.getCohortCriteria().getToDob().toString());

    }
} 