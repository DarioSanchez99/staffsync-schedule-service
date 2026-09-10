package com.staffsync.schedule.domain.port.in;

import com.staffsync.schedule.domain.model.Shift;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShiftUseCase {

    Shift createShift(Shift shift);

    Shift updateShift(UUID id, Shift shift);

    void deleteShift(UUID id);

    Optional<Shift> findById(UUID id);

    List<Shift> findAll();

    List<Shift> findByEmployee(UUID employeeId);

    List<Shift> findByWeek(LocalDate weekStart);

    List<Shift> findByDepartment(String department);

    java.util.Map<String, Object> publishWeek(LocalDate weekStart);
}
