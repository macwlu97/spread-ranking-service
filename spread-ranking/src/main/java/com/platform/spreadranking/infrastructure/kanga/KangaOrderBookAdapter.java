package com.platform.spreadranking.infrastructure.kanga;

import com.platform.spreadranking.application.port.out.OrderBookProviderPort;
import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.market.OrderBook;
import com.platform.spreadranking.domain.market.PriceLevel;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;

@Component
@Profile("!test")
public class KangaOrderBookAdapter implements OrderBookProviderPort {

    private final RestClient restClient;
    private final KangaApiProperties properties;

    public KangaOrderBookAdapter(KangaApiProperties properties) {

        this.properties = properties;

        this.restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .build();
    }

    @Override
    public OrderBook getOrderBook(Market market) {

        var dto = restClient.get()
                .uri("/market/orderbook/{market}", market.tickerId())
                .retrieve()
                .body(OrderBookDto.class);

        if (dto == null) {
            return new OrderBook(
                    market,
                    List.of(),
                    List.of()
            );
        }

        return new OrderBook(
                market,
                map(dto.bids()),
                map(dto.asks())
        );
    }

    private List<PriceLevel> map(List<String[]> input) {

        if (input == null) {
            return List.of();
        }

        return input.stream()
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
    ) {
    }
}