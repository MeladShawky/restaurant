package com.meloCoding.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.models.Order;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Collection<Order> findByUserId(Long userId);

    
}
