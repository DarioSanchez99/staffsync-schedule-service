package com.staffsync.schedule.infrastructure.adapter.out.messaging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftEvent {

    private String eventType;
    private UUID shiftId;
    private UUID employeeId;
    private LocalDate date;
    private String shiftType;
    private Instant timestamp;
}
