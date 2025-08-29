package com.hospitalmanagement.patientmanagement.converter;

public interface Converter<M, D> {
    D toDto(M model);
    M fromDto(D dto);
} 