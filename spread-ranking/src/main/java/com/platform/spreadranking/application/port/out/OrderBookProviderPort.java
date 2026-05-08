package com.platform.spreadranking.application.port.out;

import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.market.OrderBook;

public interface OrderBookProviderPort {
    OrderBook getOrderBook(Market market);
}
