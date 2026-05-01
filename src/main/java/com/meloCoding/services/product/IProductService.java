package com.meloCoding.services.product;

import java.util.List;

import com.meloCoding.dto.ProductDto;
import com.meloCoding.models.Product;
import com.meloCoding.request.AddProductRequest;
import com.meloCoding.request.ProductUpdateRequest;

public interface IProductService {
    Product addProduct(AddProductRequest request);

    Product updateProduct(ProductUpdateRequest request, Long productId);

    void deleteProduct(Long productId);

    Product getProductById(Long productId);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByName(String name);

    Long countProducts();

    ProductDto convertToDto(Product product);

    List<ProductDto> getConvertedProducts(List<Product> products);

}
