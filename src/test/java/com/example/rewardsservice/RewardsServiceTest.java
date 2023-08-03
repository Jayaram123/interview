package com.example.rewardsservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.example.rewardsservice.entity.model.Customer;
import com.example.rewardsservice.entity.model.CustomerTransaction;
import com.example.rewardsservice.repository.CustomerRepository;
import com.example.rewardsservice.repository.TransactionRepository;
import com.example.rewardsservice.service.RewardsService;
import com.example.rewardsservice.util.RewardsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RewardsServiceTest {

    @Mock
    private RewardsUtil rewardsUtil;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    private RewardsService rewardsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        rewardsService = new RewardsService(rewardsUtil, transactionRepository, customerRepository);
    }

    @Test
    void testUserTransaction() {
        CustomerTransaction inputTransaction = new CustomerTransaction();
        inputTransaction.setTransactionAmount(new BigDecimal("120"));

        when(rewardsUtil.calculateRewards(any(BigDecimal.class))).thenReturn(90);
        when(transactionRepository.save(any(CustomerTransaction.class))).thenReturn(inputTransaction);

        CustomerTransaction result = rewardsService.userTransaction(inputTransaction);

        verify(transactionRepository).save(eq(inputTransaction));
        verify(rewardsUtil).calculateRewards(eq(inputTransaction.getTransactionAmount()));

        assertEquals(90, result.getRewardPoints());
    }

    @Test
    void testGetCustomerTransactions_CustomerExists() {
        Customer customer = new Customer();
        customer.setCustomerTransactions(Collections.singletonList(new CustomerTransaction()));

        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));

        List<CustomerTransaction> result = rewardsService.getCustomerTransactions("123");

        verify(customerRepository).findById(eq("123"));

        assertEquals(1, result.size());
    }

    @Test
    void testGetCustomerTransactions_CustomerDoesNotExist() {
        when(customerRepository.findById(anyString())).thenReturn(Optional.empty());

        List<CustomerTransaction> result = rewardsService.getCustomerTransactions("123");

        verify(customerRepository).findById(eq("123"));

        assertTrue(result.isEmpty());
    }

    @Test
    void testCalculateTotalRewardsForMonth() {
        LocalDate month = LocalDate.of(2023, 8, 1);
        CustomerTransaction transaction1 = new CustomerTransaction();
        transaction1.setTransactionAmount(new BigDecimal("120"));
        CustomerTransaction transaction2 = new CustomerTransaction();
        transaction2.setTransactionAmount(new BigDecimal("80"));

        when(transactionRepository.findByCustomerAndPurchaseDateBetween(anyString(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(transaction1, transaction2));
        when(rewardsUtil.calculateRewards(any(BigDecimal.class))).thenReturn(90, 30);

        int result = rewardsService.calculateTotalRewardsForMonth("123", month);

        verify(transactionRepository).findByCustomerAndPurchaseDateBetween(eq("123"), any(LocalDate.class), any(LocalDate.class));
        verify(rewardsUtil, times(2)).calculateRewards(any(BigDecimal.class));

        assertEquals(120, result);
    }

    @Test
    void testGetCustomer() {
        Customer customer = new Customer();
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(customer));

        Optional<Customer> result = rewardsService.getCustomer("123");

        verify(customerRepository).findById(eq("123"));

        assertTrue(result.isPresent());
    }

    @Test
    void testMakeCustomerTransactionWithRewards() {
        CustomerTransaction inputTransaction = new CustomerTransaction();
        inputTransaction.setTransactionAmount(new BigDecimal("120"));

        when(rewardsUtil.calculateRewards(any(BigDecimal.class))).thenReturn(90);

        CustomerTransaction result = rewardsService.makeCustomerTransactionWithRewards(inputTransaction);

        verify(rewardsUtil).calculateRewards(eq(inputTransaction.getTransactionAmount()));

        assertEquals(90, result.getRewardPoints());
    }
}
