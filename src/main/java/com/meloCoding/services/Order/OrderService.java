package com.meloCoding.services.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meloCoding.dto.OrderDto;
import com.meloCoding.dto.OrderItemDto;
import com.meloCoding.enums.OrderStatus;
import com.meloCoding.exceptions.ResourceNotFoundException;
import com.meloCoding.models.Cart;
import com.meloCoding.models.Order;
import com.meloCoding.models.OrderItem;
import com.meloCoding.models.Product;
import com.meloCoding.services.Cart.ICartService;
import com.meloCoding.services.repository.OrderRepository;
import com.meloCoding.services.repository.productRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;
    private final productRepository productRepository;

    @Transactional
    @Override
    public Order placeOrder(Long userId, String promoCode) {
        Cart cart = cartService.getCartByUserId(userId);

        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));

        BigDecimal baseTotal = calculateTotalAmount(orderItems);
        BigDecimal discount = BigDecimal.ZERO;

        if (promoCode != null && !promoCode.trim().isEmpty()) {
            String code = promoCode.trim().toUpperCase();
            if (code.equals("SALAD20")) {
                BigDecimal saladTotal = BigDecimal.ZERO;
                for (OrderItem item : orderItems) {
                    if (item.getProduct().getName().toLowerCase().contains("salad")) {
                        saladTotal = saladTotal.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                    }
                }
                discount = saladTotal.multiply(BigDecimal.valueOf(0.20));
            } else if (code.equals("NOODLES")) {
                boolean hasNoodles = orderItems.stream()
                        .anyMatch(item -> item.getProduct().getName().toLowerCase().contains("noodle"));
                if (hasNoodles) {
                    discount = BigDecimal.valueOf(5.00);
                }
            }
        }

        BigDecimal finalTotal = baseTotal.subtract(discount);
        if (finalTotal.compareTo(BigDecimal.ZERO) < 0) {
            finalTotal = BigDecimal.ZERO;
        }

        order.setTotalAmount(finalTotal);

        // Decrease inventory for each ordered product
        for (OrderItem item : orderItems) {
            Product product = item.getProduct();
            int newInventory = product.getInventory() - item.getQuantity();
            if (newInventory < 0) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }
            product.setInventory(newInventory);
            productRepository.save(product);
        }

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems()
                .stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem(
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice(),
                            order,
                            cartItem.getProduct());
                    return orderItem;
                }).toList();
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public OrderDto convertToDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    @Override
    public OrderItemDto convertItemToDto(OrderItem item) {
        return modelMapper.map(item, OrderItemDto.class);
    }
}
