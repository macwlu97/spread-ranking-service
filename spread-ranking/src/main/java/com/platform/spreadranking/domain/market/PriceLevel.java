package com.platform.spreadranking.domain.market;

import java.math.BigDecimal;

public record PriceLevel(
        BigDecimal price,
        BigDecimal volume
) {

    public boolean isValid() {
        return price != null
                && volume != null
                && price.signum() > 0
                && volume.signum() >= 0;
    }
}