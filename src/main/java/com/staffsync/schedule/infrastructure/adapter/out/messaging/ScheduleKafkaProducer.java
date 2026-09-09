package com.staffsync.schedule.infrastructure.adapter.out.messaging;

import com.staffsync.schedule.domain.model.Shift;
import com.staffsync.schedule.domain.port.out.ScheduleEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleKafkaProducer implements ScheduleEventPort {

    private static final String TOPIC = "schedule-events";

    private final KafkaTemplate<String, ShiftEvent> kafkaTemplate;

    @Override
    public void publishShiftCreated(Shift shift) {
        ShiftEvent event = buildEvent("SHIFT_CREATED", shift);
        send(event);
    }

    @Override
    public void publishShiftUpdated(Shift shift) {
        ShiftEvent event = buildEvent("SHIFT_UPDATED", shift);
        send(event);
    }

    private ShiftEvent buildEvent(String eventType, Shift shift) {
        return ShiftEvent.builder()
                .eventType(eventType)
                .shiftId(shift.getId())
                .employeeId(shift.getEmployeeId())
                .date(shift.getDate())
                .shiftType(shift.getShiftType() != null ? shift.getShiftType().name() : null)
                .timestamp(Instant.now())
                .build();
    }

    private void send(ShiftEvent event) {
        kafkaTemplate.send(TOPIC, event.getShiftId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish event {}: {}", event.getEventType(), ex.getMessage());
                    } else {
                        log.debug("Published event {} for shift {}", event.getEventType(), event.getShiftId());
                    }
                });
    }
}
