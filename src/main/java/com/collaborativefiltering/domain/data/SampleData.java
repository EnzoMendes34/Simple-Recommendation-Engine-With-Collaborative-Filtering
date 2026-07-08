package com.collaborativefiltering.domain.data;

import com.collaborativefiltering.domain.model.Category;
import com.collaborativefiltering.domain.model.Product;

import java.util.List;

public record SampleData(
        List<Category> categories,
        List<Product> products
) {
}
