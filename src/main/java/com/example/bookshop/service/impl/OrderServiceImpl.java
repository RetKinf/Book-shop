package com.example.bookshop.service.impl;

import com.example.bookshop.dto.order.CreateOrderRequestDto;
import com.example.bookshop.dto.order.OrderDto;
import com.example.bookshop.dto.order.OrderItemDto;
import com.example.bookshop.dto.order.OrderRequestDto;
import com.example.bookshop.exception.EntityNotFoundException;
import com.example.bookshop.mapper.OrderMapper;
import com.example.bookshop.model.CartItem;
import com.example.bookshop.model.Order;
import com.example.bookshop.model.OrderStatus;
import com.example.bookshop.model.Role;
import com.example.bookshop.model.RoleName;
import com.example.bookshop.model.ShoppingCart;
import com.example.bookshop.model.User;
import com.example.bookshop.repository.OrderRepository;
import com.example.bookshop.repository.ShoppingCartRepository;
import com.example.bookshop.service.OrderService;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl  implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderDto> findAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).stream()
                .map(order -> orderMapper.toDto(order))
                .toList();
    }

    @Override
    public List<OrderDto> buy(CreateOrderRequestDto createOrderRequestDto) {
        Order order = new Order();
        User user = getCurrentUser();
        order.setUser(user);
        order.setOrderStatus(OrderStatus.PENDING);
        int sum
        order.setTotalPrice();

        return List.of();
    }

    @Override
    public List<OrderItemDto> findAllItems(Long orderId) {
        return List.of();
    }

    @Override
    public OrderItemDto findItem(Long orderId, Long id) {
        return null;
    }

    @Override
    public OrderDto patch(OrderRequestDto orderRequestDto, Long id) {
        return null;
    }

    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    private BigDecimal getTotalPrice(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> cartItem.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
