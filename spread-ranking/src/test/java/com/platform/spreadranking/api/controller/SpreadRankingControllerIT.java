package com.platform.spreadranking.api.controller;

import com.platform.spreadranking.testconfig.FakeBeansConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(FakeBeansConfig.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SpreadRankingControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldReturnRanking() throws Exception {

        mockMvc.perform(post("/api/v1/spread/calculate")
                        .header("Authorization", "Bearer ABC123"))
                .andExpect(status().isAccepted());

        mockMvc.perform(get("/api/v1/spread/ranking")
                        .header("Authorization", "Bearer ABC123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.ranking.group1").isArray())
                .andExpect(jsonPath("$.ranking.group2").isArray())
                .andExpect(jsonPath("$.ranking.group3").isArray());
    }

    @Test
    void shouldRejectUnauthorized() throws Exception {

        mockMvc.perform(get("/api/v1/spread/ranking"))
                .andExpect(status().isUnauthorized());
    }
}