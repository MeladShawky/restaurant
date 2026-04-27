package com.meloCoding.dream_shops.services.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.dream_shops.models.Product;

public interface productRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String category);

    List<Product> findByName(String name);

    boolean existsByName(String name);

}
