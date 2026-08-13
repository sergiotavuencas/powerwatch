package com.tavuencas.sergio.usage_service.client;

import com.tavuencas.sergio.usage_service.dto.DeviceResponseDto;
import com.tavuencas.sergio.usage_service.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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

    public List<DeviceResponseDto> getAllDevicesForUser(Long userId) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path("/user/{userId}")
                .buildAndExpand(userId)
                .toUriString();

        ResponseEntity<DeviceResponseDto[]> response = template.getForEntity(url, DeviceResponseDto[].class);
        DeviceResponseDto[] devices = response.getBody();

        return devices == null ? List.of() : List.of(devices);
    }
}
