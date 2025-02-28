package com.example.bookshop.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateCategoryRequestDto {
    @NotBlank
    @Size(min = 2, max = 50, message = "Description must be between 2 and 255 characters")
    private String name;
    @Size(max = 255)
    private String description;
}
