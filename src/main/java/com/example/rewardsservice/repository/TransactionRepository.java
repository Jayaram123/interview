package com.example.rewardsservice.repository;

import com.example.rewardsservice.entity.model.Customer;
import com.example.rewardsservice.entity.model.CustomerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<CustomerTransaction,Integer> {


    List<CustomerTransaction> findByCustomerIdAndTransactionDateAfter(String customerId, LocalDate date);
    List<CustomerTransaction> findByCustomerAndPurchaseDateBetween(String customerId, LocalDate startDate, LocalDate endDate);
}
