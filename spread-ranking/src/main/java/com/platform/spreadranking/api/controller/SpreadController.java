package com.platform.spreadranking.api.controller;

import com.platform.spreadranking.application.port.in.CalculateAndStoreRankingUseCase;
import com.platform.spreadranking.application.port.in.CalculateRankingUseCase;
import com.platform.spreadranking.application.port.in.GetRankingUseCase;
import com.platform.spreadranking.infrastructure.memory.RankingStore;
import com.platform.spreadranking.api.dto.RankingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/spread")
public class SpreadController {

    private final CalculateAndStoreRankingUseCase calculateAndStoreRankingUseCase;
    private final GetRankingUseCase getRankingUseCase;

    public SpreadController(
            CalculateAndStoreRankingUseCase calculateAndStoreRankingUseCase,
            GetRankingUseCase getRankingUseCase
    ) {
        this.calculateAndStoreRankingUseCase = calculateAndStoreRankingUseCase;
        this.getRankingUseCase = getRankingUseCase;
    }

    @PostMapping("/calculate")
    public ResponseEntity<Void> calculateRanking() {
        calculateAndStoreRankingUseCase.execute();
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<RankingResponse> getRanking() {

        return getRankingUseCase.getLatest()
                .map(r -> ResponseEntity.ok(
                        new RankingResponse(Instant.now(), r)
                ))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}