package com.tavuencas.sergio.usage_service.dto;

import lombok.Builder;

@Builder
public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        String address,
        boolean alerting,
        double energyAlertingThreshold
) {
}
