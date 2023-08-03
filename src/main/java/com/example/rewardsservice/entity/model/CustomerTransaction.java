package com.example.rewardsservice.entity.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Table
@Entity
public class CustomerTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String customerId;
    private String transactionId;
    private LocalDate transactionDate;
    private BigDecimal transactionAmount;

    private Integer rewardPoints;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public CustomerTransaction(String customerId, BigDecimal transactionAmount, LocalDate transactionDate) {
        this.customerId = customerId;
        this.transactionAmount = transactionAmount;
        this.transactionDate = transactionDate;
    }

    public CustomerTransaction(String customerId, BigDecimal transactionAmount) {
        this.customerId = customerId;
        this.transactionAmount = transactionAmount;
    }
}
