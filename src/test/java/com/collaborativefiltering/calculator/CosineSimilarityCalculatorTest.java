package com.collaborativefiltering.calculator;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CosineSimilarityCalculatorTest {

    @Test
    void shouldReturnOneForPerfectlyProportionalProducts() {
        Map<String, Double> scoresA = Map.of("user1", 2.0, "user2", 1.0);
        Map<String, Double> scoresB = Map.of("user1", 4.0, "user2", 2.0);

        Map<String, Map<String, Double>> productUserMatrix = Map.of(
                "productA", scoresA,
                "productB", scoresB
        );

        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();
        double similarity = calculator.calculateSimilarity("productA", "productB", productUserMatrix);

        assertEquals(1.0, similarity, 0.0001);
    }

    @Test
    void shouldReturnZeroForNullProductA() {
        Map<String, Double> scoresB = Map.of("user1", 4.0, "user2", 2.0);
        Map<String, Double> scoresA = Map.of("user1", 4.0, "user2", 2.0);

        Map<String, Map<String, Double>> productUserMatrix = Map.of(
                "productB", scoresB,
                "productA", scoresA
        );

        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();
        double similarity = calculator.calculateSimilarity("productC", "productB", productUserMatrix);
        assertEquals(0.0, similarity, 0.0001);
    }

    @Test
    void shouldReturnZeroForNonCommomUsers(){
        Map<String, Double> scoresA = Map.of("user1", 2.0, "user2", 1.0);
        Map<String, Double> scoresB = Map.of("user3", 4.0, "user4", 2.0);

        Map<String, Map<String, Double>> productUserMatrix = Map.of(
                "productA", scoresA,
                "productB", scoresB
        );

        CosineSimilarityCalculator calculator = new CosineSimilarityCalculator();
        double similarity = calculator.calculateSimilarity("productA", "productB", productUserMatrix);
        assertEquals(0.0, similarity, 0.0001);
    }
}
