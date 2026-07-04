package com.tavuencas.sergio.user_service.dto;

public record UserResponseDto(
        String firstName,
        String lastName,
        String email,
        String address
) {
}
