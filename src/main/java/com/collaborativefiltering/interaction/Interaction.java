package com.collaborativefiltering.interaction;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Impression.class, name = "IMPRESSION"),
        @JsonSubTypes.Type(value = Click.class, name = "CLICK"),
        @JsonSubTypes.Type(value = Purchase.class, name = "PURCHASE"),
        @JsonSubTypes.Type(value = ExplicitRating.class, name = "EXPLICIT RATING")
})
public sealed interface Interaction permits Impression, Click, Purchase, ExplicitRating {
    String userId();
    String productId();
    double score();
}
