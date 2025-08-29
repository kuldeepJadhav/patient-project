package com.hospitalmanagement.patientmanagement.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospitalmanagement.patientmanagement.dto.PatientDto;
import com.hospitalmanagement.patientmanagement.interceptor.UserPopulatorInterceptor;
import com.hospitalmanagement.patientmanagement.model.BloodGroup;
import com.hospitalmanagement.patientmanagement.model.Gender;
import com.hospitalmanagement.patientmanagement.model.Status;
import com.hospitalmanagement.patientmanagement.service.PatientService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@Import(value = {PatientControllerTest.TestSecurityConfig.class, PatientControllerTest.TestWebMvcConfig.class})
@ComponentScan( basePackages = {"com.hospitalmanagement.patientmanagement.interceptor", "com.hospitalmanagement.patientmanagement.dto"})
@ActiveProfiles("test")
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    private PatientDto validPatientDto;
    private PatientDto savedPatientDto;
    private String validJwtToken;
    private String invalidJwtToken;

    private String invalidRoleJwt;
    static String testSecret = "iXew3rva2D5YlryDewkVWpmCZKBf2sgJgfzHIr2bGtk";

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    static class TestSecurityConfig {

        @Bean
        public PatientController landingController(@Autowired  PatientService patientService) {
            return new PatientController(patientService);
        }
        
        @Bean
        public PatientService patientService() {
            return mock(PatientService.class);
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(AbstractHttpConfigurer::disable)
                    .cors(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authorize -> authorize
                            .anyRequest().authenticated()
                    )
                    .oauth2ResourceServer(oauth2 -> oauth2
                            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter()))

                    );
            return http.build();
        }

        @Bean
        public JwtDecoder jwtDecoder(){
            byte[] keyBytes = Base64.getUrlDecoder().decode(testSecret);
            SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes); // or HmacSHA512
            // Match the algorithm you used to sign (HS256/384/512)
            return NimbusJwtDecoder.withSecretKey(secretKey)
                    .build();
        }

        Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthConverter() {
            return jwt -> {
                Set<String> roles = new HashSet<>((ArrayList)jwt.getClaims().get("roles"));
                Collection<GrantedAuthority> authorities = roles == null ? List.of() :
                        roles.stream()
                                .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role) // add ROLE_ prefix
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toSet());
                return new JwtAuthenticationToken(jwt, authorities);
            };
        }
    }

    @Configuration
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public static class TestWebMvcConfig implements WebMvcConfigurer {
        private final UserPopulatorInterceptor userPopulatorInterceptor;
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            WebMvcConfigurer.super.addInterceptors(registry);
            registry.addInterceptor(userPopulatorInterceptor).order(Ordered.LOWEST_PRECEDENCE).addPathPatterns("/api/v1/**").excludePathPatterns("/actuator/**");
        }

    }

    @BeforeEach
    void setUp() {
        validPatientDto = PatientDto.builder()
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

        savedPatientDto = PatientDto.builder()
                .id(1L)
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
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        // Generate valid JWT token with ADMIN role
        validJwtToken = generateJwtToken("admin", "ROLE_MANAGER", 1l);
        invalidRoleJwt = generateJwtToken("admin", "ROLE_TEST", 1l);
        // Generate invalid JWT token
        invalidJwtToken = "invalid.jwt.token";
    }

    private String generateJwtToken(String username, String role, Long id) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000); // 1 hour
        Map<String, Object> customClaims = new HashMap<>();
        customClaims.put("id", id);
        Set<String> role_manager = Set.of(role);
        customClaims.put("roles", role_manager);
        byte[] keyBytes = Base64.getUrlDecoder().decode(testSecret);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
                .claims(customClaims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate)
                .signWith(secretKey)
                .compact();
    }

    @Test
    void createPatient_WithValidJwtToken_ShouldSucceed() throws Exception {
        when(patientService.create(any(PatientDto.class))).thenReturn(savedPatientDto);

        mockMvc.perform(post("/api/v1/patients")
                        .header("Authorization", "Bearer " + validJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPatientDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@email.com"));
    }

    @Test
    void createPatient_WithInvalidJwtToken_ShouldBeUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                        .header("Authorization", "Bearer " + invalidJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPatientDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createPatient_WithoutJwtToken_ShouldBeUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPatientDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createPatient_WithoutProperRole_ShouldBeUnauthorized() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                        .header("Authorization", "Bearer " + invalidRoleJwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validPatientDto)))
                .andExpect(status().isForbidden());
    }
} 