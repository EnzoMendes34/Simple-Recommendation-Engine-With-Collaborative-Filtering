package com.collaborativefiltering.engine;

import com.collaborativefiltering.calculator.CosineSimilarityCalculator;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
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
            return getMostPopularProducts(productUserMatrix, allProductsIds);
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

        return sortByScoreDescending(candidateScores);
    }


    private List<String> getMostPopularProducts(Map<String, Map<String, Double>> productUserMatrix, List<String> productsIds) {
        Map<String, Double> popularityScores = new HashMap<>();

        for(String product : productsIds){
            Map<String, Double> userScores = productUserMatrix.get(product);

            double totalScore = (userScores == null) ? 0.0 : userScores.values().stream().mapToDouble(Double::doubleValue).sum();

            popularityScores.put(product, totalScore);
        }

        return sortByScoreDescending(popularityScores);
    }

    private List<String> sortByScoreDescending(Map<String, Double> scores) {
        return scores.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed()).map(Map.Entry::getKey).toList();
    }

}
