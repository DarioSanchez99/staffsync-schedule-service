package com.staffsync.schedule.domain.port.out;

import com.staffsync.schedule.domain.model.Shift;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleEventPort {

    void publishShiftCreated(Shift shift);

    void publishShiftUpdated(Shift shift);

    void publishWeeklySchedule(List<Shift> shifts, LocalDate weekStart);
}
