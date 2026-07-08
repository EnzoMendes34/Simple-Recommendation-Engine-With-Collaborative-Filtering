package com.collaborativefiltering.api.repository;

import com.collaborativefiltering.domain.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CategoryRepositoryInMemory {

    private Map<String, Category> categories = new ConcurrentHashMap<>();

    public void save(Category category){
        categories.put(category.getId(), category);
    }

    public Optional<Category> findById(String id) {
        return Optional.ofNullable(categories.get(id));
    }

    public List<Category> findAll(){
        return categories.values().stream().toList();
    }

    public void delete(String id){
        categories.remove(id);
    }

    public void deleteAll(){
        categories.clear();
    }

}
