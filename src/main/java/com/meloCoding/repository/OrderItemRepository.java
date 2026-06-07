package com.meloCoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
