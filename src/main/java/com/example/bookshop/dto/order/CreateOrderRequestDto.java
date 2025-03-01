package com.example.bookshop.dto.order;

import jakarta.validation.constraints.NotBlank;

public record CreateOrderRequestDto(
        @NotBlank(message = "Shipping address is required")
        String shippingAddress
) {
}
