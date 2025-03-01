package com.example.bookshop.controller;

import com.example.bookshop.dto.order.CreateOrderRequestDto;
import com.example.bookshop.dto.order.OrderDto;
import com.example.bookshop.dto.order.OrderItemDto;
import com.example.bookshop.dto.order.OrderRequestDto;
import com.example.bookshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    @Operation(summary = "Get all orders for the current user")
    public List<OrderDto> findAll(Authentication authentication) {
        return orderService.findAllOrders(authentication);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @Operation(summary = "Create a new order from the shopping cart")
    public OrderDto buy(
            @RequestBody @Valid CreateOrderRequestDto createOrderRequestDto,
            Authentication authentication
    ) {
        return orderService.buy(createOrderRequestDto, authentication);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get all items for a specific order")
    public List<OrderItemDto> findAllItem(
            @PathVariable Long orderId,
            Authentication authentication
    ) {
        return orderService.findAllItems(orderId, authentication);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/{orderId}/items/{id}")
    @Operation(summary = "Get a specific item from a specific order")
    public OrderItemDto findItemById(
            @PathVariable Long orderId,
            @PathVariable Long id,
            Authentication authentication
    ) {
        return orderService.findItemById(orderId, id, authentication);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update the status of an order by its ID")
    public OrderDto patchById(
            @PathVariable Long id,
            @RequestBody @Valid OrderRequestDto orderRequestDto
    ) {
        return orderService.patchById(orderRequestDto, id);
    }
}
