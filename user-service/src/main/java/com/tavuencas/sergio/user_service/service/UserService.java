package com.tavuencas.sergio.user_service.service;

import com.tavuencas.sergio.user_service.dto.UserRequestDto;
import com.tavuencas.sergio.user_service.dto.UserResponseDto;
import com.tavuencas.sergio.user_service.entity.User;
import com.tavuencas.sergio.user_service.exception.UserNotFoundException;
import com.tavuencas.sergio.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserResponseDto create(UserRequestDto request) {
        log.info("Creating user: {}", request);

        final User createdUser = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .address(request.address())
                .alerting(false)
                .energyAlertingThreshold(0)
                .build();

        User savedUser = repository.save(createdUser);

        return toDto(savedUser);
    }

    public UserResponseDto getUserById(Long id) {
        log.info("Getting User by ID: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        return toDto(user);
    }

    public void update(Long id, UserRequestDto request) {
        log.info("Updating User with ID: {}", id);

        User user = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setAddress(request.address());

        repository.save(user);
    }

    public void delete(Long id) {
        log.info("Deleting User with ID: {}", id);

        User user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));

        repository.delete(user);
    }

    private UserResponseDto toDto(User user) {
        return new UserResponseDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getAddress()
        );
    }
}
