package com.example.orderservice;

public class OrderResponse {
    private Long orderId;
    private String status;
    private Double total;
    private String message;

    public OrderResponse(Long orderId, String status, Double total, String message) {
        this.orderId = orderId;
        this.status = status;
        this.total = total;
        this.message = message;
    }

    // -- get set

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}