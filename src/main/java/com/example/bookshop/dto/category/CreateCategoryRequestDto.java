package com.example.bookshop.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateCategoryRequestDto {
    @NotBlank
    private String name;
    private String description;
}
