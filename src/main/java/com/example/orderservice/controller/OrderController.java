package com.example.orderservice.controller;

import com.example.orderservice.dto.*;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.entity.Order;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    


    @PostMapping("/")
    public ResponseEntity<OrderResponse> makeOrder(
            @RequestBody  OrderRequest request) {

        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusResponse> getOrder(@PathVariable Long id) {

        OrderStatusResponse response = orderService.getOrderStatus(id);
        return ResponseEntity.ok(response);
    }
    

    
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateOrderStatusRequest request) {

        orderService.updateStatus(id, request.getStatus());
        return ResponseEntity.noContent().build();
    }
}
