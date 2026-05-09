package com.platform.spreadranking.infrastructure.memory;

import com.platform.spreadranking.application.port.out.RankingStorePort;
import com.platform.spreadranking.domain.ranking.Ranking;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class RankingStore implements RankingStorePort {

    private final AtomicReference<Ranking> cache = new AtomicReference<>();

    @Override
    public void save(Ranking ranking) {
        cache.set(ranking);
    }

    @Override
    public Optional<Ranking> getLatest() {
        return Optional.ofNullable(cache.get());
    }
}