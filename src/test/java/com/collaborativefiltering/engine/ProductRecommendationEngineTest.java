package com.collaborativefiltering.engine;

import com.collaborativefiltering.calculator.CosineSimilarityCalculator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductRecommendationEngineTest {

    @Test
    void shouldReturnProductsThatUserDidNotInteractWith(){
        Map<String, Map<String, Double>> productuserMatrix = new HashMap<>(
                Map.of(
                        "p1", Map.of("u1", 2.0),
                        "p2", Map.of("u2", 2.0),
                        "pC", Map.of("u1", 2.0, "u2", 1.0),
                        "pD", Map.of("u2", 3.0)
                )
        );

        List<String> allProductsIds = new ArrayList<>(List.of("p1", "p2", "pC", "pD"));

        Map<String, Map<String, Double>> userProductMatrix = new HashMap<>(
                Map.of("targetUser", Map.of("p1", 5.0, "p2", 1.0))
        );

        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();

        ProductRecommendationEngine recommendationEngine = new ProductRecommendationEngine(calculator);

        List<String> result = recommendationEngine.generateRecommendations("targetUser", userProductMatrix, productuserMatrix, allProductsIds);

        assertEquals(List.of("pC", "pD"), result);

    }

    @Test
    void shouldReturnMostPopularProductsWhenUserHasNoHistory() {
        Map<String, Map<String, Double>> productUserMatrix = new HashMap<>(
                Map.of(
                        "p1", Map.of("u1", 3.0, "u2", 2.0),
                        "p2", Map.of("u1", 1.0),
                        "p3", Map.of("u1", 4.0, "u2" , 4.0, "u3" , 4.0)
                )
        );

        //usuário sem nenhum interação, testando coldStart
        Map<String, Map<String, Double>> userProductMatrix = new HashMap<>();

        List<String> allProductsIds = new ArrayList<>(List.of("p1", "p2", "p3"));

        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();
        ProductRecommendationEngine engine = new ProductRecommendationEngine(calculator);

        List<String> result = engine.generateRecommendations("newUser", userProductMatrix, productUserMatrix, allProductsIds);

        assertEquals(List.of("p3", "p1", "p2"), result);
    }

}
