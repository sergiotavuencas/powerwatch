package com.tavuencas.sergio.user_service;

import com.tavuencas.sergio.user_service.entity.User;
import com.tavuencas.sergio.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class UserServiceApplicationTests {

	public static final int NUMBER_OF_USERS = 10;

	@Autowired
	private UserRepository repository;

	@Test
	void contextLoads() {
	}

	@Disabled
	@Test
	void createUsers() {
		for(int index = 1; index <= NUMBER_OF_USERS; index++) {
			var user = User.builder()
					.firstName("User" + index)
					.lastName("LastName" + index)
					.email("user" + index + "@email.com")
					.address(index + " Example St")
					.alerting(index % 2 == 0)
					.energyAlertingThreshold(1000.0 + index)
					.build();

			repository.save(user);
		}

		log.info("User repository has been populated: {}", repository.findAll());
	}
}
