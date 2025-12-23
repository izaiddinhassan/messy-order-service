package com.example.orderservice.client;

import com.example.orderservice.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
public class ProductClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${product.service.url}")
    private String productServiceUrl;

    public Optional<ProductDTO> getProductById(Long id) {
        try {
            String url = productServiceUrl + "/api/products/" + id;
            ProductDTO product = restTemplate.getForObject(url, ProductDTO.class);
            return Optional.ofNullable(product);
        } catch (Exception e) {
            // Log error
            return Optional.empty();
        }
    }
}
