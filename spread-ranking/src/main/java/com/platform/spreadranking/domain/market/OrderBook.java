package com.platform.spreadranking.domain.market;

import java.math.BigDecimal;

public record OrderBook(BigDecimal bestBid, BigDecimal bestAsk) {

    public boolean isValid() {
        return bestBid != null && bestAsk != null;
    }
}