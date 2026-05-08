package com.platform.spreadranking.api.controller;

import com.platform.spreadranking.testconfig.FakeBeansConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(FakeBeansConfig.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SpreadCalculateControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCalculateRanking_withBearerAuth() throws Exception {

        mockMvc.perform(post("/api/v1/spread/calculate")
                        .header("Authorization", "Bearer ABC123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldRejectWithoutAuth() throws Exception {

        mockMvc.perform(post("/api/spread/calculate"))
                .andExpect(status().isUnauthorized());
    }
}