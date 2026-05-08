package com.platform.spreadranking.api.controller;

import com.platform.spreadranking.application.port.in.CalculateRankingUseCase;
import com.platform.spreadranking.infrastructure.memory.RankingStore;
import com.platform.spreadranking.api.dto.RankingResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/spread")
public class SpreadController {

    private final CalculateRankingUseCase useCase;
    private final RankingStore store;

    public SpreadController(CalculateRankingUseCase useCase,
                            RankingStore store) {
        this.useCase = useCase;
        this.store = store;
    }

    @PostMapping("/calculate")
    public ResponseEntity<Void> calculate() {
        var result = useCase.calculate();
        store.save(result);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<RankingResponse> get() {

        return store.get()
                .map(r -> ResponseEntity.ok(
                        new RankingResponse(r)
                ))
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}