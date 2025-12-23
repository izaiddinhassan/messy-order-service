package com.example.orderservice.model.enums;

public enum CustomerType {
    VIP(0.15),
    PREMIUM(0.10),
    REGULAR(0.05),
    NEW(0.02);

    private final double discount;

    CustomerType(double discount) {
        this.discount = discount;
    }

    public double getDiscount() {
        return discount;
    }
}
