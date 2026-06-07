package com.meloCoding.services.Order;

import java.util.List;

import com.meloCoding.dto.OrderDto;
import com.meloCoding.dto.OrderItemDto;
import com.meloCoding.models.Order;
import com.meloCoding.models.OrderItem;

public interface IOrderService {
    Order placeOrder(Long userId, String promoCode);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrders(Long userId);
    OrderDto convertToDto(Order order);
    OrderItemDto convertItemToDto(OrderItem item);
}
