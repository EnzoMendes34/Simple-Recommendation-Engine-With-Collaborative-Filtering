package com.collaborativefiltering.api.dtos.request;

import com.collaborativefiltering.api.dtos.request.enums.InteractionType;
import jakarta.annotation.Nullable;

import java.util.Objects;

public class InteractionRequestDTO {

    private String userId;
    private String productId;
    private InteractionType type;
    private double rating;

    public InteractionRequestDTO() {}

    public InteractionRequestDTO(String userId, String productId, InteractionType type, @Nullable Double rating) {
        this.userId = userId;
        this.productId = productId;
        this.type = type;
        if (rating == null){
            rating = 0.0;
        }
        this.rating = rating;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public InteractionType getType() {
        return type;
    }

    public void setType(InteractionType type) {
        this.type = type;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof InteractionRequestDTO that)) return false;
        return Double.compare(rating, that.rating) == 0 && Objects.equals(userId, that.userId) && Objects.equals(productId, that.productId) && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId, type, rating);
    }
}
