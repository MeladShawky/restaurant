package com.meloCoding.dream_shops.services.category;

import java.util.List;

import com.meloCoding.dream_shops.models.Category;

public interface ICatagoryService {
    Category getCategoryById(Long id);
    
    Category getCategoryByName(String name);
    
    List<Category> getAllCategories();
    
    Category addCategory(Category category);
    
    Category updateCategory(Category category, Long id);
    
    void deleteCategoryById(Long id);
}
