package com.example.bookshop.service;

import com.example.bookshop.dto.order.CreateOrderRequestDto;
import com.example.bookshop.dto.order.OrderDto;
import com.example.bookshop.dto.order.OrderItemDto;
import com.example.bookshop.dto.order.OrderRequestDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface OrderService {
    List<OrderDto> findAllOrders(Pageable pageable);

    List<OrderDto> buy(CreateOrderRequestDto createOrderRequestDto);

    List<OrderItemDto> findAllItems(Long orderId);

    OrderItemDto findItem(Long orderId, Long id);

    OrderDto patch(OrderRequestDto orderRequestDto, Long id);
}
