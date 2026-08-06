package com.nelsongomez.financetracker.service;

import com.nelsongomez.financetracker.dto.CategoryRequest;
import com.nelsongomez.financetracker.dto.CategoryResponse;
import com.nelsongomez.financetracker.exception.ResourceNotFoundException;
import com.nelsongomez.financetracker.model.Category;
import com.nelsongomez.financetracker.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryResponse::fromEntity)
                .toList();
    }

    public CategoryResponse findById(Long id) {
        return CategoryResponse.fromEntity(getCategoryOrThrow(id));
    }

    public CategoryResponse create(CategoryRequest request) {
        if (categoryRepository.existsByNameIgnoreCase(request.name())) {
            throw new IllegalArgumentException("Ya existe una categoria con ese nombre");
        }
        Category category = new Category(request.name(), request.type());
        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = getCategoryOrThrow(id);
        category.setName(request.name());
        category.setType(request.type());
        return CategoryResponse.fromEntity(categoryRepository.save(category));
    }

    public void delete(Long id) {
        Category category = getCategoryOrThrow(id);
        categoryRepository.delete(category);
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id " + id));
    }
}
