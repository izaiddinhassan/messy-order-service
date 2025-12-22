    package com.example.orderservice;

    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    @Repository // database
    public interface OrderRepository extends JpaRepository<Order, Long> {
    }
