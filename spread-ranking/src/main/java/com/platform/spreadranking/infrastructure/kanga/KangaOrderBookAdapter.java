package com.platform.spreadranking.infrastructure.kanga;

import com.platform.spreadranking.application.port.out.OrderBookProviderPort;
import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.market.OrderBook;
import com.platform.spreadranking.domain.market.PriceLevel;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class KangaOrderBookAdapter implements OrderBookProviderPort {

    private final RestClient restClient;

    public KangaOrderBookAdapter(RestClient kangaRestClient) {
        this.restClient = kangaRestClient;
    }

    @Override
    @Retry(name = "kangaApi", fallbackMethod = "fallback")
    public OrderBook getOrderBook(Market market) {

        var dto = restClient.get()
                .uri("/market/orderbook/{market}", market.tickerId())
                .retrieve()
                .body(OrderBookDto.class);

        if (dto == null) {
            return empty(market);
        }

        return new OrderBook(
                market,
                map(dto.bids()),
                map(dto.asks())
        );
    }

    private OrderBook fallback(Market market, Exception ex) {
        return empty(market);
    }

    private OrderBook empty(Market market) {
        return new OrderBook(market, List.of(), List.of());
    }

    private List<PriceLevel> map(List<String[]> input) {
        return Optional.ofNullable(input)
                .orElse(List.of())
                .stream()
                .map(arr -> new PriceLevel(
                        new BigDecimal(arr[0]),
                        new BigDecimal(arr[1])
                ))
                .toList();
    }

    public record OrderBookDto(
            List<String[]> bids,
            List<String[]> asks,
            String ticker_id
    ) {}
}