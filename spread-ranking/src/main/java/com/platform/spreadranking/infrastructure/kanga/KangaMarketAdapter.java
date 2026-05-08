package com.platform.spreadranking.infrastructure.kanga;

import com.platform.spreadranking.application.port.out.MarketProviderPort;
import com.platform.spreadranking.domain.market.Market;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class KangaMarketAdapter implements MarketProviderPort {

    private final RestClient restClient;

    public KangaMarketAdapter(RestClient kangaRestClient) {
        this.restClient = kangaRestClient;
    }

    @Override
    @Retry(name = "kangaApi", fallbackMethod = "fallback")
    public List<Market> getMarkets() {

        var response = restClient.get()
                .uri("/market/pairs")
                .retrieve()
                .body(MarketDto[].class);

        var res = response;
        System.out.println(response);
        return Optional.ofNullable(response)
                .stream()
                .flatMap(Arrays::stream)
                .map(KangaMarketAdapter::toMarket)
                .toList();
    }

    private static Market toMarket(MarketDto dto) {
        return new Market(dto.ticker_id());
    }

    private List<Market> fallback(Exception ex) {
        throw new IllegalStateException("Kanga API unavailable", ex);
    }

    public record MarketDto(
            String ticker_id,
            String base,
            String target
    ) {}
}