package com.tavuencas.sergio.usage_service.client;

import com.tavuencas.sergio.usage_service.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class UserClient {
    private final RestTemplate template;
    private final String baseUrl;

    public UserClient(@Value("${user.service.url}") String baseUrl) {
        this.template = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    public UserResponseDto getUserById(Long userId) {
        String url = UriComponentsBuilder
                .fromUriString(baseUrl)
                .path("/{userId}")
                .buildAndExpand(userId)
                .toUriString();

        ResponseEntity<UserResponseDto> response = template.getForEntity(url, UserResponseDto.class);

        return response.getBody();
    }
}
