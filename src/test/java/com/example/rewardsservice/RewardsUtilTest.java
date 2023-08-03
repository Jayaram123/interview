package com.example.rewardsservice;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.example.rewardsservice.entity.model.CustomerTransaction;
import com.example.rewardsservice.props.RewardProperties;
import com.example.rewardsservice.util.RewardsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

class RewardsUtilTest {

    @Mock
    private RewardProperties rewardProperties;

    private RewardsUtil rewardsUtil;

    @BeforeEach
    void setUp() {
        rewardsUtil = new RewardsUtil();
        // Initialize your rewardProperties mock or set properties if necessary
        // rewardUtil.setRewardProperties(rewardProperties);
    }

    @Test
    void testCalculateRewards_NoRewards() {
        BigDecimal purchaseAmount = new BigDecimal("40");

        int result = rewardsUtil.calculateRewards(purchaseAmount);

        assertEquals(0, result);
    }

    @Test
    void testCalculateRewards_BaseRewards() {
        BigDecimal purchaseAmount = new BigDecimal("80");

        int result = rewardsUtil.calculateRewards(purchaseAmount);

        assertEquals(30, result);
    }

    @Test
    void testCalculateRewards_BonusRewards() {
        BigDecimal purchaseAmount = new BigDecimal("150");

        int result = rewardsUtil.calculateRewards(purchaseAmount);

        assertEquals(120, result);
    }

    @Test
    void testCalculateRewardPointsForACustomer() {
        CustomerTransaction transaction1 = new CustomerTransaction("C1", new BigDecimal("80"));
        CustomerTransaction transaction2 = new CustomerTransaction("C1", new BigDecimal("120"));
        CustomerTransaction transaction3 = new CustomerTransaction("C2", new BigDecimal("200"));

        Map<String, Map<Integer, Integer>> result = rewardsUtil.calculateRewardPointsForACustomer(Arrays.asList(transaction1, transaction2, transaction3));

        assertEquals(2, result.size());
        assertEquals(30, result.get("C1").get(1));
        assertEquals(120, result.get("C1").get(2));
        assertEquals(120, result.get("C2").get(1));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void testCalculateTotalRewardPointsPast3Months(int month) {
        CustomerTransaction transaction1 = new CustomerTransaction("C1", BigDecimal.TEN, LocalDate.now().minusMonths(3));
        CustomerTransaction transaction2 = new CustomerTransaction("C1", BigDecimal.TEN, LocalDate.now().minusMonths(2));
        CustomerTransaction transaction3 = new CustomerTransaction("C1", BigDecimal.TEN, LocalDate.now().minusMonths(1));

        Map<Integer, Integer> result = rewardsUtil.calculateTotalRewardPointsPast3Months(Arrays.asList(transaction1, transaction2, transaction3));

        if (month < 3) {
            assertEquals(30, result.get(month + 1));
        } else {
            assertEquals(0, result.get(month + 1));
        }
    }

    @ParameterizedTest
    @MethodSource("provideTestDataForRewardsCalculation")
    void testCalculateRewards_Parametrized(BigDecimal purchaseAmount, int expectedRewards) {
        int result = rewardsUtil.calculateRewards(purchaseAmount);

        assertEquals(expectedRewards, result);
    }

    private static Collection<Arguments> provideTestDataForRewardsCalculation() {
        return Arrays.asList(
                Arguments.of(new BigDecimal("40"), 0),
                Arguments.of(new BigDecimal("80"), 30),
                Arguments.of(new BigDecimal("150"), 120)
        );
    }
}
