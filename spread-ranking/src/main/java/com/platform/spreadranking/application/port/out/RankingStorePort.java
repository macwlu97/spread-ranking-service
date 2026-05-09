package com.platform.spreadranking.application.port.out;

import com.platform.spreadranking.domain.ranking.Ranking;

import java.util.Optional;

public interface RankingStorePort {
    void save(Ranking ranking);
    Optional<Ranking> getLatest();
}