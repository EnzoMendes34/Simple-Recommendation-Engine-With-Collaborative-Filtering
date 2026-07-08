package com.collaborativefiltering.api.service;

import com.collaborativefiltering.api.dtos.request.InteractionRequestDTO;
import com.collaborativefiltering.api.dtos.response.RecommendationResponseDTO;
import com.collaborativefiltering.calculator.CosineSimilarityCalculator;
import com.collaborativefiltering.engine.ProductRecommendationEngine;
import com.collaborativefiltering.interaction.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.collaborativefiltering.interaction.matrix.InteractionMatrix.invert;
import static com.collaborativefiltering.interaction.matrix.InteractionMatrix.matrixBuilder;

@Service
public class RecommendationService {

    private final CosineSimilarityCalculator calculator;
    private final ProductRecommendationEngine engine;
    private final ProductService productService;
    private final List<Interaction> interactionList = new ArrayList<>();

    public RecommendationService(
            CosineSimilarityCalculator calculator,
            ProductRecommendationEngine engine,
            ProductService productService
    ) {
        this.calculator = calculator;
        this.engine = engine;
        this.productService = productService;
    }

    public void registerInteraction(InteractionRequestDTO dto){
        if(dto == null) {
            throw new IllegalArgumentException("Interaction cannot be null. Try again");
        }

        Interaction interaction = switch (dto.getType()) {
            case VIEW -> new Impression(dto.getUserId(), dto.getProductId());
            case CLICK -> new Click(dto.getUserId(), dto.getProductId());
            case PURCHASE -> new Purchase(dto.getUserId(), dto.getProductId());
            case EXPLICIT_RATING -> new ExplicitRating(dto.getUserId(), dto.getProductId(), dto.getRating());
        };

        interactionList.add(interaction);
    }

    //Apenas para teste
    public List<Interaction> findAllInteractions(){
        return interactionList;
    }

    public RecommendationResponseDTO getRecommendations(String userId) {
        List<String> allProductsIds = productService.findAllIds();

        Map<String, Map<String, Double>> userProductMatrix = matrixBuilder(interactionList);
        Map<String, Map<String, Double>> productUserMatrix = invert(userProductMatrix);

        List<String> recommendedIds = engine.generateRecommendations(userId, userProductMatrix, productUserMatrix, allProductsIds);

        return new RecommendationResponseDTO(userId, recommendedIds);
    }

}
