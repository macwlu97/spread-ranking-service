package com.platform.spreadranking.testconfig.fakes;

import com.platform.spreadranking.application.port.in.CalculateRankingUseCase;
import com.platform.spreadranking.domain.ranking.Ranking;

import java.util.List;

public class FakeCalculateRankingUseCase implements CalculateRankingUseCase {

    @Override
    public Ranking calculate() {
        return new Ranking(
                List.of(),
                List.of(),
                List.of()
        );
    }
}