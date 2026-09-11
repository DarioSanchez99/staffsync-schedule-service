package com.staffsync.schedule.infrastructure.adapter.out.messaging;

import com.staffsync.schedule.domain.model.Shift;
import com.staffsync.schedule.domain.port.out.ScheduleEventPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleKafkaProducer implements ScheduleEventPort {

    private static final String RABBITMQ_EXCHANGE = "staffsync.notifications";

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishShiftCreated(Shift shift) {
        sendScheduleNotification("SCHEDULE_PUBLISHED", shift);
    }

    @Override
    public void publishShiftUpdated(Shift shift) {
        sendScheduleNotification("SCHEDULE_UPDATED", shift);
    }

    private void sendScheduleNotification(String type, Shift shift) {
        try {
            Map<String, Object> notification = new HashMap<>();
            notification.put("type", type);
            notification.put("shiftId", shift.getId().toString());
            notification.put("employeeIds", List.of(shift.getEmployeeId().toString()));
            notification.put("weekStart", shift.getDate() != null ? shift.getDate().toString() : null);
            notification.put("timestamp", Instant.now().toString());

            String routingKey = "SCHEDULE_PUBLISHED".equals(type) ? "schedule.published" : "schedule.updated";
            rabbitTemplate.convertAndSend(RABBITMQ_EXCHANGE, routingKey, notification);
            log.debug("Published schedule notification {} to RabbitMQ", type);
        } catch (Exception ex) {
            log.error("Failed to publish schedule notification to RabbitMQ: {}", ex.getMessage());
        }
    }

    @Override
    public void publishWeeklySchedule(List<Shift> shifts, LocalDate weekStart) {
        try {
            List<String> uniqueEmployeeIds = shifts.stream()
                    .map(s -> s.getEmployeeId().toString())
                    .distinct()
                    .collect(Collectors.toList());

            Map<String, Object> notification = new HashMap<>();
            notification.put("type", "SCHEDULE_WEEK_PUBLISHED");
            notification.put("weekStart", weekStart.toString());
            notification.put("employeeIds", uniqueEmployeeIds);
            notification.put("shiftCount", shifts.size());
            notification.put("timestamp", Instant.now().toString());

            rabbitTemplate.convertAndSend(RABBITMQ_EXCHANGE, "schedule.week.published", notification);
            log.info("Published weekly schedule for {} employees, {} shifts", uniqueEmployeeIds.size(), shifts.size());
        } catch (Exception ex) {
            log.error("Failed to publish weekly schedule notification: {}", ex.getMessage());
        }
    }
}
