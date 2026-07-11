package com.tavuencas.sergio.ingestion_service.service;

import com.tavuencas.sergio.ingestion_service.dto.EnergyUsageRequestDto;
import com.tavuencas.sergio.kafka.event.EnergyUsageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
@Service
public class IngestionService {

    private final KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate;

    public IngestionService(KafkaTemplate<String, EnergyUsageEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void ingestEnergyUsage(EnergyUsageRequestDto request) {
         EnergyUsageEvent event = EnergyUsageEvent.builder()
                 .deviceId(request.deviceId())
                 .energyConsumed(request.energyConsumed())
                 .timestamp(request.timestamp())
                 .build();

         kafkaTemplate.send("energy-usage", event);
         log.info("Ingested Energy Usage Event: {}", event);
    }
}
