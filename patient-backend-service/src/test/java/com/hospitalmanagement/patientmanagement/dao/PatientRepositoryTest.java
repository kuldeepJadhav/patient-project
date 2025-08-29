package com.hospitalmanagement.patientmanagement.dao;

import com.hospitalmanagement.patientmanagement.model.BloodGroup;
import com.hospitalmanagement.patientmanagement.model.Gender;
import com.hospitalmanagement.patientmanagement.model.Patient;
import com.hospitalmanagement.patientmanagement.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PatientRepository using Spring Data JPA test support.
 * Tests CRUD operations, custom queries, and derived query methods.
 */
@DataJpaTest
@ActiveProfiles("test")
class PatientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PatientRepository patientRepository;

    private Patient testPatient1;
    private Patient testPatient2;
    private Patient testPatient3;

    @BeforeEach
    void setUp() {
        // Create test patients
        testPatient1 = Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .gender(Gender.MALE)
                .address("123 Main St")
                .contactNumber("555-0101")
                .email("john.doe@email.com")
                .city("New York")
                .state("NY")
                .bloodGroup(BloodGroup.O_PLUS)
                .emergencyContactName("Jane Doe")
                .emergencyContactPhone("555-0102")
                .createdBy(1L)
                .status(Status.ACTIVE)
                .build();

        testPatient2 = Patient.builder()
                .firstName("Jane")
                .lastName("Smith")
                .dateOfBirth(LocalDate.of(1985, 8, 22))
                .gender(Gender.FEMALE)
                .address("456 Oak Ave")
                .contactNumber("555-0202")
                .email("jane.smith@email.com")
                .city("Los Angeles")
                .state("CA")
                .bloodGroup(BloodGroup.A_PLUS)
                .emergencyContactName("John Smith")
                .emergencyContactPhone("555-0203")
                .createdBy(1L)
                .status(Status.ACTIVE)
                .build();

        testPatient3 = Patient.builder()
                .firstName("Bob")
                .lastName("Johnson")
                .dateOfBirth(LocalDate.of(1975, 3, 10))
                .gender(Gender.MALE)
                .address("789 Pine St")
                .contactNumber("555-0303")
                .email("bob.johnson@email.com")
                .city("Chicago")
                .state("IL")
                .bloodGroup(BloodGroup.B_PLUS)
                .emergencyContactName("Mary Johnson")
                .emergencyContactPhone("555-0304")
                .createdBy(2L)
                .status(Status.INACTIVE)
                .build();

        // Persist test data
        entityManager.persistAndFlush(testPatient1);
        entityManager.persistAndFlush(testPatient2);
        entityManager.persistAndFlush(testPatient3);
    }

    @Test
    void testSavePatient() {
        // Given
        Patient newPatient = Patient.builder()
                .firstName("Alice")
                .lastName("Brown")
                .dateOfBirth(LocalDate.of(1995, 12, 3))
                .gender(Gender.FEMALE)
                .address("321 Elm St")
                .contactNumber("555-0404")
                .email("alice.brown@email.com")
                .city("Boston")
                .state("MA")
                .bloodGroup(BloodGroup.AB_MINUS)
                .emergencyContactName("Charlie Brown")
                .emergencyContactPhone("555-0405")
                .createdBy(1L)
                .status(Status.ACTIVE)
                .build();

        // When
        Patient savedPatient = patientRepository.save(newPatient);

        // Then
        assertNotNull(savedPatient.getId());
        assertEquals("Alice", savedPatient.getFirstName());
        assertEquals("Brown", savedPatient.getLastName());
        assertEquals("alice.brown@email.com", savedPatient.getEmail());
        assertNotNull(savedPatient.getCreatedOn());
        assertNotNull(savedPatient.getUpdatedOn());
    }

    @Test
    void testFindById() {
        // When
        Optional<Patient> foundPatient = patientRepository.findById(testPatient1.getId());

        // Then
        assertTrue(foundPatient.isPresent());
        assertEquals("John", foundPatient.get().getFirstName());
        assertEquals("Doe", foundPatient.get().getLastName());
    }

    @Test
    void testFindAll() {
        // When
        List<Patient> allPatients = patientRepository.findAll();

        // Then
        assertEquals(3, allPatients.size());
        assertTrue(allPatients.stream().anyMatch(p -> p.getFirstName().equals("John")));
        assertTrue(allPatients.stream().anyMatch(p -> p.getFirstName().equals("Jane")));
        assertTrue(allPatients.stream().anyMatch(p -> p.getFirstName().equals("Bob")));
    }

    @Test
    void testFindByEmail() {
        // When
        Optional<Patient> foundPatient = patientRepository.findByEmail("john.doe@email.com");

        // Then
        assertTrue(foundPatient.isPresent());
        assertEquals("John", foundPatient.get().getFirstName());
        assertEquals("Doe", foundPatient.get().getLastName());
    }

    @Test
    void testFindByEmailNotFound() {
        // When
        Optional<Patient> foundPatient = patientRepository.findByEmail("nonexistent@email.com");

        // Then
        assertFalse(foundPatient.isPresent());
    }

    @Test
    void testFindByFirstName() {
        // When
        List<Patient> foundPatients = patientRepository.findByFirstNameIgnoreCase("John");

        // Then
        assertEquals(1, foundPatients.size());
        assertEquals("John", foundPatients.get(0).getFirstName());
        assertEquals("Doe", foundPatients.get(0).getLastName());
    }

    @Test
    void testFindByFirstNameIgnoreCase() {
        // When
        List<Patient> foundPatients = patientRepository.findByFirstNameIgnoreCase("john");

        // Then
        assertEquals(1, foundPatients.size());
        assertEquals("John", foundPatients.get(0).getFirstName());
    }





    @Test
    void testFindByDateOfBirthBetween() {
        // Given
        LocalDate startDate = LocalDate.of(1980, 1, 1);
        LocalDate endDate = LocalDate.of(1995, 12, 31);

        // When
        List<Patient> patientsInRange = patientRepository.findByDateOfBirthBetween(startDate, endDate);

        // Then
        assertEquals(2, patientsInRange.size());
        assertTrue(patientsInRange.stream().anyMatch(p -> p.getFirstName().equals("John")));
        assertTrue(patientsInRange.stream().anyMatch(p -> p.getFirstName().equals("Jane")));
    }


    @Test
    void testFindByStatusWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 1);

        // When
        Page<Patient> activePatientsPage = patientRepository.findByStatus(Status.ACTIVE, pageable);

        // Then
        assertEquals(1, activePatientsPage.getContent().size());
        assertEquals(2, activePatientsPage.getTotalElements());
        assertEquals(2, activePatientsPage.getTotalPages());
        assertTrue(activePatientsPage.getContent().get(0).getStatus() == Status.ACTIVE);
    }

    @Test
    void testFindByCreatedDateRange() {
        // Given
        LocalDateTime startDateTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endDateTime = LocalDateTime.now().plusDays(1);

        // When
        List<Patient> patientsInDateRange = patientRepository.findByCreatedDateRange(startDateTime, endDateTime);

        // Then
        assertEquals(3, patientsInDateRange.size());
    }


    @Test
    void testUpdatePatient() {
        // Given
        Patient patientToUpdate = patientRepository.findById(testPatient1.getId()).orElseThrow();
        patientToUpdate.setFirstName("Jonathan");
        patientToUpdate.setLastName("Doe-Smith");
        patientToUpdate.setEmail("jonathan.doe@email.com");

        // When
        Patient updatedPatient = patientRepository.save(patientToUpdate);

        // Then
        assertEquals("Jonathan", updatedPatient.getFirstName());
        assertEquals("Doe-Smith", updatedPatient.getLastName());
        assertEquals("jonathan.doe@email.com", updatedPatient.getEmail());
        assertNotNull(updatedPatient.getUpdatedOn());
    }

    @Test
    void testDeletePatient() {
        // Given
        Long patientId = testPatient1.getId();

        // When
        patientRepository.deleteById(patientId);

        // Then
        Optional<Patient> deletedPatient = patientRepository.findById(patientId);
        assertFalse(deletedPatient.isPresent());
    }

    @Test
    void testCountPatients() {
        // When
        long totalCount = patientRepository.count();

        // Then
        assertEquals(3, totalCount);
    }

    @Test
    void testFindAllPaginatedSortedByUpdatedOnDesc() throws Exception {
        // Ensure distinct updatedOn values by updating entities with delays
        Patient p1 = patientRepository.findById(testPatient1.getId()).orElseThrow();
        p1.setCity("NYC");
        patientRepository.saveAndFlush(p1);

        Thread.sleep(50);

        Patient p2 = patientRepository.findById(testPatient2.getId()).orElseThrow();
        p2.setCity("LA");
        patientRepository.saveAndFlush(p2);

        Thread.sleep(50);

        Patient p3 = patientRepository.findById(testPatient3.getId()).orElseThrow();
        p3.setCity("CHI");
        patientRepository.saveAndFlush(p3);

        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "updatedOn"));

        Page<Patient> page = patientRepository.findAll(pageable);

        assertEquals(2, page.getContent().size());
        // Most recently updated should be first (p3), then p2
        assertEquals(p3.getId(), page.getContent().get(0).getId());
        assertEquals(p2.getId(), page.getContent().get(1).getId());
        assertEquals(3, page.getTotalElements());
    }
} 