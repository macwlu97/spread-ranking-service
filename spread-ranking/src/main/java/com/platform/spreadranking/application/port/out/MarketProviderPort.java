package com.platform.spreadranking.application.port.out;

import com.platform.spreadranking.domain.market.Market;

import java.util.List;

public interface MarketProviderPort {
    List<Market> getMarkets();
}