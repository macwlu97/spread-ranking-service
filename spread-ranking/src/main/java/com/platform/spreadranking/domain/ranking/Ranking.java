package com.platform.spreadranking.domain.ranking;

import java.util.List;

public record Ranking(
        List<Item> group1,
        List<Item> group2,
        List<Item> group3
) {
    public Ranking {
        group1 = List.copyOf(group1);
        group2 = List.copyOf(group2);
        group3 = List.copyOf(group3);
    }

    public record Item(
            String market,
            String spread,
            Group group
    ) {}
}