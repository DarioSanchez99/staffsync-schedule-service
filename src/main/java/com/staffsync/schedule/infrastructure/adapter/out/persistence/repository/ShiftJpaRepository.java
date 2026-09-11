package com.staffsync.schedule.infrastructure.adapter.out.persistence.repository;

import com.staffsync.schedule.infrastructure.adapter.out.persistence.entity.ShiftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ShiftJpaRepository extends JpaRepository<ShiftEntity, UUID> {

    List<ShiftEntity> findByEmployeeId(UUID employeeId);

    List<ShiftEntity> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<ShiftEntity> findByEmployeeIdAndDate(UUID employeeId, LocalDate date);

    List<ShiftEntity> findByDepartment(String department);

    List<ShiftEntity> findByDepartmentAndDateBetween(String department, LocalDate startDate, LocalDate endDate);
}
