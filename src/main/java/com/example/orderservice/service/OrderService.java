package com.example.orderservice.service;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.dto.OrderResponse;
import com.example.orderservice.dto.OrderStatusResponse;
import com.example.orderservice.dto.ProductPopularityDto;
import com.example.orderservice.utill.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {
    OrderResponse createOrder(OrderRequest request);

    OrderStatusResponse getOrderStatus(Long id);

    void updateStatus(Long orderId, OrderStatus newStatus);
}
