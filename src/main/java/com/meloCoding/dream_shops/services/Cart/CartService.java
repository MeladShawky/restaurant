package com.meloCoding.dream_shops.services.Cart;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meloCoding.dream_shops.exceptions.ResourceNotFoundException;
import com.meloCoding.dream_shops.models.Cart;
import com.meloCoding.dream_shops.models.User;
import com.meloCoding.dream_shops.services.repository.CartItemRepository;
import com.meloCoding.dream_shops.services.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    
    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }
@Transactional
public Cart initializeCart(User user) {
    return Optional.ofNullable(getCartByUserId(user.getId()))
    .orElseGet(()->{
        Cart cart=new Cart();
        cart.setUser(user);
        cart.setTotalAmount(BigDecimal.ZERO);
        return cartRepository.save(cart);
    });
}

@Override
public Cart getCartByUserId(Long userId) {
   return cartRepository.findByUserId(userId);
}

}
