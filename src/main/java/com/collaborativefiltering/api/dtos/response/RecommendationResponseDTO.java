package com.collaborativefiltering.api.dtos.response;

import java.util.List;
import java.util.Objects;

public class RecommendationResponseDTO {

    private String userId;
    private List<String> recommendedProductsIds;

    public RecommendationResponseDTO(String userId, List<String> recommendedProductsIds) {
        this.userId = userId;
        this.recommendedProductsIds = recommendedProductsIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getRecommendedProductsIds() {
        return recommendedProductsIds;
    }

    public void setRecommendedProductsIds(List<String> recommendedProductsIds) {
        this.recommendedProductsIds = recommendedProductsIds;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RecommendationResponseDTO that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(recommendedProductsIds, that.recommendedProductsIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, recommendedProductsIds);
    }
}
