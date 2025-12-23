package com.example.orderservice.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private Long productId;
    private Integer quantity;
    private Double priceAtPurchase; // Store price to avoid changes affecting old orders
}
