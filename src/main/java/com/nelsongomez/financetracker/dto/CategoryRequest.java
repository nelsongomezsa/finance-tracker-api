package com.nelsongomez.financetracker.dto;

import com.nelsongomez.financetracker.model.Category.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryRequest(

        @NotBlank(message = "El nombre de la categoria es obligatorio")
        String name,

        @NotNull(message = "El tipo de categoria es obligatorio")
        CategoryType type
) {
}
