package com.meloCoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
