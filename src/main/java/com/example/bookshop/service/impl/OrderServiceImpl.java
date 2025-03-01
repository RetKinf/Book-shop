package com.example.bookshop.service.impl;

import com.example.bookshop.dto.order.CreateOrderRequestDto;
import com.example.bookshop.dto.order.OrderDto;
import com.example.bookshop.dto.order.OrderItemDto;
import com.example.bookshop.dto.order.OrderRequestDto;
import com.example.bookshop.exception.EntityNotFoundException;
import com.example.bookshop.mapper.OrderMapper;
import com.example.bookshop.model.Order;
import com.example.bookshop.model.OrderItem;
import com.example.bookshop.model.OrderStatus;
import com.example.bookshop.model.ShoppingCart;
import com.example.bookshop.model.User;
import com.example.bookshop.repository.OrderRepository;
import com.example.bookshop.repository.ShoppingCartRepository;
import com.example.bookshop.service.OrderService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> findAllOrders(Authentication authentication) {
        return orderRepository.findByUser((User) authentication.getPrincipal())
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto buy(
            CreateOrderRequestDto createOrderRequestDto,
            Authentication authentication
    ) {
        Order order = new Order();
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("ShoppingCart is empty");
        }
        order.setUser(user);
        order.convertFromCart(shoppingCart);
        order.setOrderDate(LocalDateTime.now().plusDays(3));
        order.setShippingAddress(createOrderRequestDto.shippingAddress());
        orderRepository.save(order);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderItemDto> findAllItems(
            Long orderId, Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        return orderRepository.findByUser(user).stream()
                .filter(order -> order.getId().equals(order.getId()))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Order with id " + orderId + " not found")
                )
                .getOrderItems()
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto findItemById(
            Long orderId,
            Long id,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        OrderItem orderItem = orderRepository.findByUser(user).stream()
                .filter(order -> order.getId().equals(order.getId()))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Order with id " + orderId + " not found")
                )
                .getOrderItems()
                .stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("OrderItem with id " + id + " not found")
                );
        return orderMapper.toDto(orderItem);
    }

    @Override
    public OrderDto patchById(
            OrderRequestDto orderRequestDto,
            Long id
    ) {
        Order currentOrder = orderRepository.findAll().stream()
                .filter(order -> order.getId().equals(id))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException("Order with id " + id + " not found")
                );
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(orderRequestDto.status().toUpperCase());
            currentOrder.setStatus(orderStatus);
        } catch (IllegalArgumentException e) {
            throw new EntityNotFoundException(
                    "Order status with name "
                            + orderRequestDto.status()
                            + " not found"
            );
        }
        orderRepository.save(currentOrder);
        return orderMapper.toDto(currentOrder);
    }
}
