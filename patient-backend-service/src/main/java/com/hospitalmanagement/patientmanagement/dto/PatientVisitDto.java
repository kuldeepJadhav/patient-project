package com.hospitalmanagement.patientmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PatientVisitDto {
    private Long id;
    private LocalDateTime visitedOn;
    private Long doctorId;
    private String diagnosis;
    private String treatment;
    private String visitNotes;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private Long patientId;
}
