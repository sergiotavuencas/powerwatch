package com.tavuencas.sergio.usage_service.dto;

import lombok.Builder;

@Builder
public record DeviceResponseDto(
        Long id,
        String name,
        String type,
        String location,
        Long userId,
        Double energyConsumed
) {
}
