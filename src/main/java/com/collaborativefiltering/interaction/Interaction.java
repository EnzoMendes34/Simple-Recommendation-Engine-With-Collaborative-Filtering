package com.collaborativefiltering.interaction;

public sealed interface Interaction permits Impression, Click, Purchase, ExplicitRating {
    String userId();
    String productId();
    double score();
}
