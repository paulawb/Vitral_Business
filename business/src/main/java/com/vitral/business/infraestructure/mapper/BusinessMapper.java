package com.vitral.business.infraestructure.mapper;

import com.vitral.business.domain.model.Business;
import com.vitral.business.domain.model.Schedule;
import com.vitral.business.infraestructure.driver_adapters.jpa_repository.BusinessData;
import com.vitral.business.infraestructure.driver_adapters.jpa_repository.ScheduleData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BusinessMapper {

    public Business toBusiness(BusinessData data) {
        List<Schedule> schedules = new ArrayList<>();
        for (ScheduleData item : data.getSchedules()) {
            schedules.add(Schedule.builder()
                    .dayOfWeek(item.getDayOfWeek())
                    .openAt(item.getOpenAt())
                    .closeAt(item.getCloseAt())
                    .closed(item.getClosed())
                    .build());
        }

        return Business.builder()
                .tenantId(data.getTenantId())
                .slug(data.getSlug())
                .nombre(data.getNombre())
                .descripcion(data.getDescripcion())
                .logoUrl(data.getLogoUrl())
                .primaryColor(data.getPrimaryColor())
                .secondaryColor(data.getSecondaryColor())
                .businessType(data.getBusinessType())
                .vertical(data.getVertical())
                .phone(data.getPhone())
                .email(data.getEmail())
                .whatsappNumber(data.getWhatsappNumber())
                .instagramUrl(data.getInstagramUrl())
                .facebookUrl(data.getFacebookUrl())
                .tiktokUrl(data.getTiktokUrl())
                .timezone(data.getTimezone())
                .active(data.getActive())
                .schedules(schedules)
                .build();
    }

    public BusinessData toData(Business business) {
        BusinessData data = BusinessData.builder()
                .tenantId(business.getTenantId())
                .slug(business.getSlug())
                .nombre(business.getNombre())
                .descripcion(business.getDescripcion())
                .logoUrl(business.getLogoUrl())
                .primaryColor(business.getPrimaryColor())
                .secondaryColor(business.getSecondaryColor())
                .businessType(business.getBusinessType())
                .vertical(business.getVertical())
                .phone(business.getPhone())
                .email(business.getEmail())
                .whatsappNumber(business.getWhatsappNumber())
                .instagramUrl(business.getInstagramUrl())
                .facebookUrl(business.getFacebookUrl())
                .tiktokUrl(business.getTiktokUrl())
                .timezone(business.getTimezone())
                .active(business.getActive())
                .schedules(new ArrayList<>())
                .build();

        if (business.getSchedules() != null) {
            for (Schedule schedule : business.getSchedules()) {
                data.getSchedules().add(ScheduleData.builder()
                        .dayOfWeek(schedule.getDayOfWeek())
                        .openAt(schedule.getOpenAt())
                        .closeAt(schedule.getCloseAt())
                        .closed(schedule.getClosed())
                        .business(data)
                        .build());
            }
        }

        return data;
    }
}
