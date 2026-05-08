package com.platform.spreadranking.domain.market;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.platform.spreadranking.domain.spread.Spread;
import com.platform.spreadranking.domain.spread.SpreadCalculator;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrderBookSpreadScenarioTest {

    private final SpreadCalculator calculator = new SpreadCalculator();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void shouldCalculateSpreadFromRealJsonOrderBook() throws Exception {

        //given
        InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream("mocks/orderbook-meme-usdt.json");

        assertThat(is).isNotNull();

        OrderBook orderBook = mapper.readValue(is, OrderBook.class);

        //when
        Optional<Spread> spread = calculator.calculate(orderBook);

        //then
        assertThat(spread).isPresent();

        assertThat(spread.get().value())
                .isEqualByComparingTo(new BigDecimal("2.62"));
    }
}