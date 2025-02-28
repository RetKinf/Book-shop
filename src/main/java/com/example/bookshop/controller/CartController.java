package com.example.bookshop.controller;

import com.example.bookshop.dto.cart.CartItemRequestDto;
import com.example.bookshop.dto.cart.CreateCartItemDto;
import com.example.bookshop.dto.cart.ShoppingCartDto;
import com.example.bookshop.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    @Operation(summary = "Get all books from the shopping cart")
    public ShoppingCartDto findAll(Pageable pageable) {
        return cartService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @Operation(summary = "Add a book to the shopping cart")
    public ShoppingCartDto save(
            @RequestBody @Valid CreateCartItemDto requestDto
    ) {
        return cartService.save(requestDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/items/{id}")
    @Operation(
            summary = "Update the quantity",
            description = "Update the quantity of a book in the cart item by its ID"
    )
    public ShoppingCartDto update(
            @RequestBody @Valid CartItemRequestDto requestDto,
            @PathVariable Long id
    ) {
        return cartService.update(requestDto, id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete a cart item by ID")
    public ShoppingCartDto delete(@PathVariable Long id) {
        return cartService.delete(id);
    }
}
