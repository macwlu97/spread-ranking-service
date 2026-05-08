package com.platform.spreadranking.domain.market;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record OrderBook(
        Market market,
        List<PriceLevel> bids,
        List<PriceLevel> asks
) {

    public boolean isValid() {
        return bestBid() != null && bestAsk() != null;
    }

    public PriceLevel bestBid() {
        return bids.stream()
                .filter(PriceLevel::isValid)
                .max(Comparator.comparing(PriceLevel::price))
                .orElse(null);
    }

    public PriceLevel bestAsk() {
        return asks.stream()
                .filter(PriceLevel::isValid)
                .min(Comparator.comparing(PriceLevel::price))
                .orElse(null);
    }
}