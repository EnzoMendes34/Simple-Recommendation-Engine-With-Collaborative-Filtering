package com.collaborativefiltering.api.controller;

import com.collaborativefiltering.api.dtos.request.InteractionRequestDTO;
import com.collaborativefiltering.api.dtos.response.RecommendationResponseDTO;
import com.collaborativefiltering.api.service.RecommendationService;
import com.collaborativefiltering.interaction.Interaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @PostMapping("/interactions")
    public ResponseEntity<Void> registerInteraction(@RequestBody InteractionRequestDTO dto){
        service.registerInteraction(dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/interactions")
    public ResponseEntity<List<Interaction>> getInteractions() {
        return ResponseEntity.ok(service.findAllInteractions());
    }

    @GetMapping("/interactions/{userId}")
    public ResponseEntity<List<Interaction>> getInteractionsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(service.findAllInteractionsByUserId(userId));
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<RecommendationResponseDTO> getRecommendations(@PathVariable String userId) {
        return ResponseEntity.ok(service.getRecommendations(userId));
    }
}
