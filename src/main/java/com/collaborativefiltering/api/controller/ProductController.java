package com.collaborativefiltering.api.controller;

import com.collaborativefiltering.api.service.ProductService;
import com.collaborativefiltering.domain.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/ids")
    public ResponseEntity<List<String>> findAllIds(){
        return ResponseEntity.ok(service.findAllIds());
    }

}
