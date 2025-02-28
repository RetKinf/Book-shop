package com.example.bookshop.controller;

import com.example.bookshop.dto.order.CreateOrderRequestDto;
import com.example.bookshop.dto.order.OrderDto;
import com.example.bookshop.dto.order.OrderItemDto;
import com.example.bookshop.dto.order.OrderRequestDto;
import com.example.bookshop.model.OrderItem;
import com.example.bookshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    @Operation(summary = "Get all orders")
    public List<OrderDto> findAll(Pageable pageable) {
        return orderService.findAllOrders(pageable);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @Operation(summary = "Buy books from shipping cart")
    public List<OrderDto> buy(@Valid CreateOrderRequestDto createOrderRequestDto) {
        return orderService.buy(createOrderRequestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/api/orders/{orderId}/items")
    @Operation
    public List<OrderItemDto> findAllItem(@PathVariable Long orderId) {
        return orderService.findAllItems(orderId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/api/orders/{orderId}/items/{id}")
    @Operation
    public OrderItemDto findItem(@PathVariable Long orderId, @PathVariable Long id) {
        return orderService.findItem(orderId, id);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/api/orders/{id}")
    @Operation
    public OrderDto patch(
            @PathVariable Long id,
            @RequestBody @Valid OrderRequestDto orderRequestDto
    ) {
        return orderService.patch(orderRequestDto, id);
    }
}
