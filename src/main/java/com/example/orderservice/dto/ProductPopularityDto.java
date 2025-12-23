package com.example.orderservice.dto;

import lombok.Getter;

@Getter
public class ProductPopularityDto {
    private Long productId;
    private long orderCount;

    public ProductPopularityDto(Long productId, long orderCount) {
        this.productId = productId;
        this.orderCount = orderCount;
    }


}
