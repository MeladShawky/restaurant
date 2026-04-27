package com.meloCoding.dream_shops.services.Cart;

import java.math.BigDecimal;

import com.meloCoding.dream_shops.models.Cart;
import com.meloCoding.dream_shops.models.User;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart initializeCart(User user);

    Cart getCartByUserId(Long userId);
}