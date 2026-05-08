package com.platform.spreadranking.domain.spread;

import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.market.OrderBook;
import com.platform.spreadranking.domain.market.PriceLevel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class SpreadCalculatorTest {

    private final SpreadCalculator calculator = new SpreadCalculator();

    @Test
    void shouldCalculateSpreadCorrectly() {
        // given
        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(
                        new PriceLevel(new BigDecimal("4.2610"), new BigDecimal("1")) // BID
                ),
                List.of(
                        new PriceLevel(new BigDecimal("4.5997"), new BigDecimal("1")) // ASK
                )
        );

        // when
        Optional<Spread> result = calculator.calculate(orderBook);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().value())
                .isEqualByComparingTo("7.64");
    }

    @Test
    void shouldReturnEmptyWhenAskMissing() {
        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(new PriceLevel(new BigDecimal("4.0"), new BigDecimal("1"))),
                List.of()
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenBidMissing() {
        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(),
                List.of(new PriceLevel(new BigDecimal("4.0"), new BigDecimal("1")))
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldReturnEmptyWhenBothSidesEmpty() {
        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(),
                List.of()
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldIgnoreInvalidPriceLevels() {
        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(
                        new PriceLevel(null, new BigDecimal("1")),
                        new PriceLevel(new BigDecimal("4.2610"), new BigDecimal("1"))
                ),
                List.of(
                        new PriceLevel(new BigDecimal("4.5997"), new BigDecimal("1"))
                )
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isPresent();
    }

    @Test
    void shouldReturnZeroSpreadWhenBidEqualsAsk() {

        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(new PriceLevel(new BigDecimal("4.0000"), new BigDecimal("1"))),
                List.of(new PriceLevel(new BigDecimal("4.0000"), new BigDecimal("1")))
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isPresent();
        assertThat(result.get().value())
                .isEqualByComparingTo("0.000000");
    }

    @Test
    void shouldReturnEmptyWhenAskIsLowerThanBid() {

        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(new PriceLevel(new BigDecimal("5.0000"), new BigDecimal("1"))),
                List.of(new PriceLevel(new BigDecimal("4.0000"), new BigDecimal("1")))
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldHandleLargeBigDecimalValues() {

        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(new PriceLevel(new BigDecimal("9999999999.9999"), new BigDecimal("1"))),
                List.of(new PriceLevel(new BigDecimal("10000000000.0001"), new BigDecimal("1")))
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isPresent();
        assertThat(result.get().value())
                .isEqualByComparingTo("0.000000");
    }

    @Test
    void shouldIgnoreInvalidAskPriceLevels() {

        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(new PriceLevel(new BigDecimal("4.0000"), new BigDecimal("1"))),
                List.of(
                        new PriceLevel(null, new BigDecimal("1")),
                        new PriceLevel(new BigDecimal("4.5000"), new BigDecimal("1"))
                )
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isPresent();
    }

    @Test
    void shouldReturnEmptyWhenAllPriceLevelsAreInvalid() {

        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(
                        new PriceLevel(null, new BigDecimal("1"))
                ),
                List.of(
                        new PriceLevel(null, new BigDecimal("1"))
                )
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldSelectHighestBidNotFirst() {
        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(
                        new PriceLevel(new BigDecimal("4.0000"), new BigDecimal("1")),
                        new PriceLevel(new BigDecimal("4.5000"), new BigDecimal("1")), // BEST BID
                        new PriceLevel(new BigDecimal("4.2000"), new BigDecimal("1"))
                ),
                List.of(
                        new PriceLevel(new BigDecimal("5.0000"), new BigDecimal("1"))
                )
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isPresent();
        assertThat(result.get().value()).isGreaterThan(BigDecimal.ZERO);
    }

    @Test
    void shouldSelectLowestAskNotFirst() {
        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(
                        new PriceLevel(new BigDecimal("4.0000"), new BigDecimal("1"))
                ),
                List.of(
                        new PriceLevel(new BigDecimal("5.5000"), new BigDecimal("1")),
                        new PriceLevel(new BigDecimal("4.5997"), new BigDecimal("1")), // BEST ASK
                        new PriceLevel(new BigDecimal("6.0000"), new BigDecimal("1"))
                )
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isPresent();
    }

    @Test
    void shouldWorkWithUnsortedOrderBook() {
        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(
                        new PriceLevel(new BigDecimal("4.1"), new BigDecimal("1")),
                        new PriceLevel(new BigDecimal("4.5"), new BigDecimal("1")),
                        new PriceLevel(new BigDecimal("4.2"), new BigDecimal("1"))
                ),
                List.of(
                        new PriceLevel(new BigDecimal("4.9"), new BigDecimal("1")),
                        new PriceLevel(new BigDecimal("4.6"), new BigDecimal("1")),
                        new PriceLevel(new BigDecimal("5.0"), new BigDecimal("1"))
                )
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isPresent();
    }

    @Test
    void shouldRoundSpreadToSixDecimalPlacesExactly() {
        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(new PriceLevel(new BigDecimal("4.2610"), new BigDecimal("1"))),
                List.of(new PriceLevel(new BigDecimal("4.5997"), new BigDecimal("1")))
        );

        Optional<Spread> result = calculator.calculate(orderBook);

        assertThat(result).isPresent();

        assertThat(result.get().value().scale()).isEqualTo(2);
    }

    @Test
    void shouldBeDeterministic() {

        OrderBook orderBook = new OrderBook(
                new Market("BTC_USD"),
                List.of(new PriceLevel(new BigDecimal("4.2610"), new BigDecimal("1"))),
                List.of(new PriceLevel(new BigDecimal("4.5997"), new BigDecimal("1")))
        );

        BigDecimal first = calculator.calculate(orderBook).get().value();
        BigDecimal second = calculator.calculate(orderBook).get().value();

        assertThat(first).isEqualByComparingTo(second);
    }
}