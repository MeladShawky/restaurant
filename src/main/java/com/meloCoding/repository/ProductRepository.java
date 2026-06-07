package com.meloCoding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String category);

    List<Product> findByName(String name);

    boolean existsByName(String name);

}
