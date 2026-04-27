package com.meloCoding.dream_shops.services.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.dream_shops.models.Order;
import com.meloCoding.dream_shops.models.OrderItem;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Collection<Order> findByUserId(Long userId);

    
}
