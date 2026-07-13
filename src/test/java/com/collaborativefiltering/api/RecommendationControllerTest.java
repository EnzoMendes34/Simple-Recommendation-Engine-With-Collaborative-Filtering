package com.collaborativefiltering.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRegisterInteractionAndReturn201() throws Exception {
        String userId = UUID.randomUUID().toString();
        String productId = UUID.randomUUID().toString();

        String requestJson = """
                {
                    "userId": "%s",
                    "productId": "%s",
                    "type": "CLICK"
                }
                """.formatted(userId, productId);

        mockMvc.perform(post("/api/interactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated());
    }


    @Test
    void shouldReturn400ForInvalidInteractionType() throws Exception {
        String requestJson = """
                {
                    "userId": "%s",
                    "productId": "%s",
                    "type": "INVALID_TYPE"
                }
                """.formatted(UUID.randomUUID(), UUID.randomUUID());

        mockMvc.perform(post("/api/interactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnRecommendationsForAnyUser() throws Exception {
        String userId = UUID.randomUUID().toString();

        mockMvc.perform(get("/api/recommendations/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.recommendedProductsIds").isArray());
    }
}
