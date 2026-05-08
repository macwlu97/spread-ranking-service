package com.platform.spreadranking.domain.spread;

import java.math.BigDecimal;

public record Spread(BigDecimal value) {

    private static final BigDecimal THRESHOLD = BigDecimal.valueOf(2);

    public static Spread of(BigDecimal value) {
        return new Spread(value);
    }

    public boolean isLow() {
        return value.compareTo(THRESHOLD) <= 0;
    }

    public boolean isHigh() {
        return value.compareTo(THRESHOLD) > 0;
    }
}