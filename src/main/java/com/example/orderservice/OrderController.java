package com.example.orderservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

@RestController // HTTP request
public class OrderController {
    @Value("${order.shipping.full-fill-price}")
    private double full_fill_price;

    @Value("${order.shipping.full-fill-discount}")
    private double full_fill_discount;


    enum CustomerType {
        VIP(0.15),
        PREMIUM(0.10),
        REGULAR(0.05),
        NEW(0.02);

        private final double discount;
        private static final Map<String, CustomerType> Customer_Map = new HashMap<>();

        static {
            for (CustomerType type: CustomerType.values()) {
                Customer_Map.put(type.name().toUpperCase(), type);
            }
        }

        CustomerType(double discount) {
            this.discount = discount;
        }

        private double getDiscount() {
            return this.discount;
        }

        public static double getDiscountByString(String customer) {
            if (customer == null) return 0;
            CustomerType type = Customer_Map.get(customer.toUpperCase());
            return (type != null) ? type.getDiscount() : 0;
        }
    }

    @Autowired // injection
    private OrderRepository orderRepository;
    
    @Autowired // injection
    private ProductService productService;
    
    // BAD: Everything in controller, no service layer
    // BAD: Magic numbers, hardcoded URLs, no error handling
    // BAD: Using Map instead of DTOs
    @PostMapping("/order")
    public OrderResponse makeOrder(@RequestBody OrderRequest request) {
        
        // BAD: Long if-else that should be strategy/enum or HashMap V
        String customerType = request.getCustomerType();
        double discount = CustomerType.getDiscountByString(customerType);


        // BAD: Using raw ArrayList, should use proper collection
        List<ProductItem> items = request.getItems();

        // BAD: No duplicate detection - should use Set!
        // BAD: Nested loops, bad variable names, no Stream API
        // BAD: N+1 query problem - API call inside loop!
        Set<Long> visitedIDs = new HashSet<>();
        List<Long> productIDs = new ArrayList<>();
        for (ProductItem item : items) {
            if(!visitedIDs.add(item.getProductId())) { // duplicate
                throw new RuntimeException("Error: Duplicate product ID " + item.getProductId());
            }

            productIDs.add(item.getProductId());
        }

        Map<Long, ProductInformation> productInformationMap = productService.getProductsByIDs(productIDs);
        double total = 0;
        Order order = new Order();
        order.setCustomerType(customerType);
        order.setStatus(Order.OrderStatus.PENDING);

        for (ProductItem item: items) {
            ProductInformation productInfo = productInformationMap.get(item.getProductId());
            if ( productInfo == null ) {
                throw new RuntimeException("Error: Product " + item.getProductId() + " is missing");
            }

            if ( productInfo.getStock() <= 0 ) {
                throw new RuntimeException("Error: Product " + item.getProductId() + " out of stock");
            }

            if ( item.getQuantity() > productInfo.getStock() ) {
                throw new RuntimeException("Error: Not enough stock for product " + item.getProductId());
            }

            total += (productInfo.getPrice() * item.getQuantity());

            // add OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(productInfo.getPrice());

            order.getItems().add(orderItem);
        }


        // BAD: Magic numbers, unclear calculation
        double finalTotal = total - (total * discount);
        if ( finalTotal > full_fill_price ) {
            finalTotal -= full_fill_discount;
        }
        order.setTotal(finalTotal);

        Order saveOrder = orderRepository.save(order);
        return new OrderResponse(saveOrder.getId(), saveOrder.getStatus().toString(), saveOrder.getTotal(), "Order created:" + saveOrder.getId());
    }
    
    // BAD: Another giant method with duplicated logic
    @GetMapping("/order/{id}")
    public OrderResponse getOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order id" + id + "not found"));
        String msg = order.getStatus().getDescription();
        return new OrderResponse(
                order.getId(),
                order.getStatus().toString(),
                order.getTotal(),
                msg
        );
    }
    
    // BAD: Another method with performance issues
    @GetMapping("/analytics/popular")
    public List<PopularSold> getPopularProducts() {
        List<Order> allOrders = orderRepository.findAll();
        
        // BAD: Nested loops to count products - O(n²)
        // BAD: Should use HashMap to count!
        Map<Long, Integer> frequencyMap = new HashMap<>(); // id -> count
        for (Order order : allOrders) {
            for(OrderItem item: order.getItems()) {
                Long id = item.getProductId();
                int quantity = item.getQuantity();
                frequencyMap.put(id, frequencyMap.getOrDefault(id, 0) + quantity);
            }
        }

        // create response
//        List<Map<String, Object>> resultList = new ArrayList<>();
//        for (Map.Entry<Long, Integer> entry: frequencyMap.entrySet()) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("productId", entry.getKey());
//            map.put("totalSold", entry.getValue());
//            resultList.add(map);
//        }
        List<PopularSold> resultList = new ArrayList<>();
        for (Map.Entry<Long, Integer> entry: frequencyMap.entrySet()) {
            resultList.add(new PopularSold(entry.getKey(), entry.getValue()));
        }

        return resultList;
    }
    
    // BAD: No validation, no proper response
    @PutMapping("/order/{id}/status")
    public OrderResponse updateStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order id " + id + " not found"));
        if(request.getStatus() == null) {
            throw new RuntimeException("Status not found");
        }

        order.setStatus(request.getStatus());
        orderRepository.save(order);

        return new OrderResponse(
                order.getId(),
                order.getStatus().toString(),
                order.getTotal(),
                "Status updated"
        );
    }
}
