package com.vitral.booking.infraestructure.driver_adapters.memory;

import com.vitral.booking.domain.model.gateway.SoftLockGateway;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemorySoftLockGateway implements SoftLockGateway {

    private final Map<String, LocalDateTime> locks = new ConcurrentHashMap<>();

    @Override
    public void hold(String tenantId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        locks.put(key(tenantId, date, startTime, endTime), LocalDateTime.now().plusMinutes(10));
    }

    @Override
    public boolean isHeld(String tenantId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        String key = key(tenantId, date, startTime, endTime);
        LocalDateTime until = locks.get(key);
        if (until == null) {
            return false;
        }
        if (until.isBefore(LocalDateTime.now())) {
            locks.remove(key);
            return false;
        }
        return true;
    }

    private String key(String tenantId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        return tenantId + "|" + date + "|" + startTime + "|" + endTime;
    }
}
