package com.tavuencas.sergio.usage_service.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UsageDto(
        Long userId,
        List<DeviceResponseDto> devices
) {
}
