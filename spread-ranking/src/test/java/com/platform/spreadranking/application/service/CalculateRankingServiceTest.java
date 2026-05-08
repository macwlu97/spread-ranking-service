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

class CalculateRankingServiceTest {

    private final MarketProviderPort marketProvider = mock(MarketProviderPort.class);
    private final OrderBookProviderPort orderBookProvider = mock(OrderBookProviderPort.class);
    private final SpreadCalculator spreadCalculator = mock(SpreadCalculator.class);

    private final CalculateRankingService service =
            new CalculateRankingService(marketProvider, orderBookProvider, spreadCalculator);

    @Test
    void shouldGroupMarketsCorrectly() {

        // given
        Market btc = new Market("BTC_USD");
        Market eth = new Market("ETH_USD");
        Market ada = new Market("ADA_USD");

        when(marketProvider.getMarkets()).thenReturn(List.of(btc, eth, ada));

        OrderBook btcOb = mock(OrderBook.class);
        OrderBook ethOb = mock(OrderBook.class);
        OrderBook adaOb = mock(OrderBook.class);

        when(orderBookProvider.getOrderBook(btc)).thenReturn(btcOb);
        when(orderBookProvider.getOrderBook(eth)).thenReturn(ethOb);
        when(orderBookProvider.getOrderBook(ada)).thenReturn(adaOb);

        when(spreadCalculator.calculate(btcOb))
                .thenReturn(Optional.of(new Spread(new BigDecimal("1.50"))));

        when(spreadCalculator.calculate(ethOb))
                .thenReturn(Optional.of(new Spread(new BigDecimal("3.50"))));

        when(spreadCalculator.calculate(adaOb))
                .thenReturn(Optional.empty());

        // when
        Ranking result = service.calculate();

        // then
        assertThat(result.group1()).hasSize(1);
        assertThat(result.group2()).hasSize(1);
        assertThat(result.group3()).hasSize(1);

        assertThat(result.group1().getFirst().market()).isEqualTo("BTC_USD");
        assertThat(result.group2().getFirst().market()).isEqualTo("ETH_USD");
        assertThat(result.group3().getFirst().market()).isEqualTo("ADA_USD");

        verify(marketProvider, times(1)).getMarkets();
        verify(orderBookProvider, times(3)).getOrderBook(any());
        verify(spreadCalculator, times(3)).calculate(any());
    }

    @Test
    void shouldSortMarketsAlphabeticallyInsideGroups() {

        // given
        Market b = new Market("BTC_USD");
        Market a = new Market("ADA_USD");

        when(marketProvider.getMarkets()).thenReturn(List.of(b, a));

        OrderBook ob = mock(OrderBook.class);

        when(orderBookProvider.getOrderBook(any())).thenReturn(ob);
        when(spreadCalculator.calculate(any()))
                .thenReturn(Optional.of(new Spread(new BigDecimal("1.0"))));

        // when
        Ranking result = service.calculate();

        // then
        assertThat(result.group1())
                .extracting(Ranking.Item::market)
                .containsExactly("ADA_USD", "BTC_USD");
    }

    @Test
    void shouldHandleAllMissingSpreads() {

        Market btc = new Market("BTC_USD");
        Market eth = new Market("ETH_USD");

        when(marketProvider.getMarkets()).thenReturn(List.of(btc, eth));

        OrderBook ob = mock(OrderBook.class);

        when(orderBookProvider.getOrderBook(any())).thenReturn(ob);
        when(spreadCalculator.calculate(any())).thenReturn(Optional.empty());

        Ranking result = service.calculate();

        assertThat(result.group3()).hasSize(2);
        assertThat(result.group1()).isEmpty();
        assertThat(result.group2()).isEmpty();
    }
}