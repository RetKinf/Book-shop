package com.example.bookshop.dto.cart;

import java.util.List;

public record ShoppingCartDto(
        Long id,
        Long userId,
        List<CartItemResponseDto> cartItems
) {
}
