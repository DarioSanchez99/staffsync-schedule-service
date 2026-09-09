package com.staffsync.schedule.domain.port.out;

import com.staffsync.schedule.domain.model.Shift;

public interface ScheduleEventPort {

    void publishShiftCreated(Shift shift);

    void publishShiftUpdated(Shift shift);
}
