package com.meloCoding.services.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.models.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUserId(Long userId);
}
