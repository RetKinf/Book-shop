package com.example.bookshop.service;

import com.example.bookshop.dto.cart.CartItemRequestDto;
import com.example.bookshop.dto.cart.CreateCartItemDto;
import com.example.bookshop.dto.cart.ShoppingCartDto;
import org.springframework.data.domain.Pageable;

public interface CartService {
    ShoppingCartDto findAll(Pageable pageable);

    ShoppingCartDto save(CreateCartItemDto requestDto);

    ShoppingCartDto update(CartItemRequestDto requestDto, Long id);

    ShoppingCartDto delete(Long id);
}
