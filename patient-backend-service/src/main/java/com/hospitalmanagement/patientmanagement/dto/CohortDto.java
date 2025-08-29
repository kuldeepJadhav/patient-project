package com.hospitalmanagement.patientmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hospitalmanagement.patientmanagement.model.CohortFilter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CohortDto {

    private Long id;

    @NotBlank(message = "Cohort name is required")
    @Size(max = 100, message = "Cohort name must be less than 100 characters")
    private String cohortName;

    private String cohortDescription;

    private CohortFilter cohortCriteria;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;
} 