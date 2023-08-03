package com.example.rewardsservice.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@ConfigurationProperties(prefix = "reward")
@Data
public class RewardProperties {

    private BigDecimal baseThreshold;
    private BigDecimal bonusThreshold;
    private BigDecimal baseRate;
    private BigDecimal bonusRate;
}
