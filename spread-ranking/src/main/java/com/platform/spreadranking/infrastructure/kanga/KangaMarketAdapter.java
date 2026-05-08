package com.platform.spreadranking.infrastructure.kanga;

import com.platform.spreadranking.application.port.out.MarketProviderPort;
import com.platform.spreadranking.domain.market.Market;

import java.util.List;

public class KangaMarketAdapter implements MarketProviderPort {

    @Override
    public List<Market> getMarkets() {
        // TODO: HTTP call to /market/pairs
        return List.of(new Market("BTC_USD"));
    }
}
