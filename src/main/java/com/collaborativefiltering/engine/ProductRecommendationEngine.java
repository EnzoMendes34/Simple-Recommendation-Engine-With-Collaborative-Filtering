package com.collaborativefiltering.engine;

import com.collaborativefiltering.calculator.CosineSimilarityCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.Entry.comparingByValue;

public class ProductRecommendationEngine {

    private final CosineSimilarityCalculator calculator;

    public ProductRecommendationEngine(CosineSimilarityCalculator calculator) {
        this.calculator = calculator;
    }

    public List<String> generateRecommendations(
            String userId,
            Map<String, Map<String, Double>> userProductMatrix,
            Map<String, Map<String, Double>> productUserMatrix,
            List<String> allProductsIds
    ) {
        Map<String, Double> userInteractions = userProductMatrix.get(userId);

        if (userInteractions == null) {
            return new ArrayList<>();
        }

        List<String> productsThatUserDidNotInteractWith = new ArrayList<>();
        for (String productId : allProductsIds) {
            if (!userInteractions.containsKey(productId)) {
                productsThatUserDidNotInteractWith.add(productId);
            }
        }

        Map<String, Double> candidateScores = new HashMap<>();

        for(String candidate : productsThatUserDidNotInteractWith) {

            double numerator = 0.0;
            double denominatorSum = 0.0;

            for (Map.Entry<String, Double> entry : userInteractions.entrySet()) {
                double similarity = calculator.calculateSimilarity(candidate, entry.getKey(), productUserMatrix);
                numerator += similarity * entry.getValue();
                denominatorSum += Math.abs(similarity);
            }
            double predictedScore = (denominatorSum == 0.0) ? 0.0 : numerator /denominatorSum;
            candidateScores.put(candidate, predictedScore);
        }

        return candidateScores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .map(Map.Entry::getKey).toList();
    }
}
