package com.hospitalmanagement.patientmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SuperBuilder
@NoArgsConstructor
@Data
public class CohortFilter {

    private LocalDateTime fromDob;
    private LocalDateTime toDob;

    private Integer toAge;
    private Integer fromAge;

    private String diagnosis;

    private Long doctorId;

    private String sortDirection = "DESC";
    private String sortBy; // firstname, DOB

    private Integer limit = 50;
    private Integer offset = 0;

 }


