package com.platform.spreadranking.api.controller;

import com.platform.spreadranking.application.port.in.CalculateRankingUseCase;
import com.platform.spreadranking.infrastructure.memory.RankingStore;
import com.platform.spreadranking.api.dto.RankingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/spread")
public class SpreadController {

    private final CalculateRankingUseCase calculateRankingUseCase;
    private final RankingStore rankingStore;

    public SpreadController(CalculateRankingUseCase calculateRankingUseCase,
                            RankingStore rankingStore) {
        this.calculateRankingUseCase = calculateRankingUseCase;
        this.rankingStore = rankingStore;
    }

    @PostMapping("/calculate")
    public ResponseEntity<Void> calculateRanking() {
        var result = calculateRankingUseCase.calculate();
        rankingStore.save(result);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<RankingResponse> getRanking() {

        return rankingStore.get()
                .map(r -> ResponseEntity.ok(
                        new RankingResponse(Instant.now(), r)
                ))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}