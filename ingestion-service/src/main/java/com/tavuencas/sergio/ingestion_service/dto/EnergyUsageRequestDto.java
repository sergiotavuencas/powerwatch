package com.tavuencas.sergio.ingestion_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public record EnergyUsageRequestDto(
        Long deviceId,
        double energyConsumed,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant timestamp
) {
}
