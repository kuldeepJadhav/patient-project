package com.hospitalmanagement.patientmanagement.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalmanagement.patientmanagement.dao.PatientRepository;
import com.hospitalmanagement.patientmanagement.dao.PatientVisitRepository;
import com.hospitalmanagement.patientmanagement.dto.PatientDto;
import com.hospitalmanagement.patientmanagement.model.BloodGroup;
import com.hospitalmanagement.patientmanagement.model.Gender;
import com.hospitalmanagement.patientmanagement.model.Patient;
import com.hospitalmanagement.patientmanagement.model.PatientVisit;
import com.hospitalmanagement.patientmanagement.model.Status;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "jwt.secret=iXew3rva2D5YlryDewkVWpmCZKBf2sgJgfzHIr2bGtk",
                "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.liquibase.enabled=false"
        }
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "/sql/integration-test-setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PatientIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientVisitRepository patientVisitRepository;

    private PatientDto requestDto;

    private static final String TEST_BASE64_SECRET = "iXew3rva2D5YlryDewkVWpmCZKBf2sgJgfzHIr2bGtk";

    @BeforeEach
    void setUp() {
        requestDto = PatientDto.builder()
                .firstName("John")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .gender(Gender.MALE)
                .address("123 Main St")
                .contactNumber("555-0101")
                .email("john.doe.it@sample.com")
                .city("New York")
                .state("NY")
                .bloodGroup(BloodGroup.O_PLUS)
                .emergencyContactName("Jane Doe")
                .emergencyContactPhone("555-0102")
                .createdBy(1L)
                .status(Status.ACTIVE)
                .build();
    }

    private String generateJwtToken(String username, List<String> roles) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + 3600_000);
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("id", 1L);
        Set<String> role_manager = Set.of("ROLE_MANAGER");
        customClaims.put("roles", role_manager);
        byte[] keyBytes = Base64.getUrlDecoder().decode(TEST_BASE64_SECRET);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
                .claims(customClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(exp)
                .signWith(secretKey)
                .compact();
    }

    public static void main(String[] args) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + 3600_000);
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("id", 1L);
        Set<String> role_manager = Set.of("ROLE_MANAGER");
        customClaims.put("roles", role_manager);
        byte[] keyBytes = Base64.getUrlDecoder().decode("iXew3rva2D5YlryDewkVWpmCZKBf2sgJgfzHIr2bGtk");
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        System.out.println(Jwts.builder()
                .claims(customClaims)
                .subject("admin")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(exp)
                .signWith(secretKey)
                .compact());
    }

    @Test
    void endToEnd_CreatePatient_Succeeds_WithValidJwt() throws Exception {
        String token = generateJwtToken("admin", List.of("ROLE_MANAGER"));

        mockMvc.perform(post("/api/v1/patients")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.email").value("john.doe.it@sample.com"));

        assertThat(patientRepository.findByEmail("john.doe.it@sample.com")).isPresent();
    }

    @Test
    void endToEnd_CohortFilter_WithBPDiagnosis_ReturnsCorrectPatients() throws Exception {
        String token = generateJwtToken("admin", List.of("ROLE_MANAGER"));

        // Create 5 patients with 2 visits each, all with 'BP' diagnosis
        createPatientsWithVisits();

        // Test cohort filter with BP diagnosis
        mockMvc.perform(get("/api/v1/patients")
                        .header("Authorization", "Bearer " + token)
                        .param("diagnosis", "BP")
                        .param("limit", "100")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(5))
                .andExpect(jsonPath("$.totalElements").value(5));

        // Verify all returned patients have visits with BP diagnosis
        assertThat(patientRepository.count()).isEqualTo(6);
        assertThat(patientVisitRepository.count()).isEqualTo(11);
    }

    @Test
    void endToEnd_UpdatePatient() throws Exception {
        String token = generateJwtToken("admin", List.of("ROLE_MANAGER"));

        Patient patient = getPatient(200);
        Patient saved = patientRepository.save(patient);

        PatientDto dto = PatientDto.builder()
                .firstName("John1")
                .lastName("Doe")
                .dateOfBirth(LocalDate.of(1990, 5, 15))
                .gender(Gender.MALE)
                .address("123 Main St")
                .contactNumber("555-0101")
                .email("john.doe.ita@sample.com")
                .city("New York")
                .state("NY")
                .bloodGroup(BloodGroup.O_PLUS)
                .emergencyContactName("Jane Doe")
                .emergencyContactPhone("555-0102")
                .createdBy(1L)
                .status(Status.ACTIVE)
                .build();

        mockMvc.perform(put("/api/v1/patients/"+saved.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.firstName").value("John1"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

    }

    private void createPatientsWithVisits() {
        // Create 5 patients
        Patient patient = getPatient(100);
        Patient savedPatient = patientRepository.save(patient);
        PatientVisit patientVisit = getPatientVisit(savedPatient, 100, 100);
        patientVisit.setDiagnosis("XYZ");
        patientVisitRepository.save(patientVisit);
        for (int i = 1; i <= 5; i++) {
            patient = getPatient(i);

            savedPatient = patientRepository.save(patient);

            // Create 2 visits for each patient with BP diagnosis
            for (int j = 1; j <= 2; j++) {
                PatientVisit visit = getPatientVisit(savedPatient, i, j);

                patientVisitRepository.save(visit);
            }
        }
    }

    private PatientVisit getPatientVisit(Patient savedPatient, int i, int j) {
        PatientVisit visit = PatientVisit.builder()
                .patient(savedPatient)
                .visitedOn(LocalDateTime.now().minusDays(j))
                .doctorId(1L)
                .diagnosis("BP")
                .treatment("Medication " + j)
                .visitNotes("Visit " + j + " notes for patient " + i)
                .build();
        return visit;
    }

    private Patient getPatient(int i) {
        Patient patient = Patient.builder()
                .firstName("Patient" + i)
                .lastName("Test")
                .dateOfBirth(LocalDate.of(1980 + i, 1, 1))
                .gender(Gender.MALE)
                .address("Address " + i)
                .contactNumber("555-000" + i)
                .email("patient" + i + "@test.com")
                .city("City " + i)
                .state("State " + i)
                .bloodGroup(BloodGroup.O_PLUS)
                .emergencyContactName("Emergency " + i)
                .emergencyContactPhone("555-999" + i)
                .createdBy(1L)
                .status(Status.ACTIVE)
                .build();
        return patient;
    }
}