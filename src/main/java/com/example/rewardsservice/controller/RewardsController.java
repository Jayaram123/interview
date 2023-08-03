package com.example.rewardsservice.controller;

import com.example.rewardsservice.entity.model.Customer;
import com.example.rewardsservice.entity.model.CustomerTransaction;
import com.example.rewardsservice.service.RewardsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

private final RewardsService rewardsService;

    public RewardsController(RewardsService rewardsService) {
        this.rewardsService = rewardsService;
    }


    @GetMapping("/{customerId}")
    public ResponseEntity<Customer> getCustomer(@PathVariable String customerId) {
        Optional<Customer> optionalCustomer = rewardsService.getCustomer(customerId);
        return optionalCustomer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/one_month/{customerId}")
    public ResponseEntity<Integer> getRewardPointsForAMonthOr3Months(@PathVariable String customerId,
                                                                     @RequestParam @DateTimeFormat(pattern = "yyyy-MM") LocalDate month)  {
        int totalRewardsForMonth = rewardsService.calculateTotalRewardsForMonth(customerId, month);
        return ResponseEntity.ok(totalRewardsForMonth);
    }


    @PostMapping("/reward-points")
    public ResponseEntity<CustomerTransaction> rewardPoints(@RequestBody CustomerTransaction customerTransaction) {
        CustomerTransaction customerTransaction1 = rewardsService.userTransaction(customerTransaction);
        return ResponseEntity.ok(customerTransaction1);
    }
}
