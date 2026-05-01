package com.meloCoding.services.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.models.Order;
import com.meloCoding.models.OrderItem;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Collection<Order> findByUserId(Long userId);

    
}
