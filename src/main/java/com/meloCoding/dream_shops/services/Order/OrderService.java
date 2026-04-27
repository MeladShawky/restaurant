package com.meloCoding.dream_shops.services.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meloCoding.dream_shops.dto.OrderDto;
import com.meloCoding.dream_shops.dto.OrderItemDto;
import com.meloCoding.dream_shops.enums.OrderStatus;
import com.meloCoding.dream_shops.exceptions.ResourceNotFoundException;
import com.meloCoding.dream_shops.models.Cart;
import com.meloCoding.dream_shops.models.Order;
import com.meloCoding.dream_shops.models.OrderItem;
import com.meloCoding.dream_shops.models.Product;
import com.meloCoding.dream_shops.services.Cart.ICartService;
import com.meloCoding.dream_shops.services.repository.OrderRepository;
import com.meloCoding.dream_shops.services.repository.productRepository;

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
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalAmount(calculateTotalAmount(orderItems));

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
