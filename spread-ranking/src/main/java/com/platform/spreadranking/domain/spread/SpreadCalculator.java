package com.platform.spreadranking.domain.spread;

import com.platform.spreadranking.domain.market.OrderBook;
import com.platform.spreadranking.domain.market.PriceLevel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

public class SpreadCalculator {

    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    public Optional<Spread> calculate(OrderBook orderBook) {

        if (orderBook == null || !orderBook.isValid()) {
            return Optional.empty();
        }

        PriceLevel bid = orderBook.bestBid();
        PriceLevel ask = orderBook.bestAsk();

        if (bid == null || ask == null) {
            return Optional.empty();
        }

        BigDecimal bidPrice = bid.price();
        BigDecimal askPrice = ask.price();

        if (bidPrice == null || askPrice == null) {
            return Optional.empty();
        }

        if (bidPrice.signum() <= 0 || askPrice.signum() <= 0) {
            return Optional.empty();
        }

        BigDecimal avg = askPrice.add(bidPrice)
                .divide(BigDecimal.valueOf(2), 10, RoundingMode.HALF_UP);

        if (avg.compareTo(ZERO) == 0) {
            return Optional.empty();
        }

        BigDecimal spread = askPrice.subtract(bidPrice)
                .divide(avg, 6, RoundingMode.HALF_UP)
                .multiply(HUNDRED);

        return Optional.of(new Spread(spread));
    }
}