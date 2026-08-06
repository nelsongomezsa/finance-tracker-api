package com.nelsongomez.financetracker.dto;

import com.nelsongomez.financetracker.model.Category;
import com.nelsongomez.financetracker.model.Category.CategoryType;

public record CategoryResponse(
        Long id,
        String name,
        CategoryType type
) {

    public static CategoryResponse fromEntity(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getType());
    }
}
