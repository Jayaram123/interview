package com.example.rewardsservice;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import com.example.rewardsservice.controller.RewardsController;
import com.example.rewardsservice.entity.model.Customer;
import com.example.rewardsservice.entity.model.CustomerTransaction;
import com.example.rewardsservice.service.RewardsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class RewardsControllerTest {

    @Mock
    private RewardsService rewardsService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new RewardsController(rewardsService)).build();
    }

    @Test
    void testGetCustomer_CustomerExists() throws Exception {
        Customer customer = new Customer();
        when(rewardsService.getCustomer(anyString())).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/api/rewards/{customerId}", "123"))
                .andExpect(status().isOk());

        verify(rewardsService).getCustomer(eq("123"));
    }

    @Test
    void testGetCustomer_CustomerDoesNotExist() throws Exception {
        when(rewardsService.getCustomer(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/rewards/{customerId}", "123"))
                .andExpect(status().isBadRequest());

        verify(rewardsService).getCustomer(eq("123"));
    }

    @Test
    void testGetRewardPointsForAMonthOr3Months() throws Exception {
        when(rewardsService.calculateTotalRewardsForMonth(anyString(), any(LocalDate.class))).thenReturn(150);

        mockMvc.perform(get("/api/rewards/one_month/{customerId}?month=2023-08", "123"))
                .andExpect(status().isOk())
                .andExpect(content().string("150"));

        verify(rewardsService).calculateTotalRewardsForMonth(eq("123"), any(LocalDate.class));
    }

    @Test
    void testRewardPoints() throws Exception {
        CustomerTransaction inputTransaction = new CustomerTransaction();
        inputTransaction.setTransactionAmount(new BigDecimal("120"));

        when(rewardsService.userTransaction(any(CustomerTransaction.class))).thenReturn(inputTransaction);

        mockMvc.perform(post("/api/rewards/reward-points")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"transactionAmount\": 120}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionAmount").value("120"));

        verify(rewardsService).userTransaction(any(CustomerTransaction.class));
    }
}
