package com.tavuencas.sergio.user_service.dto;

import lombok.Builder;

@Builder
public record UserRequestDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String address,
        boolean alerting,
        double energyAlertingThreshold
) {
}
