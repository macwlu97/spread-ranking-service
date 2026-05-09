package com.platform.spreadranking.application.service;

import com.platform.spreadranking.application.port.in.CalculateAndStoreRankingUseCase;
import com.platform.spreadranking.application.port.in.CalculateRankingUseCase;
import com.platform.spreadranking.application.port.out.RankingStorePort;
import com.platform.spreadranking.domain.ranking.Ranking;
import org.springframework.stereotype.Service;

@Service
public class CalculateAndStoreRankingService implements CalculateAndStoreRankingUseCase {

    private final CalculateRankingUseCase calculateRankingUseCase;
    private final RankingStorePort rankingStorePort;

    public CalculateAndStoreRankingService(
            CalculateRankingUseCase calculateRankingUseCase,
            RankingStorePort rankingStorePort
    ) {
        this.calculateRankingUseCase = calculateRankingUseCase;
        this.rankingStorePort = rankingStorePort;
    }

    @Override
    public void execute() {
        Ranking ranking = calculateRankingUseCase.calculate();
        rankingStorePort.save(ranking);
    }
}