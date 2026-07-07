package com.collaborativefiltering.interaction;

public record Purchase(String userId, String productId) implements Interaction {

    private static final double WEIGHT = 5.0;

    @Override
    public double score() {
        return WEIGHT;
    }
}
