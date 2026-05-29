package com.vitral.booking.domain.model.gateway;

import java.time.LocalDate;
import java.time.LocalTime;

public interface SoftLockGateway {
    void hold(String tenantId, LocalDate date, LocalTime startTime, LocalTime endTime);
    boolean isHeld(String tenantId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
