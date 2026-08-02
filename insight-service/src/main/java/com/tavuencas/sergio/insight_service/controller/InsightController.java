package com.tavuencas.sergio.insight_service.controller;

import com.tavuencas.sergio.insight_service.dto.InsightDto;
import com.tavuencas.sergio.insight_service.service.InsightService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/insight")
public class InsightController {
    private final InsightService service;

    public InsightController(InsightService service) {
        this.service = service;
    }

    @GetMapping("/savings-tips/{userId}")
    public ResponseEntity<InsightDto> getSavingsTips(@PathVariable Long userId) {
        final InsightDto insightDto = service.getSavingsTips(userId);
        return ResponseEntity.ok(insightDto);
    }

    @GetMapping("/overview/{userId}")
    public ResponseEntity<InsightDto> getOverview(@PathVariable Long userId) {
        final InsightDto insightDto = service.getOverview(userId);
        return ResponseEntity.ok(insightDto);
    }
}
