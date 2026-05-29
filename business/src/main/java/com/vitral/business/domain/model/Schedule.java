package com.vitral.business.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {
    private String dayOfWeek;
    private String openAt;
    private String closeAt;
    private Boolean closed;
}
