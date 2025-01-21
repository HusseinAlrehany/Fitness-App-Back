package com.coding.fitness.controller.admin;

import com.coding.fitness.dtos.FAQDTO;
import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.services.admin.faq.FAQService;
import com.coding.fitness.services.admin.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;
    private final FAQService faqService;

    //using @ModelAttribute to accept multipart files as images
    @PostMapping("/product")
    public ResponseEntity<ProductDTO> addProduct(@ModelAttribute ProductDTO productDTO) throws IOException {

          return  ResponseEntity.status(HttpStatus.CREATED)
                    .body(productService.addProduct(productDTO));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> findAll(){
        return ResponseEntity.ok(productService.findAll());

    }

    @GetMapping("/search/{name}")
    public ResponseEntity<List<ProductDTO>> getAllProductsByName(@PathVariable String name){
         List<ProductDTO> productDTOS = productService.findAllProductsByName(name);
         return ResponseEntity.ok(productDTOS);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){

         productService.deleteProduct(productId);
       return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ProductDTO> findProductById(@PathVariable Long productId){
        ProductDTO productDTO = productService.findProductById(productId);
        return productDTO != null ? ResponseEntity.ok(productDTO) : ResponseEntity.notFound().build();
    }
    @PutMapping("/product/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId,
                                                    @ModelAttribute ProductDTO productDTO) {
          ProductDTO productDTO1 = productService.updateProduct(productId ,productDTO);
        return productDTO1 != null ? ResponseEntity.ok(productDTO1) : ResponseEntity.notFound().build();
    }
    @PostMapping("/faq/{productId}")
    public ResponseEntity<FAQDTO> createFAQ(@PathVariable Long productId, @RequestBody FAQDTO faqdto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(faqService.createFAQ(productId, faqdto));
    }

}
