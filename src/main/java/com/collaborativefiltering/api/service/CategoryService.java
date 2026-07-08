package com.collaborativefiltering.api.service;

import com.collaborativefiltering.api.repository.CategoryRepositoryInMemory;
import com.collaborativefiltering.domain.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepositoryInMemory repository;

    public CategoryService(CategoryRepositoryInMemory repository) {
        this.repository = repository;
    }

    public void save(Category category) {
        if(category == null) {
            throw new IllegalArgumentException("Category cannot be null.");
        }

        repository.save(category);
    }

    public List<Category> findAll(){
        return repository.findAll();
    }

    public Optional<Category> findById(String id) {
        if(id == null) {
            throw new IllegalArgumentException("You must pass an id.");
        }

        return repository.findById(id);
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
