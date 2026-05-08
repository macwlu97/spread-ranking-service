package com.platform.spreadranking.application.service;

import com.platform.spreadranking.application.port.in.CalculateRankingUseCase;
import com.platform.spreadranking.application.port.out.MarketProviderPort;
import com.platform.spreadranking.application.port.out.OrderBookProviderPort;
import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.ranking.Group;
import com.platform.spreadranking.domain.ranking.Ranking;
import com.platform.spreadranking.domain.ranking.Ranking.Item;
import com.platform.spreadranking.domain.spread.SpreadCalculator;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

        var markets = marketProvider.getMarkets();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            List<Future<Item>> futures = markets.stream()
                    .map(market -> executor.submit(() -> processMarket(market)))
                    .toList();

            List<Item> items = futures.stream()
                    .map(this::safeGet)
                    .toList();

            return buildRanking(items);
        }
    }

    private Item processMarket(Market market) {

        var orderBook = orderBookProvider.getOrderBook(market);
        var spreadOpt = spreadCalculator.calculate(orderBook);

        return spreadOpt
                .filter(spread -> spread.value() != null)
                .map(spread -> {

                    var formatted = spread.value()
                            .setScale(2, RoundingMode.HALF_UP)
                            .toPlainString();

                    var group = spread.isLow() ? Group.LOW : Group.HIGH;

                    return new Item(
                            market.tickerId(),
                            formatted,
                            group
                    );
                })
                .orElseGet(() -> new Item(
                        market.tickerId(),
                        "N/A",
                        Group.MISSING
                ));
    }

    private Item safeGet(Future<Item> future) {
        try {
            return future.get();
        } catch (Exception e) {
            return new Item("UNKNOWN", "N/A", Group.MISSING);
        }
    }

    private Ranking buildRanking(List<Item> items) {

        var grouped = items.stream()
                .sorted(BY_MARKET)
                .collect(java.util.stream.Collectors.groupingBy(Item::group));

        return new Ranking(
                grouped.getOrDefault(Group.LOW, List.of()),
                grouped.getOrDefault(Group.HIGH, List.of()),
                grouped.getOrDefault(Group.MISSING, List.of())
        );
    }
}