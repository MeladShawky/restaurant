package com.meloCoding.dream_shops.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.dream_shops.models.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long cartId);
}
