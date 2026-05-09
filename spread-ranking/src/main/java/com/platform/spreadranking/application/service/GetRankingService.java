package com.platform.spreadranking.application.service;

import com.platform.spreadranking.application.port.in.GetRankingUseCase;
import com.platform.spreadranking.application.port.out.RankingStorePort;
import com.platform.spreadranking.domain.ranking.Ranking;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GetRankingService implements GetRankingUseCase {

    private final RankingStorePort rankingStorePort;

    public GetRankingService(RankingStorePort rankingStorePort) {
        this.rankingStorePort = rankingStorePort;
    }

    @Override
    public Optional<Ranking> getLatest() {
        return rankingStorePort.getLatest();
    }
}