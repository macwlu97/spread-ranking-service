package com.platform.spreadranking.application.port.in;

import com.platform.spreadranking.domain.ranking.Ranking;

import java.util.Optional;

public interface GetRankingUseCase {
    Optional<Ranking> getLatest();
}