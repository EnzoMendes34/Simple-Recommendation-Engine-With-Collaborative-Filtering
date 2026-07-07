package com.collaborativefiltering.interaction;

public record Impression(String userId, String productId) implements Interaction {

    private static final double WEIGHT = 1.0;

    @Override
    public double score() {
        return WEIGHT;
    }
}
