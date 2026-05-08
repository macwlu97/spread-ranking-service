package com.platform.spreadranking.infrastructure.memory;

import com.platform.spreadranking.domain.ranking.Ranking;

public class RankingStore {

    private volatile Ranking ranking;

    public void save(Ranking ranking) {
        this.ranking = ranking;
    }

    public Ranking get() {
        return ranking;
    }
}
