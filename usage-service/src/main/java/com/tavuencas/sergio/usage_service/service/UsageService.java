package com.tavuencas.sergio.usage_service.service;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import com.tavuencas.sergio.kafka.event.AlertingEvent;
import com.tavuencas.sergio.kafka.event.EnergyUsageEvent;
import com.tavuencas.sergio.usage_service.client.DeviceClient;
import com.tavuencas.sergio.usage_service.client.UserClient;
import com.tavuencas.sergio.usage_service.dto.DeviceResponseDto;
import com.tavuencas.sergio.usage_service.dto.UserResponseDto;
import com.tavuencas.sergio.usage_service.model.DeviceEnergy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UsageService {

    private InfluxDBClient influxDBClient;
    private DeviceClient deviceClient;
    private UserClient userClient;

    @Value("${influx.bucket}")
    private String influxBucket;

    @Value("${influx.org}")
    private String influxOrg;

    private final KafkaTemplate<String, AlertingEvent> kafkaTemplate;

    public UsageService(
            InfluxDBClient influxDBClient,
            DeviceClient deviceClient,
            UserClient userClient,
            KafkaTemplate<String, AlertingEvent> kafkaTemplate
    ) {
        this.influxDBClient = influxDBClient;
        this.deviceClient = deviceClient;
        this.userClient = userClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "energy-usage", groupId = "usage-service")
    public void energyUsageEvent(EnergyUsageEvent event) {
//        log.info("Received energy usage event: {}", event);

        Point point = Point.measurement("energy_usage")
                .addTag("deviceId", String.valueOf(event.deviceId()))
                .addField("energyConsumed", event.energyConsumed())
                .time(event.timestamp(), WritePrecision.MS);

        influxDBClient.getWriteApiBlocking().writePoint(influxBucket, influxOrg, point);
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void aggregateDeviceEnergyUsage() {
        final Instant now = Instant.now();
        final Instant oneHourAgo = now.minusSeconds(3600);

        String fluxQuery = String.format("""
            from(bucket: "%s")
                |> range(start: time(v: "%s"), stop: time(v: "%s"))
                |> filter(fn: (r) => r["_measurement"] == "energy_usage")
                |> filter(fn: (r) => r["_field"] == "energyConsumed")
                |> group(columns: ["deviceId"])
                |> sum(column: "_value")
            """, influxBucket, oneHourAgo.toString(), now);

        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(fluxQuery, influxOrg);

        List<DeviceEnergy> deviceEnergies = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                String deviceIdStr = (String) record.getValueByKey("deviceId");
                Double energyConsumed = record.getValueByKey("_value") instanceof Number
                        ? ((Number) record.getValueByKey("_value")).doubleValue()
                        : 0.0;

                deviceEnergies.add(
                        DeviceEnergy.builder()
                                .deviceId(Long.valueOf(deviceIdStr))
                                .energyConsumed(energyConsumed)
                                .build()
                );
            }
        }

        log.info("Aggregated device energies over the past hour: {}", deviceEnergies);

        for (DeviceEnergy deviceEnergy : deviceEnergies) {
            final DeviceResponseDto response = deviceClient.getDeviceById(deviceEnergy.getDeviceId());

            if (response == null || response.id() == null) {
                log.warn("Device not found for ID: {}", deviceEnergy.getDeviceId());
                continue;
            }

            deviceEnergy.setUserId(response.userId());
        }

        deviceEnergies.removeIf(device -> device.getUserId() == null);

        Map<Long, List<DeviceEnergy>> userDeviceEnergyMap = deviceEnergies
                .stream()
                .collect(Collectors.groupingBy(DeviceEnergy::getUserId));

        log.info("User-Device Energy Map: {}", userDeviceEnergyMap);

        List<Long> userIds = new ArrayList<>(userDeviceEnergyMap.keySet());
        final Map<Long, Double> userThresholdMap = new HashMap<>();
        final Map<Long, String> userEmailMap = new HashMap<>();

        for (final Long userId : userIds) {
            try {
                UserResponseDto response = userClient.getUserById(userId);

                if (response == null || response.id() == null || !response.alerting()) {
                    log.warn("User not found or alerting disabled for ID: {}", userId);
                    continue;
                }

                userThresholdMap.put(userId, response.energyAlertingThreshold());
                userEmailMap.put(userId, response.email());
            } catch (Exception e) {
                log.warn("Failed to fetch user for ID: {}", userId);
            }
        }

        log.info("User Threshold Map: {}", userThresholdMap);

        final List<Long> alertedUsers = new ArrayList<>(userThresholdMap.keySet());

        for (final Long userId : alertedUsers) {
            final Double threshold = userThresholdMap.get(userId);
            final List<DeviceEnergy> devices = userDeviceEnergyMap.get(userId);

            final Double totalConsumption = devices
                    .stream()
                    .mapToDouble(DeviceEnergy::getEnergyConsumed)
                    .sum();

            if (totalConsumption > threshold) {
                log.info("ALERT: User Id {} has exceeded the energy threshold! " +
                        "Total Consumption: {}, Threshold: {}",
                        userId, totalConsumption, threshold
                );

                final AlertingEvent event = AlertingEvent.builder()
                        .userId(userId)
                        .message("Energy consumption threshold exceeded")
                        .threshold(threshold)
                        .energyConsumed(totalConsumption)
                        .email(userEmailMap.get(userId))
                        .build();

                kafkaTemplate.send("energy-alert", event);
            } else {
                log.info("User ID {} is within the energy threshold. " +
                        "Total Consumption: {}, Threshold: {}",
                        userId, totalConsumption, threshold
                );
            }
        }

    }
}
