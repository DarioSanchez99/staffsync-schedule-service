package com.staffsync.schedule.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Shift {

    private UUID id;
    private UUID employeeId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ShiftType shiftType;
    private String notes;
    private String department;
}
