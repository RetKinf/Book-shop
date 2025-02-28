package com.example.bookshop.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CartItemRequestDto(
        @NotNull
        @Positive
        int quantity
) {
}
