package com.dailywork.dreamshops.service.order;

import com.dailywork.dreamshops.dto.OrderDto;
import com.dailywork.dreamshops.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);


    List<OrderDto> getUserOrders(Long userId);

    OrderDto convertToDto(Order order);
}
