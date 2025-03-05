package com.example.bookshop.service.impl;

import com.example.bookshop.dto.order.CreateOrderRequestDto;
import com.example.bookshop.dto.order.OrderDto;
import com.example.bookshop.dto.order.OrderItemDto;
import com.example.bookshop.dto.order.OrderRequestDto;
import com.example.bookshop.exception.EntityNotFoundException;
import com.example.bookshop.exception.OrderProcessingException;
import com.example.bookshop.mapper.OrderMapper;
import com.example.bookshop.model.CartItem;
import com.example.bookshop.model.Order;
import com.example.bookshop.model.OrderItem;
import com.example.bookshop.model.ShoppingCart;
import com.example.bookshop.model.User;
import com.example.bookshop.repository.OrderRepository;
import com.example.bookshop.repository.ShoppingCartRepository;
import com.example.bookshop.service.OrderService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public List<OrderDto> findAllOrders(Authentication authentication, Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderRepository.findAllByUser(user, pageable)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderDto createOrder(
            CreateOrderRequestDto createOrderRequestDto,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user);
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException(
                    "Shopping cart with id "
                            + shoppingCart.getId()
                            + " is empty");
        }
        Order order = convertCartToOrder(shoppingCart);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
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
        Order order = orderRepository.findByIdAndUser(user.getId(), orderId).orElseThrow(
                () -> new EntityNotFoundException("Order with id " + orderId + " not found")
        );
        return orderMapper.toOrderItemDtoList(order.getOrderItems());
    }

    @Override
    public OrderItemDto findItemById(
            Long orderId,
            Long id,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        OrderItem orderItem = orderRepository.findItemByUserAndId(user, id, orderId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Order item with id " + id + " not found")
                );
        return orderMapper.toDto(orderItem);
    }

    @Override
    public OrderDto patchOrderStatusById(
            OrderRequestDto orderRequestDto,
            Long id
    ) {
        Order order = orderRepository.findById(id).orElseThrow(
                        () -> new EntityNotFoundException("Order with id " + id + " not found")
                );
        order.setStatus(orderRequestDto.status());
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    private Order convertCartToOrder(ShoppingCart shoppingCart) {
        Order order = new Order();
        order.setOrderItems(shoppingCart.getCartItems().stream()
                .map(item -> convertItemCartToOrderCart(item, order))
                .collect(Collectors.toSet()));
        order.setTotal(order.getOrderItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        return order;
    }

    private OrderItem convertItemCartToOrderCart(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setBook(cartItem.getBook());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(orderItem.getBook().getPrice());
        orderItem.setOrder(order);
        return orderItem;
    }
}
