package com.example.rewardsservice.util;

import com.example.rewardsservice.entity.model.CustomerTransaction;
import com.example.rewardsservice.props.RewardProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class RewardsUtil {

    @Autowired
    private RewardProperties rewardProperties;


    public int calculateRewards(BigDecimal purchaseAmount) {
        BigDecimal baseThreshold = rewardProperties.getBaseThreshold();
        BigDecimal bonusThreshold = rewardProperties.getBonusThreshold();
        BigDecimal baseRate = rewardProperties.getBaseRate();
        BigDecimal bonusRate = rewardProperties.getBonusRate();

        BigDecimal totalRewards = BigDecimal.ZERO;

        if (purchaseAmount.compareTo(bonusThreshold) > 0) {
            BigDecimal bonusPoints = purchaseAmount.subtract(bonusThreshold).multiply(bonusRate);
            totalRewards = totalRewards.add(bonusPoints);
            purchaseAmount = bonusThreshold;
        }

        if (purchaseAmount.compareTo(baseThreshold) > 0) {
            BigDecimal basePoints = purchaseAmount.subtract(baseThreshold).multiply(baseRate);
            totalRewards = totalRewards.add(basePoints);
        }

        return totalRewards.intValue();
    }


    public Map<String, Map<Integer, Integer>> calculateRewardPointsForACustomer(List<CustomerTransaction> transactions) {
        Map<String, Map<Integer, Integer>> rewardData = new HashMap<>();

        for (CustomerTransaction transaction : transactions) {
            String customerId = transaction.getCustomerId();
            int month = transaction.getTransactionDate().getMonthValue();
            int rewardPoints = calculateRewards(transaction.getTransactionAmount());
            rewardData.computeIfAbsent(customerId, k -> new HashMap<>())
                    .merge(month, rewardPoints, Integer::sum);
        }

        return rewardData;
    }


    public Map<Integer, Integer> calculateTotalRewardPointsPast3Months(List<CustomerTransaction> transactions) {
        Map<Integer, Integer> rewardData = new HashMap<>();

        LocalDate threeMonthsAgo = LocalDate.now().minusMonths(3);

        for (CustomerTransaction transaction : transactions) {
            int month = transaction.getTransactionDate().getMonthValue();
            BigDecimal amount = transaction.getTransactionAmount();

            int rewardPoints = calculateRewards(amount);
            rewardData.merge(month, rewardPoints, Integer::sum);
        }

        return rewardData;
    }


}
