package com.staffsync.schedule.infrastructure.adapter.out.persistence.adapter;

import com.staffsync.schedule.domain.model.Shift;
import com.staffsync.schedule.domain.port.out.ShiftRepository;
import com.staffsync.schedule.infrastructure.adapter.out.persistence.entity.ShiftEntity;
import com.staffsync.schedule.infrastructure.adapter.out.persistence.mapper.ShiftMapper;
import com.staffsync.schedule.infrastructure.adapter.out.persistence.repository.ShiftJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShiftRepositoryAdapter implements ShiftRepository {

    private final ShiftJpaRepository jpaRepository;
    private final ShiftMapper mapper;

    @Override
    public Shift save(Shift shift) {
        ShiftEntity entity = mapper.toEntity(shift);
        ShiftEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Shift> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Shift> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Shift> findByEmployeeId(UUID employeeId) {
        return jpaRepository.findByEmployeeId(employeeId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Shift> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return jpaRepository.findByDateBetween(startDate, endDate).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Shift> findByEmployeeIdAndDate(UUID employeeId, LocalDate date) {
        return jpaRepository.findByEmployeeIdAndDate(employeeId, date).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
