package com.example.orderservice.service.impl;

import com.example.orderservice.dto.ProductPopularityDto;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;


public class AnalyticsServiceImpl implements AnalyticsService {
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public List<ProductPopularityDto> getPopularProducts() {

        Map<Long, Long> productCounts = new HashMap<>();
        List<Order> orders = orderRepository.findAll();
        countProducts(productCounts, orders);
        List<ProductPopularityDto> productPopularityDtoList = productPopularityDtoList(productCounts);

        productPopularityDtoList.sort((a, b) -> Long.compare(b.getOrderCount(), a.getOrderCount()));

        return productPopularityDtoList;
    }
    private void countProducts(Map<Long,Long> productCounts,List<Order> orders){

        for (Order order : orders) {
            for (String item : order.getItems()) {
                String[] parts = item.split(":");
                Long productId = Long.parseLong(parts[0]);

                if (productCounts.containsKey(productId)) {
                    productCounts.put(productId, productCounts.get(productId) + 1);
                } else {
                    productCounts.put(productId, 1L);
                }
            }
        }
     

    }

    private List<ProductPopularityDto> productPopularityDtoList(Map<Long,Long> productCounts){
        List<ProductPopularityDto> popularProducts = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : productCounts.entrySet()) {
            popularProducts.add(new ProductPopularityDto(entry.getKey(), entry.getValue()));
        }
        return popularProducts;
    }

}


