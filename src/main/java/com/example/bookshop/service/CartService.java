package com.example.bookshop.service;

import com.example.bookshop.dto.cart.CartItemRequestDto;
import com.example.bookshop.dto.cart.CartItemResponseDto;
import com.example.bookshop.dto.cart.CreateCartItemDto;
import com.example.bookshop.dto.cart.ShoppingCartDto;
import com.example.bookshop.model.User;
import org.springframework.security.core.Authentication;

public interface CartService {
    ShoppingCartDto findAll(Authentication authentication);

    ShoppingCartDto save(CreateCartItemDto requestDto, Authentication authentication);

    CartItemResponseDto update(
            CartItemRequestDto requestDto,
            Long id,
            Authentication authentication
    );

    void delete(Long id, Authentication authentication);

    void createShoppingCart(User user);
}
