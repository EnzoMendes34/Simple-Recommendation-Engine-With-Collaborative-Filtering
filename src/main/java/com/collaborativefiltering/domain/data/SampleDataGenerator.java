package com.collaborativefiltering.domain.data;

import com.collaborativefiltering.domain.model.Category;
import com.collaborativefiltering.domain.model.Product;
import com.collaborativefiltering.domain.model.User;
import com.collaborativefiltering.interaction.*;
import org.springframework.stereotype.Component;

import java.util.*;

import static ch.qos.logback.core.joran.spi.ConsoleTarget.findByName;

@Component
public class SampleDataGenerator {

    private final Random random = new Random();

    private static final int INTERACTIONS_PER_USER = 15;
    private static final int AFFINITY_CHANCE_PERCENT = 80;

    public SampleData generate() {
        List<Category> categories = generateCategories();
        Map<String, List<Product>> productsByCategory = generateProducts(categories);
        List<Product> allProducts = productsByCategory.values().stream().flatMap(List::stream).toList();
        List<UserAffinity> userAffinities = generateUserAffinities(categories);


        return new SampleData(categories, allProducts, userAffinities, productsByCategory);
    }

    public List<Interaction> generateInteractions(SampleData data) {
        List<Interaction> interactions = new ArrayList<>();

        for(UserAffinity userAffinity : data.userAffinities()) {
            String userId = userAffinity.user().getId();
            String affinityCategoryId = userAffinity.category().getId();

            for(int i = 0; i < INTERACTIONS_PER_USER; i++) {
                Product chosenProduct = chooseProduct(data, affinityCategoryId);
                interactions.add(createRandomInteraction(userId, chosenProduct.getId()));
            }
        }

        return interactions;
    }

    private Product chooseProduct(SampleData data, String affinityCategoryId) {
        boolean followsAffinity = random.nextInt(100) < AFFINITY_CHANCE_PERCENT;

        List<Product> candidates = followsAffinity ? data.productsByCategory().get(affinityCategoryId) : data.products();

        return candidates.get(random.nextInt(candidates.size()));
    }

    private Interaction createRandomInteraction(String userId, String productId) {
        int typeChoide = random.nextInt(4);

        return switch (typeChoide) {
            case 0 -> new Impression(userId, productId);
            case 1 -> new Click(userId, productId);
            case 2 -> new Purchase(userId, productId);
            default ->  new ExplicitRating(userId, productId, 1.0 + random.nextInt(5));
        };
    }

    private List<Category> generateCategories() {
        return new ArrayList<>(List.of(
                new Category("Eletrônicos"),
                new Category("Moda"),
                new Category("Livros")
        ));
    }

    private Map<String, List<Product>> generateProducts(List<Category> categories) {
        Map<String, List<Product>> productsByCategory = new HashMap<>();

        Map<String, String[]> productNamesByCategory = Map.of(
                "Eletrônicos", new String[]{"Smartphone X", "Notebook Pro", "Fone Bluetooth", "Smartwatch", "Tablet 10\"", "Câmera Digital", "Carregador Turbo"},
                "Moda", new String[]{"Camiseta Básica", "Calça Jeans", "Jaqueta Corta-Vento", "Tênis Casual", "Boné Aba Reta", "Vestido Verão", "Mochila Urbana"},
                "Livros", new String[]{"Ficção Científica Vol.1", "Romance Histórico", "Guia de Programação", "Biografia Inspiradora", "Suspense Psicológico", "Poesia Contemporânea"}
        );

        for(Category category : categories) {
            String[] names = productNamesByCategory.get(category.getName());
            List<Product> products = new ArrayList<>();

            for (String name : names) {
                products.add(new Product(name, category.getId()));
            }

            productsByCategory.put(category.getId(), products);
        }

        return productsByCategory;
    }

    private List<UserAffinity> generateUserAffinities(List<Category> categories) {
        Category eletronicos = findByName(categories, "Eletrônicos");
        Category moda = findByName(categories, "Moda");
        Category livros = findByName(categories, "Livros");

        return new ArrayList<>(List.of(
                new UserAffinity(new User(), eletronicos),
                new UserAffinity(new User(), eletronicos),
                new UserAffinity(new User(), moda),
                new UserAffinity(new User(), moda),
                new UserAffinity(new User(), livros)
        ));
    }

    private Category findByName(List<Category> categories, String name) {
        return categories.stream().filter(c -> c.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Categoria não encontrada."));
    }
}