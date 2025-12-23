package com.example.orderservice.utill;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("Your order is being processed"),
    CONFIRMED("Your order is confirmed"),
    SHIPPED("Your order is on the way"),
    DELIVERED("Your order has been delivered"),
    CANCELLED("Your order was cancelled");

    private final String message;

    OrderStatus(String message) {
        this.message = message;
    }

}