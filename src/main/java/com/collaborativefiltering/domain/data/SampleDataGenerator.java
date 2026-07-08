package com.collaborativefiltering.domain.data;

import com.collaborativefiltering.domain.model.Category;
import com.collaborativefiltering.domain.model.Product;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class SampleDataGenerator {

    private final Random random = new Random();

    public SampleData generate() {
        List<Category> categories = generateCategories();
        List<Product> products = generateProducts(categories);

        return new SampleData(categories, products);
    }

    private List<Category> generateCategories() {
        List<Category> categories = new ArrayList<>();

        categories.add(new Category("Eletrônicos"));
        categories.add(new Category("Moda"));
        categories.add(new Category("Livros"));
        categories.add(new Category("Casa e Jardim"));
        categories.add(new Category("Esportes"));
        categories.add(new Category("Beleza"));
        categories.add(new Category("Automotivo"));
        categories.add(new Category("Alimentos"));

        return categories;
    }

    private List<Product> generateProducts(List<Category> categories) {
        List<Product> products = new ArrayList<>();
        String[] productPrefixes = {"Smart", "Ultra", "Pro", "Premium", "Novo", "Original", "Super", "Mega"};

        for (int i = 1; i <= 100; i++) {
            Category category = categories.get(random.nextInt(categories.size()));

            String prefix = productPrefixes[random.nextInt(productPrefixes.length)];
            String name = prefix + " " + getBaseProductName(category) + " " + i;

            Product product = new Product(name, category.getId());
            products.add(product);
        }

        return products;
    }

    private String getBaseProductName(Category category) {
        return switch (category.getName()) {
            case "Eletrônicos" -> "Smartphone";
            case "Moda" -> "Camiseta";
            case "Livros" -> "Livro";
            case "Casa e Jardim" -> "Conjunto";
            case "Esportes" -> "Bola";
            case "Beleza" -> "Kit";
            case "Automotivo" -> "Acessório";
            case "Alimentos" -> "Pacote";
            default -> "Produto";
        };
    }
}