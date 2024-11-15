package com.coding.fitness.services.admin.product;

import com.coding.fitness.dtos.ProductDTO;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductDTO addProduct(ProductDTO productDTO) throws IOException;

    List<ProductDTO> findAll();

    List<ProductDTO> findAllProductsByName(String name);

    void deleteProduct(Long productId);

    ProductDTO updateProduct(Long productId,ProductDTO productDTO);

    ProductDTO findProductById(Long productId);
}
