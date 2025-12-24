package com.example.orderservice;

public class PopularSold {
    private Long productId;
    private Integer totalSold;

    public PopularSold(Long productId, Integer totalSold) {
        this.productId = productId;
        this.totalSold = totalSold;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getTotalSold() {
        return totalSold;
    }

    public void setTotalSold(Integer totalSold) {
        this.totalSold = totalSold;
    }
}
