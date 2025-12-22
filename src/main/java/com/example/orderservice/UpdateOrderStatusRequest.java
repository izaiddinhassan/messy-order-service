package com.example.orderservice;

public class UpdateOrderStatusRequest {
    private Order.OrderStatus status;

    // -- get set
    public Order.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(Order.OrderStatus status) {
        this.status = status;
    }
}
