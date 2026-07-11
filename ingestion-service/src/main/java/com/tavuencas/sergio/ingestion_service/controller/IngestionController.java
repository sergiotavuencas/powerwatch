package com.tavuencas.sergio.ingestion_service.controller;

import com.tavuencas.sergio.ingestion_service.dto.EnergyUsageRequestDto;
import com.tavuencas.sergio.ingestion_service.service.IngestionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ingestion")
public class IngestionController {
    private final IngestionService service;

    public IngestionController(IngestionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void ingestData(@RequestBody EnergyUsageRequestDto request) {
        service.ingestEnergyUsage(request);
    }
}
