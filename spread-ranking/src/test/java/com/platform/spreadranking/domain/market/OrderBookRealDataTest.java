package com.platform.spreadranking.domain.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class OrderBookRealDataTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldCorrectlyFindBestBidAndAskFromRealOrderbook() throws Exception {

        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("mocks/orderbook-meme-usdt.json");

        assertThat(is).isNotNull();

        OrderBook orderBook = mapper.readValue(is, OrderBook.class);

        PriceLevel bestBid = orderBook.bestBid();
        PriceLevel bestAsk = orderBook.bestAsk();

        assertThat(bestBid.price())
                .isEqualByComparingTo("0.00063286");

        assertThat(bestAsk.price())
                .isEqualByComparingTo("0.00064964");
    }
}