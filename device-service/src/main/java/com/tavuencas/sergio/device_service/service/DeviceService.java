package com.tavuencas.sergio.device_service.service;

import com.tavuencas.sergio.device_service.dto.DeviceRequestDto;
import com.tavuencas.sergio.device_service.dto.DeviceResponseDto;
import com.tavuencas.sergio.device_service.entity.Device;
import com.tavuencas.sergio.device_service.exception.DeviceNotFoundException;
import com.tavuencas.sergio.device_service.repository.DeviceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DeviceService {
    private DeviceRepository repository;

    public DeviceService(DeviceRepository repository) {
        this.repository = repository;
    }

    public DeviceResponseDto create(DeviceRequestDto request) {
        log.info("Creating device : {}", request);

        final Device createdDevice = Device.builder()
                .name(request.name())
                .type(request.type())
                .location(request.location())
                .userId(request.userId())
                .build();

        Device savedDevice = repository.save(createdDevice);

        return toDto(savedDevice);
    }

    public DeviceResponseDto getDeviceById(Long id) {
        log.info("Getting device by id: {}", id);

        Device device = repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id " + id));

        return toDto(device);
    }

    public DeviceResponseDto update(Long id, DeviceRequestDto request) {
        log.info("Updating device with id: {}", id, request);

        Device device = repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id " + id));

        device.setName(request.name());
        device.setType(request.type());
        device.setLocation(request.location());
        device.setUserId(request.userId());

        repository.save(device);

        return toDto(device);
    }

    public void delete(Long id) {
        log.info("Deleting device with id: {}", id);

        Device device = repository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Device not found with id " + id));

        repository.delete(device);
    }

    private DeviceResponseDto toDto(Device device) {
        return new DeviceResponseDto(
                device.getId(),
                device.getName(),
                device.getType(),
                device.getLocation()
        );
    }
}
