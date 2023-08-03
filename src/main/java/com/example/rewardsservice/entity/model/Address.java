package com.example.rewardsservice.entity.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Entity
@Table
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String zip;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
