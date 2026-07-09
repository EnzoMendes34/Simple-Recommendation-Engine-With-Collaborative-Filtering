package com.collaborativefiltering.domain.data;

import com.collaborativefiltering.domain.model.Category;
import com.collaborativefiltering.domain.model.Product;

import java.util.List;
import java.util.Map;

public record SampleData(
        List<Category> categories,
        List<Product> products,
        List<UserAffinity> userAffinities,
        Map<String, List<Product>> productsByCategory
) {
}
