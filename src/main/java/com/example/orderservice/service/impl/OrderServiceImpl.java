package com.example.orderservice.service.impl;

import com.example.orderservice.dto.*;
import com.example.orderservice.entity.Order;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.service.OrderService;
import com.example.orderservice.utill.OrderStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.orderservice.utill.Constants.FREE_SHIPPING_DISCOUNT;
import static com.example.orderservice.utill.Constants.FREE_SHIPPING_THRESHOLD;


public class OrderServiceImpl implements OrderService {


    private final RestTemplate restTemplate;
    private final OrderRepository orderRepository;
    @Value("${product-service.api.url}")
    private  String productServiceUrl;

    public OrderServiceImpl(RestTemplate restTemplate, OrderRepository orderRepository) {
        this.restTemplate = restTemplate;
        this.orderRepository = orderRepository;
    }
@Override
public OrderResponse createOrder(OrderRequest request) {

        validateDuplicates(request.getItems());

        double total = request.getItems()
                .stream()
                .mapToDouble(this::calculateItemTotal)
                .sum();

        double discountedTotal =
                total - (total * request.getCustomerType().getDiscount());

        if (discountedTotal > FREE_SHIPPING_THRESHOLD) {
            discountedTotal -= FREE_SHIPPING_DISCOUNT;
        }

        Order order = new Order();
        order.setCustomerType(request.getCustomerType().name());
        order.setTotal(discountedTotal);
        order.setStatus("PENDING");

        Order saved = orderRepository.save(order);

        return new OrderResponse(
                saved.getId(),
                saved.getTotal(),
                saved.getStatus()
        );
    }
@Override
public OrderStatusResponse getOrderStatus(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found: " + id));

        OrderStatus status = OrderStatus.valueOf(order.getStatus());

        return new OrderStatusResponse(
                order.getId(),
                status.name(),
                status.getMessage(),
                order.getTotal()
        );
    }

    @Override
    public void updateStatus(Long orderId, OrderStatus newStatus) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Order not found: " + orderId));

        order.setStatus(newStatus.name());
        orderRepository.save(order);}


    private double calculateItemTotal(OrderItemRequest item) {

        ProductDto product = fetchProduct(item.getProductId());

        if (product.getStock() <= 0) {
            throw new IllegalArgumentException(
                    "Product " + item.getProductId() + " is out of stock");
        }

        if (item.getQuantity() > product.getStock()) {
            throw new IllegalArgumentException(
                    "Not enough stock for product " + item.getProductId());
        }

        return product.getPrice() * item.getQuantity();
    }

    private ProductDto fetchProduct(Long productId) {
        try {
            return restTemplate.getForObject(
                    productServiceUrl,
                    ProductDto.class,
                    productId
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to fetch product " + productId);
        }
    }

    private void validateDuplicates(List<OrderItemRequest> items) {
        Set<Long> productIds = new HashSet<>();
        for (OrderItemRequest item : items) {
            if (!productIds.add(item.getProductId())) {
                throw new IllegalArgumentException(
                        "Duplicate product detected: " + item.getProductId());
            }
        }
    }
}
