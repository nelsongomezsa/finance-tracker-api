package com.nelsongomez.financetracker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nelsongomez.financetracker.controller.TransactionController;
import com.nelsongomez.financetracker.dto.CategoryResponse;
import com.nelsongomez.financetracker.dto.TransactionRequest;
import com.nelsongomez.financetracker.dto.TransactionResponse;
import com.nelsongomez.financetracker.model.Category.CategoryType;
import com.nelsongomez.financetracker.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void create_devuelve201YLaTransaccionCreada() throws Exception {
        TransactionRequest request = new TransactionRequest(
                "Supermercado", new BigDecimal("45.90"), LocalDate.of(2026, 8, 5), 3L
        );

        CategoryResponse category = new CategoryResponse(3L, "Comida", CategoryType.EXPENSE);
        TransactionResponse response = new TransactionResponse(
                10L, "Supermercado", new BigDecimal("45.90"), LocalDate.of(2026, 8, 5), category
        );

        when(transactionService.create(any(TransactionRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.description").value("Supermercado"))
                .andExpect(jsonPath("$.category.name").value("Comida"));
    }

    @Test
    void create_devuelve400SiFaltaDescripcion() throws Exception {
        TransactionRequest request = new TransactionRequest(
                "", new BigDecimal("45.90"), LocalDate.of(2026, 8, 5), 3L
        );

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
