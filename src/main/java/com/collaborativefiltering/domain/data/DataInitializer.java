package com.collaborativefiltering.domain.data;

import com.collaborativefiltering.api.dtos.request.InteractionRequestDTO;
import com.collaborativefiltering.api.dtos.request.enums.InteractionType;
import com.collaborativefiltering.api.service.CategoryService;
import com.collaborativefiltering.api.service.ProductService;
import com.collaborativefiltering.api.service.RecommendationService;
import com.collaborativefiltering.api.service.UserService;
import com.collaborativefiltering.domain.model.User;
import com.collaborativefiltering.interaction.Interaction;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final SampleDataGenerator generator;
    private final RecommendationService recommendationService;

    public DataInitializer(
            CategoryService categoryService,
            ProductService productService,
            SampleDataGenerator generator,
            RecommendationService recommendationService) {

        this.categoryService = categoryService;
        this.productService = productService;
        this.generator = generator;
        this.recommendationService = recommendationService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Criando os dados de exemplo");

        categoryService.deleteAll();
        productService.deleteAll();

        SampleData data = generator.generate();

        data.categories().forEach(categoryService::save);
        System.out.println(data.categories().size() + " categorias criadas.");

        data.products().forEach(productService::save);
        System.out.println(data.products().size() + " produtos criados.");

        List<Interaction> interactions = generator.generateInteractions(data);
        recommendationService.registerInteractions(interactions);
        System.out.println(interactions.size() + " Interações criadas");

        data.userAffinities().forEach(affinity ->
                System.out.println("Usuário " + affinity.user().getId() + " -> afinidade: " + affinity.category().getName()));

        System.out.println("Dados carregados");
    }

}