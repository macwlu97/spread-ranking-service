package com.platform.spreadranking.api.controller;

import com.platform.spreadranking.application.port.in.CalculateRankingUseCase;
import com.platform.spreadranking.infrastructure.memory.RankingStore;
import com.platform.spreadranking.domain.ranking.Ranking;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/spread")
public class SpreadController {

    private final CalculateRankingUseCase useCase;
    private final RankingStore store;

    public SpreadController(CalculateRankingUseCase useCase, RankingStore store) {
        this.useCase = useCase;
        this.store = store;
    }

    @PostMapping("/calculate")
    public void calculate() {
        useCase.calculate();
    }

    @GetMapping("/ranking")
    public Ranking get() {
        return store.get();
    }
}
