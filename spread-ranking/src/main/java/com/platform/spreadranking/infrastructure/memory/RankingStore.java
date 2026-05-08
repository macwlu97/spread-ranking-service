package com.platform.spreadranking.infrastructure.memory;

import com.platform.spreadranking.domain.ranking.Ranking;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class RankingStore {

    private final AtomicReference<Ranking> cache = new AtomicReference<>();

    public void save(Ranking ranking) {
        cache.set(ranking);
    }

    public Optional<Ranking> get() {
        return Optional.ofNullable(cache.get());
    }
}