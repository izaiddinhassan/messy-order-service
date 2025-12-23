package com.example.orderservice.dto;

import com.example.orderservice.utill.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {
    private OrderStatus status;


}
