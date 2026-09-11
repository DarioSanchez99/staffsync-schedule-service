package com.staffsync.schedule.infrastructure.adapter.out.persistence.entity;

import com.staffsync.schedule.domain.model.ShiftType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "shifts", indexes = {
        @Index(name = "idx_shift_employee_date", columnList = "employee_id, date"),
        @Index(name = "idx_shift_date", columnList = "date"),
        @Index(name = "idx_shift_department", columnList = "department")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "employee_id", nullable = false)
    private UUID employeeId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type", nullable = false)
    private ShiftType shiftType;

    @Column(length = 500)
    private String notes;

    @Column(length = 100)
    private String department;
}
