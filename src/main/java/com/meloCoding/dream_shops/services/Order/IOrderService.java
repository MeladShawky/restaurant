package com.meloCoding.dream_shops.services.Order;

import java.util.List;

import com.meloCoding.dream_shops.dto.OrderDto;
import com.meloCoding.dream_shops.dto.OrderItemDto;
import com.meloCoding.dream_shops.models.Order;
import com.meloCoding.dream_shops.models.OrderItem;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
    OrderDto convertToDto(Order order);
    OrderItemDto convertItemToDto(OrderItem item);
}
