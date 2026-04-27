package com.meloCoding.dream_shops.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.dream_shops.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
