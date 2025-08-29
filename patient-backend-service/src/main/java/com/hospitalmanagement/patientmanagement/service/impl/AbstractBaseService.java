//package com.hospitalmanagement.patientmanagement.service.impl;
//
//import com.hospitalmanagement.patientmanagement.converter.Converter;
//import com.hospitalmanagement.patientmanagement.service.BaseService;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public abstract class AbstractBaseService<T, R, ID> implements BaseService<T, R, ID> {
//    protected final JpaRepository<T, ID> repository;
//    protected final Converter<T, R> converter;
//
//
//    protected AbstractBaseService(JpaRepository<T, ID> repository, Converter<T, R> converter) {
//        this.repository = repository;
//        this.converter = converter;
//    }
//
//    @Override
//    public R saveAndFlush(R entity) {
//        T saved = repository.saveAndFlush(converter.fromDto(entity));
//        return converter.toDto(saved);
//    }
//
//    @Override
//    public Optional<R> getById(ID id) {
//        Optional<T> byId = repository.findById(id);
//        if (byId.isEmpty())
//            throw new EntityNotFoundException("Entity not found, Entity " + );
//        T t = byId.get();
//        return converter.toDto(t);
//    }
//
//    protected abstract String getEntity();
//
//    @Override
//    public Page<R> findAll(Pageable pageable) {
//        return repository.findAll(pageable).map(converter::toDto);
//    }
//}