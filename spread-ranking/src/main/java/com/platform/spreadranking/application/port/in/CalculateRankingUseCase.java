package com.platform.spreadranking.application.port.in;

import com.platform.spreadranking.domain.ranking.Ranking;

public interface CalculateRankingUseCase {
    Ranking calculate();
}