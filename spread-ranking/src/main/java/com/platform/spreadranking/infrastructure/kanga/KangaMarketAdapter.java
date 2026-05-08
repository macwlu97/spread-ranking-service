package com.platform.spreadranking.infrastructure.kanga;

import com.platform.spreadranking.application.port.out.MarketProviderPort;
import com.platform.spreadranking.domain.market.Market;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

@Component
public class KangaMarketAdapter implements MarketProviderPort {

    private final RestClient restClient = RestClient.create();

    @Override
    public List<Market> getMarkets() {

        var response = restClient.get()
                .uri("https://public.kanga.exchange/api/market/pairs")
                .retrieve()
                .body(MarketDto[].class);

        if (response == null) return List.of();

        return Arrays.stream(response)
                .map(m -> new Market(m.ticker_id()))
                .toList();
    }

    public record MarketDto(String ticker_id, String base, String target) {}
}