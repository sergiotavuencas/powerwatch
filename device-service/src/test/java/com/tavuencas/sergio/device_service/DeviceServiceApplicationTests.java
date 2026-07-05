package com.tavuencas.sergio.device_service;

import com.tavuencas.sergio.device_service.entity.Device;
import com.tavuencas.sergio.device_service.model.DeviceType;
import com.tavuencas.sergio.device_service.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class DeviceServiceApplicationTests {

	public static final int NUMBER_OF_DEVICES = 200;
	public static final int LOCATIONS = 200;
	public static final int USERS = 10;

	@Autowired
	private DeviceRepository repository;

	@Test
	void contextLoads() {
	}

	@Disabled
	@Test
	void createDevices() {
		for(int index = 1; index <= NUMBER_OF_DEVICES; index++) {
			var device = Device.builder()
					.name("Device" + index)
					.type(DeviceType.values()[index % DeviceType.values().length])
					.location("Location" + ((index % LOCATIONS) + 1))
					.userId((long) ((index % USERS) + 1))
					.build();

			repository.save(device);
		}

		log.info("Device repository has been populated.");
	}
}
