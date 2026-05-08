package com.platform.spreadranking.application.service;

import com.platform.spreadranking.application.port.in.CalculateRankingUseCase;
import com.platform.spreadranking.application.port.out.MarketProviderPort;
import com.platform.spreadranking.application.port.out.OrderBookProviderPort;
import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.ranking.Ranking;
import com.platform.spreadranking.domain.spread.SpreadCalculator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CalculateRankingService implements CalculateRankingUseCase {

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

        List<Ranking.Item> g1 = new ArrayList<>();
        List<Ranking.Item> g2 = new ArrayList<>();
        List<Ranking.Item> g3 = new ArrayList<>();

        for (Market market : marketProvider.getMarkets()) {

            var ob = orderBookProvider.getOrderBook(market);

            var spreadOpt = spreadCalculator.calculate(ob);

            if (spreadOpt.isEmpty()) {
                g3.add(new Ranking.Item(market.tickerId(), "N/A"));
                continue;
            }

            var spread = spreadOpt.get();

            if (spread.isLow()) {
                g1.add(new Ranking.Item(market.tickerId(), spread.value().toString()));
            } else {
                g2.add(new Ranking.Item(market.tickerId(), spread.value().toString()));
            }
        }

        Comparator<Ranking.Item> sort = Comparator.comparing(Ranking.Item::market);

        g1.sort(sort);
        g2.sort(sort);
        g3.sort(sort);

        return new Ranking(g1, g2, g3);
    }
}