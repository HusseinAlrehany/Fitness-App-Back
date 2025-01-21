package com.coding.fitness.services.admin.product;

import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.entity.Category;
import com.coding.fitness.entity.Product;
import com.coding.fitness.exceptions.ProcessingImgException;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.Mapper;
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

    private final Mapper mapper;
    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {

       Product product = new Product();
       product.setName(productDTO.getName());
       product.setDescription(productDTO.getDescription());
       product.setPrice(productDTO.getPrice());
       try {
           product.setImg(productDTO.getImg().getBytes());

       }catch (IOException ex){
           throw new ProcessingImgException("Error Process Img");
       }

       Category category = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
               ()-> new ValidationException("No Category found")
       );

          product.setCategory(category);
        Product dbProduct = productRepository.save(product);
        return mapper.getProductDTO(dbProduct);
    }

    @Override
    public List<ProductDTO> findAll() {
        List<Product> productList = Optional.of(productRepository.findAll())
                .filter(products-> !products.isEmpty())
                .orElseThrow(()-> new ValidationException("No Products Found"));
        return  productList
                .stream()
                .map(mapper::getProductDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<ProductDTO> findAllProductsByName(String name) {
           Optional.ofNullable(name)
                   .filter(n-> !n.isBlank())
                   .orElseThrow(()-> new ValidationException("Invalid product name"));

           List<Product> productsList = Optional.of(productRepository.findAllByNameContaining(name))
                   .filter(products -> !products.isEmpty())
                   .orElseThrow(()-> new ValidationException("OOPS! No Products Found"));

        return productsList
                .stream()
                .map(mapper::getProductDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = Optional.ofNullable(productId)
                .flatMap(productRepository::findById)
                .orElseThrow(()-> new ValidationException("Product Not Found"));
        productRepository.deleteById(productId);
    }

    @Override
    public ProductDTO findProductById(Long productId) {
        Optional.ofNullable(productId)
                .filter(id -> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid productId"));
       Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ValidationException("No Product Found"));

        return mapper.getProductDTO(productRepository.findById(productId).get());
    }

    @Override
    public ProductDTO updateProduct(Long productId,ProductDTO productDTO) {
        Optional.ofNullable(productId)
                .filter(id -> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid Id"));
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ValidationException("No Product Found"));
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()-> new ValidationException("No category found"));
        Optional.ofNullable(productDTO.getImg())
                 .filter(img-> !img.isEmpty())
                 .orElseThrow(()-> new ValidationException("No Img Found"));

        product.setId(productId);
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        try{
            product.setImg(productDTO.getImg().getBytes());

        }catch(IOException ex){
            throw new ProcessingImgException("Error Processing The Img");
        }
        product.setCategory(category);
        Product dbProduct = productRepository.save(product);
        return mapper.getProductDTO(dbProduct);
    }


}
