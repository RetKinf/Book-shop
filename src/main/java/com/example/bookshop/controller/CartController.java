package com.example.bookshop.controller;

import com.example.bookshop.dto.cart.CartItemRequestDto;
import com.example.bookshop.dto.cart.CartItemResponseDto;
import com.example.bookshop.dto.cart.CreateCartItemDto;
import com.example.bookshop.dto.cart.ShoppingCartDto;
import com.example.bookshop.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    @Operation(summary = "Get all books from the shopping cart")
    public ShoppingCartDto findAll(Authentication authentication) {
        return cartService.findAll(authentication);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    @Operation(summary = "Add a book to the shopping cart")
    public ShoppingCartDto save(
            @RequestBody @Valid CreateCartItemDto requestDto,
            Authentication authentication
    ) {
        return cartService.save(requestDto, authentication);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/items/{id}")
    @Operation(
            summary = "Update the quantity",
            description = "Update the quantity of a book in the cart item by its ID"
    )
    public CartItemResponseDto update(
            @RequestBody @Valid CartItemRequestDto requestDto,
            @PathVariable Long id,
            Authentication authentication
    ) {
        return cartService.update(requestDto, id, authentication);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/items/{id}")
    @Operation(summary = "Delete a cart item by ID")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication authentication) {
        cartService.delete(id, authentication);
    }
}
