package com.hospitalmanagement.patientmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hospitalmanagement.patientmanagement.model.BloodGroup;
import com.hospitalmanagement.patientmanagement.model.Gender;
import com.hospitalmanagement.patientmanagement.model.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {
    private Long id;
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;
    private String lastName;
    @Past(message = "dateOfBirth cannot be in the future")
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;
    private String contactNumber;
    @Email(message = "Invalid email format")
    private String email;
    private String city;
    private String state;
    private BloodGroup bloodGroup;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private Long createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;
    private Status status;
}
