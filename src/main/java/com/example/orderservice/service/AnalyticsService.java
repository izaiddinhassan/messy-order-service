package com.example.orderservice.service;

import com.example.orderservice.dto.ProductPopularityDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface AnalyticsService {
    List<ProductPopularityDto> getPopularProducts();
}
