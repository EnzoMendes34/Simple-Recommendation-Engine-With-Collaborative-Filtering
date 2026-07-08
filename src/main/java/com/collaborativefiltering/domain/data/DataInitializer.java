package com.collaborativefiltering.domain.data;

import com.collaborativefiltering.api.dtos.request.InteractionRequestDTO;
import com.collaborativefiltering.api.dtos.request.enums.InteractionType;
import com.collaborativefiltering.api.service.CategoryService;
import com.collaborativefiltering.api.service.ProductService;
import com.collaborativefiltering.api.service.RecommendationService;
import com.collaborativefiltering.api.service.UserService;
import com.collaborativefiltering.domain.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final SampleDataGenerator generator;
    private final UserService userService;
    private final RecommendationService recommendationService;

    public DataInitializer(
            CategoryService categoryService,
            ProductService productService,
            SampleDataGenerator generator,
            UserService userService,
            RecommendationService recommendationService) {

        this.categoryService = categoryService;
        this.productService = productService;
        this.generator = generator;
        this.userService = userService;
        this.recommendationService = recommendationService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Criando os dados de exemplo");

        categoryService.deleteAll();
        productService.deleteAll();
        userService.deleteAll();

        SampleData data = generator.generate();

        data.categories().forEach(categoryService::save);
        System.out.println(data.categories().size() + " categorias criadas.");

        data.products().forEach(productService::save);
        System.out.println(data.products().size() + " produtos criados.");

        generateUsers(userService);
        System.out.println("20 usuários criados.");

        generateInteractions(recommendationService, userService, productService);
        System.out.println("400 interações aleatórias adicionadas.");

        System.out.println("Dados carregados");
    }

    private void generateUsers(UserService userService) {
        for (int i = 0; i <= 20; i++){
            User user = new User();
            userService.save(user);
        }
    }

    private void generateInteractions(RecommendationService recommendationService,
                                      UserService userService,
                                      ProductService productService)
    {
        List<String> userIds = userService.findAllIds();
        List<String> productIds = productService.findAllIds();

        Random random = new Random();

        //400 interações
        for(int i = 0; i <= 400; i++) {
            String userId = userIds.get(random.nextInt(userIds.size()));
            String productId = productIds.get(random.nextInt(productIds.size()));

            double chance = random.nextDouble();

            InteractionRequestDTO dto;

            if (chance < 0.55) {
                dto = new InteractionRequestDTO(userId, productId, InteractionType.VIEW, null);
            } else if (chance < 0.80) {
                dto = new InteractionRequestDTO(userId, productId, InteractionType.CLICK, null);
            } else if (chance < 0.95) {
                dto = new InteractionRequestDTO(userId, productId, InteractionType.PURCHASE, null);
            } else {
                int rating = random.nextInt(5) + 1;
                dto = new InteractionRequestDTO(userId, productId, InteractionType.EXPLICIT_RATING, (Double.parseDouble(String.valueOf(rating))));
            }

            recommendationService.registerInteraction(dto);
        }
    }
}