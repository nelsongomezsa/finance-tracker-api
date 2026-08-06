package com.nelsongomez.financetracker;

import com.nelsongomez.financetracker.dto.TransactionSummaryResponse;
import com.nelsongomez.financetracker.model.Category;
import com.nelsongomez.financetracker.model.Category.CategoryType;
import com.nelsongomez.financetracker.model.Transaction;
import com.nelsongomez.financetracker.repository.CategoryRepository;
import com.nelsongomez.financetracker.repository.TransactionRepository;
import com.nelsongomez.financetracker.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void getMonthlySummary_calculaBalanceYDesglosePorCategoria() throws Exception {
        Category nomina = new Category("Nomina", CategoryType.INCOME);
        setId(nomina, 1L);
        Category alquiler = new Category("Alquiler", CategoryType.EXPENSE);
        setId(alquiler, 2L);
        Category comida = new Category("Comida", CategoryType.EXPENSE);
        setId(comida, 3L);

        Transaction t1 = new Transaction("Nomina agosto", new BigDecimal("1800.00"), LocalDate.of(2026, 8, 1), nomina);
        Transaction t2 = new Transaction("Alquiler piso", new BigDecimal("650.00"), LocalDate.of(2026, 8, 2), alquiler);
        Transaction t3 = new Transaction("Supermercado", new BigDecimal("120.50"), LocalDate.of(2026, 8, 5), comida);
        Transaction t4 = new Transaction("Supermercado 2", new BigDecimal("40.00"), LocalDate.of(2026, 8, 20), comida);

        when(transactionRepository.findByDateBetweenOrderByDateDesc(
                LocalDate.of(2026, 8, 1), LocalDate.of(2026, 8, 31)))
                .thenReturn(List.of(t1, t2, t3, t4));

        TransactionSummaryResponse summary = transactionService.getMonthlySummary(2026, 8);

        assertThat(summary.totalIncome()).isEqualByComparingTo("1800.00");
        assertThat(summary.totalExpense()).isEqualByComparingTo("810.50");
        assertThat(summary.balance()).isEqualByComparingTo("989.50");
        assertThat(summary.byCategory()).hasSize(3);
        assertThat(summary.byCategory().get(0).categoryName()).isEqualTo("Nomina");
    }

    private void setId(Category category, Long id) throws Exception {
        Field idField = Category.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(category, id);
    }
}
