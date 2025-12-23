package com.example.orderservice.controller;

import com.example.orderservice.dto.ProductDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductMockController {

    @GetMapping("/{id}")
    public ProductDTO getProduct(@PathVariable Long id) {
        ProductDTO product = new ProductDTO();
        product.setId(id);
        product.setName("Mock Product " + id);
        product.setPrice(100.0);
        product.setStock(1000); // High stock to ensure successful orders
        return product;
    }
}
