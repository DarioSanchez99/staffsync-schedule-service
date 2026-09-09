package com.staffsync.schedule.domain.service;

import com.staffsync.schedule.domain.model.Shift;
import com.staffsync.schedule.domain.port.in.ShiftUseCase;
import com.staffsync.schedule.domain.port.out.ScheduleEventPort;
import com.staffsync.schedule.domain.port.out.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShiftService implements ShiftUseCase {

    private final ShiftRepository shiftRepository;
    private final ScheduleEventPort scheduleEventPort;

    @Override
    public Shift createShift(Shift shift) {
        shift.setId(UUID.randomUUID());
        Shift saved = shiftRepository.save(shift);
        scheduleEventPort.publishShiftCreated(saved);
        return saved;
    }

    @Override
    public Shift updateShift(UUID id, Shift updatedShift) {
        Shift existing = shiftRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Shift not found: " + id));

        if (updatedShift.getDate() != null) {
            existing.setDate(updatedShift.getDate());
        }
        if (updatedShift.getStartTime() != null) {
            existing.setStartTime(updatedShift.getStartTime());
        }
        if (updatedShift.getEndTime() != null) {
            existing.setEndTime(updatedShift.getEndTime());
        }
        if (updatedShift.getShiftType() != null) {
            existing.setShiftType(updatedShift.getShiftType());
        }
        if (updatedShift.getNotes() != null) {
            existing.setNotes(updatedShift.getNotes());
        }

        Shift saved = shiftRepository.save(existing);
        scheduleEventPort.publishShiftUpdated(saved);
        return saved;
    }

    @Override
    public void deleteShift(UUID id) {
        shiftRepository.deleteById(id);
    }

    @Override
    public Optional<Shift> findById(UUID id) {
        return shiftRepository.findById(id);
    }

    @Override
    public List<Shift> findAll() {
        return shiftRepository.findAll();
    }

    @Override
    public List<Shift> findByEmployee(UUID employeeId) {
        return shiftRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<Shift> findByWeek(LocalDate weekStart) {
        LocalDate weekEnd = weekStart.plusDays(6);
        return shiftRepository.findByDateBetween(weekStart, weekEnd);
    }

    @Override
    public List<Shift> findByDepartment(String department) {
        // Department-based filtering would require integration with employee-service
        // For now, returns all shifts as a placeholder
        return shiftRepository.findAll();
    }
}
