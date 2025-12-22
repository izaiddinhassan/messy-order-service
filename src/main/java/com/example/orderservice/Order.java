package com.example.orderservice;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // database table
@Table(name = "orders")
public class Order {
    public enum OrderStatus {
        PENDING("Your order is being processed"),
        CONFIRMED("Your order is confirmed"),
        SHIPPED("Your order is on the way"),
        DELIVERED("Your order has been delivered"),
        CANCELLED("Your order was cancelled");
        private final String description;

        OrderStatus(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
    }

    @Id // primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto fill
    private Long id;
    
    private String customerType;
    private Double total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL) // 1 -> many
    @JoinColumn(name= "order_id") // child link to parent
    private List<OrderItem> items = new ArrayList<>();

    private LocalDateTime createdAt;
    
    @PrePersist // do it before save disk
    public void prePersist() {
        createdAt = LocalDateTime.now(); // save create time
    }

    // -- get set
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
