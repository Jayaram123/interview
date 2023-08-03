package com.example.rewardsservice.service;

import com.example.rewardsservice.util.RewardsUtil;
import com.example.rewardsservice.entity.model.Customer;
import com.example.rewardsservice.entity.model.CustomerTransaction;
import com.example.rewardsservice.repository.CustomerRepository;
import com.example.rewardsservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RewardsService {

    private final RewardsUtil rewardsUtil;

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public RewardsService(RewardsUtil rewardsUtil,
                          TransactionRepository transactionRepository,
                          CustomerRepository customerRepository) {
        this.rewardsUtil = rewardsUtil;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }


    public CustomerTransaction userTransaction(CustomerTransaction customerTransaction) {
        CustomerTransaction makeCustomerTransactionWithRewards = makeCustomerTransactionWithRewards(customerTransaction);
        return transactionRepository.save(customerTransaction);

    }


    public List<CustomerTransaction> getCustomerTransactions(String customerId) {
        Optional<Customer> byId = customerRepository.findById(customerId);

        if (byId.isPresent()) {
            return byId.get().getCustomerTransactions();
        } else return Collections.EMPTY_LIST;


    }


    public int calculateTotalRewardsForMonth(String customerId, LocalDate month) {
        LocalDate startOfMonth = month.withDayOfMonth(1);
        LocalDate endOfMonth = month.withDayOfMonth(month.lengthOfMonth());

        List<CustomerTransaction> customerTransactions = transactionRepository.findByCustomerAndPurchaseDateBetween(customerId, startOfMonth, endOfMonth);

        int totalRewards = 0;

        for (CustomerTransaction transaction : customerTransactions) {
            int rewards = rewardsUtil.calculateRewards(transaction.getTransactionAmount());
            totalRewards += rewards;
        }

        return totalRewards;
    }


    public Optional<Customer> getCustomer(String customerId) {
        return customerRepository.findById(customerId);

    }

    public CustomerTransaction makeCustomerTransactionWithRewards(CustomerTransaction customerTransaction) {
        int rewardPoints = rewardsUtil.calculateRewards(customerTransaction.getTransactionAmount());
        customerTransaction.setRewardPoints(rewardPoints);
        return customerTransaction;
    }


}
