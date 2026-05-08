package com.platform.spreadranking.api.dto;

import com.platform.spreadranking.domain.ranking.Ranking;
import java.time.Instant;

public record RankingResponse(
        Ranking ranking
) {}