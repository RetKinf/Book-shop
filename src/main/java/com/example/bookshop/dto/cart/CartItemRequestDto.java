package com.example.bookshop.dto.cart;

import jakarta.validation.constraints.Positive;

public record CartItemRequestDto(
        @Positive
        int quantity
) {
}
