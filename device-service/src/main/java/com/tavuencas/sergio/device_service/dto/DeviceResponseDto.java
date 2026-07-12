package com.tavuencas.sergio.device_service.dto;

import com.tavuencas.sergio.device_service.model.DeviceType;

public record DeviceResponseDto(
        Long id,
        String name,
        DeviceType type,
        String location,
        Long userId
) {
}
