package com.tavuencas.sergio.usage_service.client;

import com.tavuencas.sergio.usage_service.dto.DeviceResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class DeviceClient {
    private final RestTemplate template;
    private final String baseUrl;

    public DeviceClient(@Value("${device.service.url}") String baseUrl) {
        this.template = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    public DeviceResponseDto getDeviceById(Long deviceId) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path("/{deviceId}")
                .buildAndExpand(deviceId)
                .toUriString();

        ResponseEntity<DeviceResponseDto> response = template.getForEntity(url, DeviceResponseDto.class);

        return response.getBody();
    }
}
