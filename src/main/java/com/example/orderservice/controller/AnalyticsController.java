package com.example.orderservice.controller;

import com.example.orderservice.dto.ProductPopularityDto;
import com.example.orderservice.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/popular")
    public ResponseEntity<List<ProductPopularityDto>> getPopularProducts() {
        return ResponseEntity.ok(analyticsService.getPopularProducts());
    }
}
