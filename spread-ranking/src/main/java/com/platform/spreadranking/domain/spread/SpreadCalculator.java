package com.platform.spreadranking.domain.spread;

import com.platform.spreadranking.domain.market.OrderBook;
import com.platform.spreadranking.domain.market.PriceLevel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Component
public class SpreadCalculator {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    private static final BigDecimal TWO = BigDecimal.valueOf(2);

    private static final int INTERNAL_SCALE = 10;
    private static final int FINAL_SCALE = 2;

    public Optional<Spread> calculate(OrderBook orderBook) {

        if (!isValid(orderBook)) {
            return Optional.empty();
        }

        Optional<BigDecimal> bidOpt = findBestPrice(orderBook.bids());
        Optional<BigDecimal> askOpt = findBestPrice(orderBook.asks());

        if (bidOpt.isEmpty() || askOpt.isEmpty()) {
            return Optional.empty();
        }

        BigDecimal bid = bidOpt.get();
        BigDecimal ask = askOpt.get();

        if (!areValidPrices(bid, ask)) {
            return Optional.empty();
        }

        // KANGA RULE: invalid market state
        if (ask.compareTo(bid) < 0) {
            return Optional.empty();
        }

        // flat market
        if (ask.compareTo(bid) == 0) {
            return Optional.of(new Spread(zero()));
        }

        return Optional.of(new Spread(computeSpread(bid, ask)));
    }


    // SRP: VALIDATION
    private boolean isValid(OrderBook orderBook) {
        return orderBook != null;
    }

    private boolean areValidPrices(BigDecimal bid, BigDecimal ask) {
        return bid != null
                && ask != null
                && bid.signum() > 0
                && ask.signum() > 0;
    }


    // SRP: PRICE SELECTION
    private Optional<BigDecimal> findBestPrice(List<PriceLevel> levels) {
        if (levels == null) return Optional.empty();

        return levels.stream()
                .filter(l -> l != null && l.price() != null)
                .map(PriceLevel::price)
                .filter(p -> p.signum() > 0)
                .findFirst();
    }

    // SRP: CORE MATH
    private BigDecimal computeSpread(BigDecimal bid, BigDecimal ask) {

        if (bid == null || ask == null) {
            return BigDecimal.ZERO.setScale(FINAL_SCALE, RoundingMode.HALF_UP);
        }

        BigDecimal numerator = ask.subtract(bid);

        BigDecimal denominator = ask.add(bid)
                .divide(TWO, INTERNAL_SCALE, RoundingMode.HALF_UP);

        if (denominator.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO.setScale(FINAL_SCALE, RoundingMode.HALF_UP);
        }

        BigDecimal result = numerator
                .divide(denominator, INTERNAL_SCALE, RoundingMode.HALF_UP)
                .multiply(HUNDRED);

        return result.setScale(FINAL_SCALE, RoundingMode.HALF_UP);
    }

    private BigDecimal zero() {
        return BigDecimal.ZERO.setScale(FINAL_SCALE, RoundingMode.HALF_UP);
    }
}