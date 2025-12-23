package com.example.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private static final String PRODUCT_API_URL = "http://localhost:8081/api/products/";

    @Autowired
    private RestTemplate rt;

    public Map<Long, ProductInformation> getProductsByIDs(List<Long> productIDs) {
        Map<Long, ProductInformation> result = new HashMap<>();
        for(Long productId: productIDs) {
            try {
                String url = PRODUCT_API_URL + productId;
                ProductInformation info = rt.getForObject(url, ProductInformation.class);
                if (info != null) {
                    result.put(productId, info);
                }
            } catch (Exception e) {
                System.out.println("Error fetching product ID:" + productId + " Error Message: " + e.getMessage());
            }
//            mock Data
//            ProductInformation fakeInfo = new ProductInformation();
//            fakeInfo.setId(productId);
//            fakeInfo.setPrice(100.0);
//            fakeInfo.setStock(999);
//            result.put(productId, fakeInfo);
        }
        return result;
    }
}
