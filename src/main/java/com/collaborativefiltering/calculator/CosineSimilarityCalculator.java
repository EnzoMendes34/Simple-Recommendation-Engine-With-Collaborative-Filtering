package com.collaborativefiltering.calculator;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CosineSimilarityCalculator {

    //Número mínimo de usuários em comum exigido para considerar uma similaridade confiável;
    //Com o valor 1, uma única coincidência de usuário já gera similaridade;
    //Ajuda quando a base de testes é pequena. Onde exigir mais de 1 usuário em comum zera similaridades por falta de
    //volume de dados. Em produção esse valor deve ser recalibrado para entre 2 e 5.
    private static final int MIN_COMMOM_USERS = 1;

    //fórmula similaridade(A, B) = (Σ scoreA[u] * scoreB[u]) / (√(Σ scoreA[u]²) * √(Σ scoreB[u]²))
    public double calculateSimilarity(
            String productAId,
            String productBId,
            Map<String, Map<String, Double>> productUserMatrix
    ) {
        Map<String, Double> scoresA = productUserMatrix.get(productAId);
        Map<String, Double> scoresB = productUserMatrix.get(productBId);

        if(scoresA == null || scoresB == null) {
            return 0.0;
        }

        double numerator = 0.0;
        int commomUsersCount = 0;

        for (Map.Entry<String, Double> entry : scoresA.entrySet()) {
            String user = entry.getKey();
            Double scoreA = entry.getValue();
            Double scoreB = scoresB.get(user);

            if (scoreB != null) {
                numerator += scoreA * scoreB;
                commomUsersCount++;
            }
        }

        if(commomUsersCount < MIN_COMMOM_USERS) {
            return 0.0;
        }

        double magnitudeA = calculateMagnitudeOfScores(scoresA.values().stream().toList());

        double magnitudeB = calculateMagnitudeOfScores(scoresB.values().stream().toList());

        if(magnitudeA == 0.0 || magnitudeB == 0.0) {
            return 0.0;
        }

        return numerator / (magnitudeA * magnitudeB);
    }

    private Double calculateMagnitudeOfScores(List<Double> scores) {
        double sumOfScore = 0.0;
        for (Double score : scores) {
            sumOfScore += score * score;
        }

        return Math.sqrt(sumOfScore);
    }
}
