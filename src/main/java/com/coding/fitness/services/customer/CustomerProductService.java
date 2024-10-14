package com.coding.fitness.services.customer;

import com.coding.fitness.dtos.ProductDTO;

import java.util.List;

public interface CustomerProductService {


    List<ProductDTO> findAll();

    List<ProductDTO> findAllProductsByName(String name);
}
