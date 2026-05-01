package com.meloCoding.request;

import java.math.BigDecimal;

import com.meloCoding.models.Category;

import lombok.Data;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private BigDecimal price;
    private int inventory;
    private String description;
    private Category category;
}
