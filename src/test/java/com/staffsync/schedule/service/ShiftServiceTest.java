package com.staffsync.schedule.service;

import com.staffsync.schedule.domain.model.Shift;
import com.staffsync.schedule.domain.model.ShiftType;
import com.staffsync.schedule.domain.port.out.ScheduleEventPort;
import com.staffsync.schedule.domain.port.out.ShiftRepository;
import com.staffsync.schedule.domain.service.ShiftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShiftServiceTest {

    @Mock
    private ShiftRepository shiftRepository;

    @Mock
    private ScheduleEventPort scheduleEventPort;

    private ShiftService shiftService;

    @BeforeEach
    void setUp() {
        shiftService = new ShiftService(shiftRepository, scheduleEventPort);
    }

    @Test
    void createShift_success() {
        // Given
        Shift input = Shift.builder()
                .employeeId(UUID.randomUUID())
                .date(LocalDate.now())
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(16, 0))
                .shiftType(ShiftType.MORNING)
                .build();

        when(shiftRepository.save(any(Shift.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        Shift created = shiftService.createShift(input);

        // Then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getShiftType()).isEqualTo(ShiftType.MORNING);

        verify(shiftRepository).save(any(Shift.class));
        verify(scheduleEventPort).publishShiftCreated(any(Shift.class));
    }

    @Test
    void findByEmployee_returnsList() {
        // Given
        UUID employeeId = UUID.randomUUID();
        List<Shift> expected = List.of(
                Shift.builder()
                        .id(UUID.randomUUID())
                        .employeeId(employeeId)
                        .date(LocalDate.now())
                        .shiftType(ShiftType.MORNING)
                        .build(),
                Shift.builder()
                        .id(UUID.randomUUID())
                        .employeeId(employeeId)
                        .date(LocalDate.now().plusDays(1))
                        .shiftType(ShiftType.AFTERNOON)
                        .build()
        );

        when(shiftRepository.findByEmployeeId(employeeId)).thenReturn(expected);

        // When
        List<Shift> result = shiftService.findByEmployee(employeeId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(s -> s.getEmployeeId().equals(employeeId));

        verify(shiftRepository).findByEmployeeId(employeeId);
    }

    @Test
    void deleteShift_callsRepository() {
        // Given
        UUID shiftId = UUID.randomUUID();

        // When
        shiftService.deleteShift(shiftId);

        // Then
        verify(shiftRepository).deleteById(shiftId);
    }
}
