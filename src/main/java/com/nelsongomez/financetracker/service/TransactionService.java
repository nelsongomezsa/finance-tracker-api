package com.nelsongomez.financetracker.service;

import com.nelsongomez.financetracker.dto.CategoryBreakdown;
import com.nelsongomez.financetracker.dto.TransactionRequest;
import com.nelsongomez.financetracker.dto.TransactionResponse;
import com.nelsongomez.financetracker.dto.TransactionSummaryResponse;
import com.nelsongomez.financetracker.exception.ResourceNotFoundException;
import com.nelsongomez.financetracker.model.Category;
import com.nelsongomez.financetracker.model.Category.CategoryType;
import com.nelsongomez.financetracker.model.Transaction;
import com.nelsongomez.financetracker.repository.CategoryRepository;
import com.nelsongomez.financetracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                               CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<TransactionResponse> findAll() {
        return transactionRepository.findAll().stream()
                .sorted(Comparator.comparing(Transaction::getDate).reversed())
                .map(TransactionResponse::fromEntity)
                .toList();
    }

    public TransactionResponse findById(Long id) {
        return TransactionResponse.fromEntity(getTransactionOrThrow(id));
    }

    public TransactionResponse create(TransactionRequest request) {
        Category category = getCategoryOrThrow(request.categoryId());
        Transaction transaction = new Transaction(
                request.description(),
                request.amount(),
                request.date(),
                category
        );
        return TransactionResponse.fromEntity(transactionRepository.save(transaction));
    }

    public TransactionResponse update(Long id, TransactionRequest request) {
        Transaction transaction = getTransactionOrThrow(id);
        Category category = getCategoryOrThrow(request.categoryId());

        transaction.setDescription(request.description());
        transaction.setAmount(request.amount());
        transaction.setDate(request.date());
        transaction.setCategory(category);

        return TransactionResponse.fromEntity(transactionRepository.save(transaction));
    }

    public void delete(Long id) {
        Transaction transaction = getTransactionOrThrow(id);
        transactionRepository.delete(transaction);
    }

    public TransactionSummaryResponse getMonthlySummary(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        List<Transaction> transactions = transactionRepository.findByDateBetweenOrderByDateDesc(start, end);

        BigDecimal totalIncome = sumByType(transactions, CategoryType.INCOME);
        BigDecimal totalExpense = sumByType(transactions, CategoryType.EXPENSE);
        BigDecimal balance = totalIncome.subtract(totalExpense);

        List<CategoryBreakdown> byCategory = transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().getName(),
                        Collectors.mapping(Transaction::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ))
                .entrySet().stream()
                .map(entry -> new CategoryBreakdown(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(CategoryBreakdown::total).reversed())
                .toList();

        return new TransactionSummaryResponse(month, year, totalIncome, totalExpense, balance, byCategory);
    }

    private BigDecimal sumByType(List<Transaction> transactions, CategoryType type) {
        return transactions.stream()
                .filter(t -> t.getCategory().getType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Transaction getTransactionOrThrow(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaccion no encontrada con id " + id));
    }

    private Category getCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id " + id));
    }
}
