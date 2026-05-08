package com.platform.spreadranking.infrastructure.kanga;

import com.platform.spreadranking.application.port.out.OrderBookProviderPort;
import com.platform.spreadranking.domain.market.Market;
import com.platform.spreadranking.domain.market.OrderBook;
import com.platform.spreadranking.domain.market.PriceLevel;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;

@Component
public class KangaOrderBookAdapter implements OrderBookProviderPort {

    private final RestClient restClient = RestClient.create();

    @Override
    public OrderBook getOrderBook(Market market) {

        var dto = restClient.get()
                .uri("https://public.kanga.exchange/api/market/orderbook/{market}", market.tickerId())
                .retrieve()
                .body(OrderBookDto.class);

        if (dto == null) {
            return new OrderBook(market, List.of(), List.of());
        }

        return new OrderBook(
                market,
                map(dto.bids()),
                map(dto.asks())
        );
    }

    private List<PriceLevel> map(List<String[]> input) {
        if (input == null) return List.of();

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
    ) {}
}