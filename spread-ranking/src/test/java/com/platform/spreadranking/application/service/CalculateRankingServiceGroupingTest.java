package com.platform.spreadranking.application.service;

import com.platform.spreadranking.application.port.out.MarketProviderPort;
import com.platform.spreadranking.application.port.out.OrderBookProviderPort;
import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.market.OrderBook;
import com.platform.spreadranking.domain.ranking.Ranking;
import com.platform.spreadranking.domain.spread.Spread;
import com.platform.spreadranking.domain.spread.SpreadCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CalculateRankingServiceGroupingTest {

    private final MarketProviderPort marketProvider = mock(MarketProviderPort.class);
    private final OrderBookProviderPort orderBookProvider = mock(OrderBookProviderPort.class);
    private final SpreadCalculator spreadCalculator = mock(SpreadCalculator.class);

    private final CalculateRankingService service =
            new CalculateRankingService(marketProvider, orderBookProvider, spreadCalculator);

    @Test
    void shouldCorrectlySeparateMarketsIntoThreeGroups() {

        // given
        Market low = new Market("LOW_USD");
        Market mid = new Market("MID_USD");
        Market missing = new Market("MISS_USD");

        when(marketProvider.getMarkets()).thenReturn(List.of(low, mid, missing));

        OrderBook lowOb = mock(OrderBook.class);
        OrderBook midOb = mock(OrderBook.class);
        OrderBook missOb = mock(OrderBook.class);

        when(orderBookProvider.getOrderBook(low)).thenReturn(lowOb);
        when(orderBookProvider.getOrderBook(mid)).thenReturn(midOb);
        when(orderBookProvider.getOrderBook(missing)).thenReturn(missOb);

        // group1 → <= 2%
        when(spreadCalculator.calculate(lowOb))
                .thenReturn(Optional.of(new Spread(new BigDecimal("1.50"))));

        // group2 → > 2%
        when(spreadCalculator.calculate(midOb))
                .thenReturn(Optional.of(new Spread(new BigDecimal("3.10"))));

        // group3 → missing
        when(spreadCalculator.calculate(missOb))
                .thenReturn(Optional.empty());

        // when
        Ranking result = service.calculate();

        // then (CRITICAL ASSERTIONS)

        assertThat(result.group1())
                .extracting(Ranking.Item::market)
                .containsExactly("LOW_USD");

        assertThat(result.group2())
                .extracting(Ranking.Item::market)
                .containsExactly("MID_USD");

        assertThat(result.group3())
                .extracting(Ranking.Item::market)
                .containsExactly("MISS_USD");

        // extra safety: no leaks between groups
        assertThat(result.group1()).hasSize(1);
        assertThat(result.group2()).hasSize(1);
        assertThat(result.group3()).hasSize(1);
    }
}