package com.example.bookshop.service;

import com.example.bookshop.dto.order.CreateOrderRequestDto;
import com.example.bookshop.dto.order.OrderDto;
import com.example.bookshop.dto.order.OrderItemDto;
import com.example.bookshop.dto.order.OrderRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    List<OrderDto> findAllOrders(
            Authentication authentication,
            Pageable pageable
    );

    OrderDto createOrder(
            CreateOrderRequestDto createOrderRequestDto,
            Authentication authentication
    );

    List<OrderItemDto> findAllItems(
            Long orderId,
            Authentication authentication
    );

    OrderItemDto findItemById(
            Long orderId,
            Long id,
            Authentication authentication
    );

    OrderDto patchOrderStatusById(
            OrderRequestDto orderRequestDto,
            Long id
    );
}
