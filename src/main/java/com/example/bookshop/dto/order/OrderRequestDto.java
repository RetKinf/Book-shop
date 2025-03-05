package com.example.bookshop.dto.order;

import com.example.bookshop.model.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDto(
        @NotNull
        OrderStatus status
) {
}
