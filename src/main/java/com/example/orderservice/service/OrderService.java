package com.example.orderservice.service;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.dto.*;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.model.enums.CustomerType;
import com.example.orderservice.model.enums.OrderStatus;
import com.example.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        // Validate CustomerType
        CustomerType customerType;
        try {
            customerType = CustomerType.valueOf(request.getCustomerType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid customer type: " + request.getCustomerType());
        }

        Order order = new Order();
        order.setCustomerType(customerType);

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        // Process items
        for (OrderItemRequest itemRequest : request.getItems()) {
            ProductDTO product = productClient.getProductById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            // Note: In a real system we would decrease stock here via API call to
            // ProductService

            double itemTotal = product.getPrice() * itemRequest.getQuantity();
            total += itemTotal;

            OrderItem orderItem = new OrderItem(product.getId(), itemRequest.getQuantity(), product.getPrice());
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);

        // Calculate Final Total with Discount
        double discount = customerType.getDiscount();
        double finalTotal = total - (total * discount);

        if (finalTotal > 1000) {
            finalTotal -= 50; // Free shipping rule
        }
        order.setTotal(finalTotal);

        // Save
        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    public OrderResponse getOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        return mapToResponse(order);
    }

    // Corrected logic for popular using Stream API
    public List<Map<String, Object>> getPopularProducts() {
        List<Order> orders = orderRepository.findAll();

        Map<Long, Integer> productCount = new HashMap<>();

        for (Order order : orders) {
            for (OrderItem item : order.getItems()) {
                productCount.put(item.getProductId(),
                        productCount.getOrDefault(item.getProductId(), 0) + item.getQuantity());
            }
        }

        return productCount.entrySet().stream()
                .sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("productId", entry.getKey());
                    map.put("count", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public void updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
        try {
            order.setStatus(OrderStatus.valueOf(status));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        orderRepository.save(order);
    }

    private OrderResponse mapToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerType(order.getCustomerType().name());
        response.setTotal(order.getTotal());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponse> itemResponses = order.getItems().stream().map(item -> {
            OrderItemResponse ir = new OrderItemResponse();
            ir.setProductId(item.getProductId());
            ir.setQuantity(item.getQuantity());
            ir.setPriceAtPurchase(item.getPriceAtPurchase());
            return ir;
        }).collect(Collectors.toList());

        response.setItems(itemResponses);
        return response;
    }
}
