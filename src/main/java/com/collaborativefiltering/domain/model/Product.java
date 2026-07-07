package com.collaborativefiltering.domain.model;

import java.util.UUID;

public class Product {

    private String id = UUID.randomUUID().toString();
    private String name;
    private String categoryId;

    public Product(){}

    public Product(String name, String categoryId) {
        this.name = name;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryId(Category category) {
        this.categoryId = category.getId();
    }
}
