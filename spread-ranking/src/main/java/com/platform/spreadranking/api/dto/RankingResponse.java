package com.platform.spreadranking.api.dto;

import java.time.Instant;
import com.platform.spreadranking.domain.ranking.Ranking;

public record RankingResponse(
        Instant timestamp,
        Ranking ranking
) {}