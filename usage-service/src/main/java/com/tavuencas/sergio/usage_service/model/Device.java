package com.tavuencas.sergio.usage_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Device {
    private Long id;
    private String name;
    private String type;
    private String location;
    private Long userId;
    private Double energyConsumed;
}
