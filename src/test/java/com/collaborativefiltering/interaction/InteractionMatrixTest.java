package com.collaborativefiltering.interaction;

import com.collaborativefiltering.interaction.matrix.InteractionMatrix;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class InteractionMatrixTest {

    @Test
    void shouldSumScoresForSameUserAndProduct(){
        Interaction impression = new Impression("user1", "product1");
        Interaction click = new Click("user1", "product1");

        List<Interaction> interactions = List.of(impression, click);

        InteractionMatrix builder = new InteractionMatrix();
        Map<String, Map<String, Double>> matrix = builder.matrixBuilder(interactions);

        double expectedScore = impression.score() + click.score();
        assertEquals(expectedScore, matrix.get("user1").get("product1"));
    }

    @Test
    void shouldNotSumScoresForDifferentUserAndDifferentProduct(){
        Interaction impression = new Impression("user1", "product1");
        Interaction click = new Click("user2", "product2");

        List<Interaction> interactions = List.of(impression, click);

        InteractionMatrix builder = new InteractionMatrix();
        Map<String, Map<String, Double>> matrix = builder.matrixBuilder(interactions);

        assertEquals(impression.score(), matrix.get("user1").get("product1"));
        assertNull(matrix.get("user1").get("product2"));

        assertEquals(click.score(), matrix.get("user2").get("product2"));
        assertNull(matrix.get("user2").get("product1"));
    }

}
