package com.tavuencas.sergio.user_service.dto;

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
