package com.coding.fitness.services.admin.product;

import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.entity.Category;
import com.coding.fitness.entity.Product;
import com.coding.fitness.repository.CategoryRepository;
import com.coding.fitness.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;
    @Override
    public ProductDTO addProduct(ProductDTO productDTO) throws IOException {

       Product product = new Product();
       product.setName(productDTO.getName());
       product.setDescription(productDTO.getDescription());
       product.setPrice(productDTO.getPrice());
       product.setImg(productDTO.getImg().getBytes());

       Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow();

       product.setCategory(category);
        return productRepository.save(product).getProductDTO();
    }

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

    @Override
    public void deleteProduct(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            productRepository.deleteById(productId);
        }
        else {
            throw new RuntimeException("Product Not Found");
        }
    }
}
