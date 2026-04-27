package com.meloCoding.dream_shops.request;

import java.math.BigDecimal;

import com.meloCoding.dream_shops.models.Category;

import lombok.Data;

@Data
public class AddProductRequest {
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
