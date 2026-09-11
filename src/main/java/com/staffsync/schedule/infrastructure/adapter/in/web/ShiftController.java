package com.staffsync.schedule.infrastructure.adapter.in.web;

import com.staffsync.schedule.domain.model.Shift;
import com.staffsync.schedule.domain.model.ShiftType;
import com.staffsync.schedule.domain.port.in.ShiftUseCase;
import com.staffsync.schedule.infrastructure.adapter.in.web.api.ShiftsApi;
import com.staffsync.schedule.infrastructure.adapter.in.web.dto.CreateShiftRequest;
import com.staffsync.schedule.infrastructure.adapter.in.web.dto.ShiftResponse;
import com.staffsync.schedule.infrastructure.adapter.in.web.dto.UpdateShiftRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ShiftController implements ShiftsApi {

    private final ShiftUseCase shiftUseCase;

    @Override
    public ResponseEntity<List<ShiftResponse>> listShifts(UUID employeeId, LocalDate date) {
        List<Shift> shifts;
        if (employeeId != null) {
            shifts = shiftUseCase.findByEmployee(employeeId);
        } else {
            shifts = shiftUseCase.findAll();
        }
        return ResponseEntity.ok(shifts.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @Override
    public ResponseEntity<ShiftResponse> createShift(CreateShiftRequest request) {
        Shift shift = toDomain(request);
        Shift created = shiftUseCase.createShift(shift);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Override
    public ResponseEntity<ShiftResponse> getShiftById(UUID id) {
        return shiftUseCase.findById(id)
                .map(s -> ResponseEntity.ok(toResponse(s)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<ShiftResponse> updateShift(UUID id, UpdateShiftRequest request) {
        Shift partial = toDomain(request);
        Shift updated = shiftUseCase.updateShift(id, partial);
        return ResponseEntity.ok(toResponse(updated));
    }

    @Override
    public ResponseEntity<Void> deleteShift(UUID id) {
        shiftUseCase.deleteShift(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ShiftResponse>> getShiftsByEmployee(UUID employeeId) {
        List<ShiftResponse> responses = shiftUseCase.findByEmployee(employeeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Override
    public ResponseEntity<List<ShiftResponse>> getShiftsByWeek(LocalDate weekStart) {
        List<ShiftResponse> responses = shiftUseCase.findByWeek(weekStart).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/shifts/publish-week")
    public ResponseEntity<Map<String, Object>> publishWeek(@RequestBody Map<String, String> body) {
        LocalDate weekStart = LocalDate.parse(body.get("weekStart"));
        Map<String, Object> result = shiftUseCase.publishWeek(weekStart);
        return ResponseEntity.ok(result);
    }

    private Shift toDomain(CreateShiftRequest request) {
        return Shift.builder()
                .employeeId(request.getEmployeeId())
                .date(request.getDate())
                .startTime(request.getStartTime() != null ? LocalTime.parse(request.getStartTime()) : null)
                .endTime(request.getEndTime() != null ? LocalTime.parse(request.getEndTime()) : null)
                .shiftType(request.getShiftType() != null
                        ? ShiftType.valueOf(request.getShiftType().name()) : null)
                .notes(request.getNotes())
                .department(request.getDepartment() != null ? request.getDepartment().toUpperCase() : null)
                .build();
    }

    private Shift toDomain(UpdateShiftRequest request) {
        return Shift.builder()
                .date(request.getDate())
                .startTime(request.getStartTime() != null ? LocalTime.parse(request.getStartTime()) : null)
                .endTime(request.getEndTime() != null ? LocalTime.parse(request.getEndTime()) : null)
                .shiftType(request.getShiftType() != null
                        ? ShiftType.valueOf(request.getShiftType().name()) : null)
                .notes(request.getNotes())
                .build();
    }

    private ShiftResponse toResponse(Shift shift) {
        ShiftResponse response = new ShiftResponse();
        response.setId(shift.getId());
        response.setEmployeeId(shift.getEmployeeId());
        response.setDate(shift.getDate());
        response.setStartTime(shift.getStartTime() != null ? shift.getStartTime().toString() : null);
        response.setEndTime(shift.getEndTime() != null ? shift.getEndTime().toString() : null);
        if (shift.getShiftType() != null) {
            response.setShiftType(com.staffsync.schedule.infrastructure.adapter.in.web.dto.ShiftType
                    .valueOf(shift.getShiftType().name()));
        }
        response.setNotes(shift.getNotes());
        response.setDepartment(shift.getDepartment());
        return response;
    }
}
