package com.example.orderservice.dto;

import com.example.orderservice.utill.CustomerType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class OrderRequest {
    private CustomerType customerType;
    private List<OrderItemRequest> items;

}
