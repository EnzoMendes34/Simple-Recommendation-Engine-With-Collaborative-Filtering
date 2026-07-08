package com.collaborativefiltering.domain.data;

import com.collaborativefiltering.api.service.CategoryService;
import com.collaborativefiltering.api.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final SampleDataGenerator generator;

    public DataInitializer(
            CategoryService categoryService,
            ProductService productService,
            SampleDataGenerator generator) {

        this.categoryService = categoryService;
        this.productService = productService;
        this.generator = generator;
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

        System.out.println("Dados carregados");
    }
}