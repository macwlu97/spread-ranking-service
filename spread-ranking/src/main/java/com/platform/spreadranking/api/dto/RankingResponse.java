package com.platform.spreadranking.api.dto;

import com.platform.spreadranking.domain.ranking.Ranking;

public record RankingResponse(
        String timestamp,
        Ranking ranking
) {}