package com.example.healthyshop.repository;

import com.example.healthyshop.model.Order;
import com.example.healthyshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findOrderByUser(User user);
}
