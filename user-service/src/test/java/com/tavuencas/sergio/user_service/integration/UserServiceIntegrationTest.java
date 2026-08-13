package com.tavuencas.sergio.user_service.integration;

import com.tavuencas.sergio.user_service.dto.UserRequestDto;
import com.tavuencas.sergio.user_service.dto.UserResponseDto;
import com.tavuencas.sergio.user_service.repository.UserRepository;
import com.tavuencas.sergio.user_service.testsupport.MySqlTestcontainersBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
public class UserServiceIntegrationTest extends MySqlTestcontainersBase {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    void createUser_viaRestApi_persistsAndReturnsUser() {
        UserRequestDto requestDto = UserRequestDto.builder()
                .firstName("Sérgio")
                .lastName("Tavuencas")
                .email("sergio_tavuencas@outlook.com")
                .address("R.Orville Correia de Toledo")
                .alerting(true)
                .energyAlertingThreshold(2000.0)
                .build();

        ResponseEntity<UserResponseDto> response = restTemplate
                .postForEntity("/api/v1/user", requestDto, UserResponseDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().firstName()).isEqualTo("Sérgio");
        assertThat(response.getBody().lastName()).isEqualTo("Tavuencas");
        assertThat(response.getBody().email()).isEqualTo("sergio_tavuencas@outlook.com");
        assertThat(response.getBody().address()).isEqualTo("R.Orville Correia de Toledo");
        assertThat(response.getBody().alerting()).isTrue();
        assertThat(response.getBody().energyAlertingThreshold()).isEqualTo(2000.0);

        ResponseEntity<UserResponseDto> loaded = restTemplate
                .getForEntity("/api/v1/user/" + response.getBody().id(), UserResponseDto.class);

        assertThat(loaded.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loaded.getBody()).isNotNull();
        assertThat(loaded.getBody().email()).isEqualTo("sergio_tavuencas@outlook.com");

    }
}
