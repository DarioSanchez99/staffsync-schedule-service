package com.staffsync.schedule.domain.port.out;

import com.staffsync.schedule.domain.model.Shift;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShiftRepository {

    Shift save(Shift shift);

    Optional<Shift> findById(UUID id);

    List<Shift> findAll();

    List<Shift> findByEmployeeId(UUID employeeId);

    List<Shift> findByDateBetween(LocalDate startDate, LocalDate endDate);

    void deleteById(UUID id);

    List<Shift> findByEmployeeIdAndDate(UUID employeeId, LocalDate date);

    List<Shift> findByDepartment(String department);
}
