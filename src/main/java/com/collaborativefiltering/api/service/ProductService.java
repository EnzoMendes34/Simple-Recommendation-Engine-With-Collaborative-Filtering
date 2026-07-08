package com.collaborativefiltering.api.service;

import com.collaborativefiltering.api.repository.ProductRepositoryInMemory;
import com.collaborativefiltering.domain.model.Category;
import com.collaborativefiltering.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepositoryInMemory repository;

    public ProductService(ProductRepositoryInMemory repository) {
        this.repository = repository;
    }

    public void save(Product product) {
        if(product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        repository.save(product);
    }

    public void save(String productName, Category category) {
        if(productName == null || category == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }

        Product product = new Product(productName, category.getId());

        repository.save(product);
    }

    public List<Product> findAll(){
        return repository.findAll();
    }

    public List<String> findAllIds() {

        return repository.findAllIds();
    }

    public void delete(String id) {
        if(id == null) {
            throw new IllegalArgumentException("You must pass an id.");
        }

        repository.delete(id);
    }

    public void deleteAll(){
        repository.deleteAll();
    }


}
