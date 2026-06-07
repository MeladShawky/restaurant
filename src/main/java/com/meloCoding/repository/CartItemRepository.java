package com.meloCoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.models.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long cartId);
}
