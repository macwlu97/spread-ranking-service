package com.platform.spreadranking.api.controller;

import com.platform.spreadranking.application.port.in.CalculateRankingUseCase;
import com.platform.spreadranking.infrastructure.memory.RankingStore;
import com.platform.spreadranking.api.dto.RankingResponse;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/spread")
public class SpreadController {

    private final CalculateRankingUseCase useCase;
    private final RankingStore store;

    public SpreadController(CalculateRankingUseCase useCase,
                            RankingStore store) {
        this.useCase = useCase;
        this.store = store;
    }

    @PostMapping("/calculate")
    public void calculate() {
        var result = useCase.calculate();
        store.save(result);
    }

    @GetMapping("/ranking")
    public RankingResponse get() {
        return new RankingResponse(
                Instant.now(),
                store.get()
        );
    }
}