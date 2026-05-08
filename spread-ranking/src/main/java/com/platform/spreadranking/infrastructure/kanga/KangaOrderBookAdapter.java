package com.platform.spreadranking.infrastructure.kanga;

import com.platform.spreadranking.application.port.out.OrderBookProviderPort;
import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.market.OrderBook;

import java.math.BigDecimal;

public class KangaOrderBookAdapter implements OrderBookProviderPort {

    @Override
    public OrderBook getOrderBook(Market market) {
        // TODO: HTTP call to /orderbook/{market}
        return new OrderBook(
                new BigDecimal("100"),
                new BigDecimal("102")
        );
    }
}