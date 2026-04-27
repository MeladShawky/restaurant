package com.meloCoding.dream_shops.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class OrderItemDto {
    private Long productId;
    private String productName;
    private String productBrand;
    private int quantity;
    private BigDecimal price;
}
