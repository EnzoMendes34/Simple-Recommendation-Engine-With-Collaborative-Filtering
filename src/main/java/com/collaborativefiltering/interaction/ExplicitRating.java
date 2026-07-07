package com.collaborativefiltering.interaction;

public record ExplicitRating(String userId, String productId, double rating) implements Interaction {
    private static final double MIN_RATING = 1.0;
    private static final double MAX_RATING = 5.0;
    private static final double RATING_WEIGHT = 5.0;
    private static final double MIDPOINT = (MIN_RATING + MAX_RATING) / 2;
    private static final double RANGE = MAX_RATING - MIN_RATING;


    public ExplicitRating {
        if(rating > MAX_RATING || rating < MIN_RATING) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
    }

    @Override
    public double score() {
        double normalized = (rating - MIDPOINT) / (RANGE / 2);

        return normalized * RATING_WEIGHT;
    }
}
