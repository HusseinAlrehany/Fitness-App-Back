package com.coding.fitness.services.customer;

import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.entity.Product;
import com.coding.fitness.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerProductServiceImpl implements CustomerProductService{

    @Autowired
    private final ProductRepository productRepository;


    @Override
    public List<ProductDTO> findAll() {
        return productRepository.findAll()
                .stream()
                .map(Product::getProductDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<ProductDTO> findAllProductsByName(String name) {
        return productRepository.findAllByNameContaining(name)
                .stream()
                .map(Product::getProductDTO)
                .collect(Collectors.toList());
    }

}
