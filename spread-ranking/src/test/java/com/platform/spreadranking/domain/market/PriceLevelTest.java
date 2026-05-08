package com.platform.spreadranking.domain.market;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PriceLevelTest {

    @Test
    void shouldBeValid() {

        PriceLevel priceLevel = new PriceLevel(
                new BigDecimal("10"),
                new BigDecimal("1")
        );

        assertThat(priceLevel.isValid()).isTrue();
    }

    @Test
    void shouldBeInvalidWhenPriceIsZero() {

        PriceLevel priceLevel = new PriceLevel(
                new BigDecimal("0"),
                new BigDecimal("1")
        );

        assertThat(priceLevel.isValid()).isFalse();
    }

    @Test
    void shouldBeInvalidWhenVolumeNegative() {

        PriceLevel priceLevel = new PriceLevel(
                new BigDecimal("10"),
                new BigDecimal("-1")
        );

        assertThat(priceLevel.isValid()).isFalse();
    }

    @Test
    void shouldBeInvalidWhenNulls() {

        PriceLevel priceLevel = new PriceLevel(null, null);

        assertThat(priceLevel.isValid()).isFalse();
    }
}