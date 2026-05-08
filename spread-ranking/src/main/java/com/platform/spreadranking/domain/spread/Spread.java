package com.platform.spreadranking.domain.spread;

import com.platform.spreadranking.domain.market.OrderBook;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record Spread(BigDecimal value) {

    public static Spread from(OrderBook ob) {
        if (!ob.isValid()) {
            return new Spread(null);
        }

        BigDecimal bid = ob.bestBid();
        BigDecimal ask = ob.bestAsk();

        BigDecimal numerator = ask.subtract(bid);
        BigDecimal denominator = ask.add(bid)
                .divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP);

        BigDecimal result = numerator
                .divide(denominator, 6, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));

        return new Spread(result);
    }

    public boolean isValid() {
        return value != null;
    }

    public boolean isLow() {
        return value.compareTo(BigDecimal.valueOf(2)) <= 0;
    }
}