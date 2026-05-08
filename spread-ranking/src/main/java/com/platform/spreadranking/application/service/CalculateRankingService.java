package com.platform.spreadranking.application.service;

import com.platform.spreadranking.application.port.in.CalculateRankingUseCase;
import com.platform.spreadranking.application.port.out.MarketProviderPort;
import com.platform.spreadranking.application.port.out.OrderBookProviderPort;
import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.ranking.Ranking;
import com.platform.spreadranking.domain.ranking.Ranking.Item;
import com.platform.spreadranking.domain.spread.SpreadCalculator;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CalculateRankingService implements CalculateRankingUseCase {

    private static final Comparator<Item> BY_MARKET =
            Comparator.comparing(Item::market);

    private final MarketProviderPort marketProvider;
    private final OrderBookProviderPort orderBookProvider;
    private final SpreadCalculator spreadCalculator;

    public CalculateRankingService(
            MarketProviderPort marketProvider,
            OrderBookProviderPort orderBookProvider,
            SpreadCalculator spreadCalculator
    ) {
        this.marketProvider = marketProvider;
        this.orderBookProvider = orderBookProvider;
        this.spreadCalculator = spreadCalculator;
    }

    @Override
    public Ranking calculate() {

        List<Item> group1 = new ArrayList<>();
        List<Item> group2 = new ArrayList<>();
        List<Item> group3 = new ArrayList<>();

        for (Market market : marketProvider.getMarkets()) {

            var orderBook = orderBookProvider.getOrderBook(market);

            var spreadOpt = spreadCalculator.calculate(orderBook);

            // GROUP 3 → missing data
            if (spreadOpt.isEmpty()) {
                group3.add(new Item(market.tickerId(), "N/A"));
                continue;
            }

            var spread = spreadOpt.get();

            var formattedValue = String.format("%.2f", spread.value().doubleValue());

            if (spread.isLow()) {
                group1.add(new Item(market.tickerId(), formattedValue));
            } else {
                group2.add(new Item(market.tickerId(), formattedValue));
            }
        }

        group1.sort(BY_MARKET);
        group2.sort(BY_MARKET);
        group3.sort(BY_MARKET);

        return new Ranking(
                List.copyOf(group1),
                List.copyOf(group2),
                List.copyOf(group3)
        );
    }
}