package com.example.orderservice;
import java.util.List;

public class OrderRequest {
    private String customerType;
    private List<ProductItem> items;

    // -- get set
    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public List<ProductItem> getItems() {
        return items;
    }

    public void setItems(List<ProductItem> items) {
        this.items = items;
    }
}