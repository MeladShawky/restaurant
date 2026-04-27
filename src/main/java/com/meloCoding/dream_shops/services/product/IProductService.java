package com.meloCoding.dream_shops.services.product;

import java.util.List;

import com.meloCoding.dream_shops.dto.ProductDto;
import com.meloCoding.dream_shops.models.Product;
import com.meloCoding.dream_shops.request.AddProductRequest;
import com.meloCoding.dream_shops.request.ProductUpdateRequest;

public interface IProductService {
    Product addProduct(AddProductRequest request);

    Product updateProduct(ProductUpdateRequest request, Long productId);

    void deleteProduct(Long productId);

    Product getProductById(Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductsByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductsByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);

    Long countProducts();

    ProductDto convertToDto(Product product);

    List<ProductDto> getConvertedProducts(List<Product> products);

}
