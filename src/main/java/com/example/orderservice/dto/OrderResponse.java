package com.example.orderservice.dto;

import com.example.orderservice.model.enums.OrderStatus;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private String customerType;
    private Double total;
    private OrderStatus status;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
}
