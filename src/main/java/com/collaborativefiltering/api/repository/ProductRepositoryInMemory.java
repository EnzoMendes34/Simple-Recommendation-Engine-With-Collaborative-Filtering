package com.collaborativefiltering.api.repository;

import com.collaborativefiltering.domain.model.Product;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductRepositoryInMemory {

    private Map<String, Product> products = new ConcurrentHashMap<>();

    public void save(Product product){
        products.put(product.getId(), product);
    }

    public Optional<Product> findById(String id) {
        return Optional.ofNullable(products.get(id));
    }

    public List<Product> findAll(){
        return products.values().stream().toList();
    }

    public List<String> findAllIds() {
        return products.keySet().stream().toList();
    }

    public void delete(String id){
        products.remove(id);
    }

    public void deleteAll(){
        products.clear();
    }
}
