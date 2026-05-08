package com.platform.spreadranking.testconfig.fake;

import com.platform.spreadranking.application.port.out.OrderBookProviderPort;
import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.market.OrderBook;
import com.platform.spreadranking.domain.market.PriceLevel;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("test")
public class FakeOrderBookProvider implements OrderBookProviderPort {

    @Override
    public OrderBook getOrderBook(Market market) {

        return new OrderBook(
                market,
                List.of(
                        new PriceLevel(new BigDecimal("100"), new BigDecimal("1")),
                        new PriceLevel(new BigDecimal("99"), new BigDecimal("2"))
                ),
                List.of(
                        new PriceLevel(new BigDecimal("101"), new BigDecimal("1")),
                        new PriceLevel(new BigDecimal("102"), new BigDecimal("2"))
                )
        );
    }
}