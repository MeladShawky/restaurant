package com.meloCoding.services.Cart;

import java.math.BigDecimal;

import com.meloCoding.models.Cart;
import com.meloCoding.models.User;

public interface ICartService {
    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart initializeCart(User user);

    Cart getCartByUserId(Long userId);
}
