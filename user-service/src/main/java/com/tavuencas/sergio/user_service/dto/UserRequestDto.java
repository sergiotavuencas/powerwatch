package com.tavuencas.sergio.user_service.dto;

public record UserRequestDto(
        String firstName,
        String lastName,
        String email,
        String address
) {
}
