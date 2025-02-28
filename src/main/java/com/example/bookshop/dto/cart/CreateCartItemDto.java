package com.example.bookshop.dto.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemDto(
        @NotNull
        Long bookId,
        @NotNull
        @Positive
        int quantity
) {
}
