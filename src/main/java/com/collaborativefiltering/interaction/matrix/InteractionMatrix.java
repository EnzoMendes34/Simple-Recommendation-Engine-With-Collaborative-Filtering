package com.collaborativefiltering.interaction.matrix;

import com.collaborativefiltering.interaction.Interaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InteractionMatrix {

    //devolve Map<UserId, Map<ProductId, Score>>
    public static Map<String, Map<String, Double>> matrixBuilder(List<Interaction> interactions) {
        Map<String, Map<String, Double>> matrix = new HashMap<>();

        for(Interaction interaction : interactions) {
            double score = interaction.score();
            matrix.computeIfAbsent(interaction.userId(), k -> new HashMap<>())
                    .merge(interaction.productId(), score, Double::sum);
        }

        return matrix;
    }

    //devolve Map<ProductId, Map<UserId, Score>>
    public static Map<String, Map<String, Double>> invert(Map<String, Map<String, Double>> userProductMatrix) {
        Map<String, Map<String, Double>> productUserMatrix = new HashMap<>();

        for(Map.Entry<String, Map<String, Double>> extern : userProductMatrix.entrySet()) {
            String userId = extern.getKey();
            Map<String, Double> products = extern.getValue();

            for(Map.Entry<String, Double> intern : products.entrySet()) {
                String productId = intern.getKey();
                Double score = intern.getValue();

                productUserMatrix
                        .computeIfAbsent(productId, k -> new HashMap<>())
                        .put(userId, score);
            }
        }
        return productUserMatrix;
    }
}
