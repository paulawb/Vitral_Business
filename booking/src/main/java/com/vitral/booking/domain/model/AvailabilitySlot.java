package com.vitral.booking.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailabilitySlot {
    private String startTime;
    private String endTime;
    private boolean available;
}
