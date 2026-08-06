package com.nelsongomez.financetracker;

import com.nelsongomez.financetracker.dto.CategoryRequest;
import com.nelsongomez.financetracker.dto.CategoryResponse;
import com.nelsongomez.financetracker.exception.ResourceNotFoundException;
import com.nelsongomez.financetracker.model.Category;
import com.nelsongomez.financetracker.model.Category.CategoryType;
import com.nelsongomez.financetracker.repository.CategoryRepository;
import com.nelsongomez.financetracker.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category category;

    @BeforeEach
    void setUp() throws Exception {
        category = new Category("Nomina", CategoryType.INCOME);
        setId(category, 1L);
    }

    @Test
    void findAll_devuelveTodasLasCategorias() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryResponse> result = categoryService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Nomina");
    }

    @Test
    void findById_lanzaExcepcionSiNoExiste() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoryService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void create_guardaNuevaCategoria() {
        CategoryRequest request = new CategoryRequest("Ocio", CategoryType.EXPENSE);
        when(categoryRepository.existsByNameIgnoreCase("Ocio")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category saved = invocation.getArgument(0);
            setId(saved, 2L);
            return saved;
        });

        CategoryResponse response = categoryService.create(request);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.name()).isEqualTo("Ocio");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void create_lanzaExcepcionSiElNombreYaExiste() {
        CategoryRequest request = new CategoryRequest("Nomina", CategoryType.INCOME);
        when(categoryRepository.existsByNameIgnoreCase("Nomina")).thenReturn(true);

        assertThatThrownBy(() -> categoryService.create(request))
                .isInstanceOf(IllegalArgumentException.class);

        verify(categoryRepository, never()).save(any());
    }

    private void setId(Category category, Long id) throws Exception {
        Field idField = Category.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(category, id);
    }
}
