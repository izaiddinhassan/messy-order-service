package com.example.orderservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
    private Long id;
    private double price;
    private int stock;

}
