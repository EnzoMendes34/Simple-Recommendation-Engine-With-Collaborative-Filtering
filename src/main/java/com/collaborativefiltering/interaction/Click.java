package com.collaborativefiltering.interaction;

public record Click(String userId, String productId) implements Interaction {

    private static final double WEIGHT = 2.0;

    @Override
    public double score() {
        return WEIGHT;
    }
}
