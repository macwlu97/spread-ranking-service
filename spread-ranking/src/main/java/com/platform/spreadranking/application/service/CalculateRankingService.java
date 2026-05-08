package com.platform.spreadranking.application.service;

import com.platform.spreadranking.application.port.in.CalculateRankingUseCase;
import com.platform.spreadranking.application.port.out.MarketProviderPort;
import com.platform.spreadranking.application.port.out.OrderBookProviderPort;
import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.market.OrderBook;
import com.platform.spreadranking.domain.ranking.Ranking;
import com.platform.spreadranking.domain.spread.Spread;
import com.platform.spreadranking.infrastructure.memory.RankingStore;

import java.util.*;

public class CalculateRankingService implements CalculateRankingUseCase {

    private final MarketProviderPort marketPort;
    private final OrderBookProviderPort orderBookPort;
    private final RankingStore store;

    public CalculateRankingService(
            MarketProviderPort marketPort,
            OrderBookProviderPort orderBookPort,
            RankingStore store
    ) {
        this.marketPort = marketPort;
        this.orderBookPort = orderBookPort;
        this.store = store;
    }

    @Override
    public void calculate() {

        List<Market> markets = marketPort.getMarkets();

        List<Ranking.Item> g1 = new ArrayList<>();
        List<Ranking.Item> g2 = new ArrayList<>();
        List<Ranking.Item> g3 = new ArrayList<>();

        for (Market m : markets) {

            OrderBook ob = orderBookPort.getOrderBook(m);
            Spread spread = Spread.from(ob);

            if (!spread.isValid()) {
                g3.add(new Ranking.Item(m.tickerId(), "N/A"));
                continue;
            }

            String value = spread.value().setScale(2, java.math.RoundingMode.HALF_UP).toString();

            if (spread.isLow()) {
                g1.add(new Ranking.Item(m.tickerId(), value));
            } else {
                g2.add(new Ranking.Item(m.tickerId(), value));
            }
        }

        Comparator<Ranking.Item> sort = Comparator.comparing(Ranking.Item::market);

        Ranking ranking = new Ranking(
                g1.stream().sorted(sort).toList(),
                g2.stream().sorted(sort).toList(),
                g3.stream().sorted(sort).toList()
        );

        store.save(ranking);
    }
}
