package com.coding.fitness.services.customer.review;

import com.coding.fitness.dtos.ProductDTO;
import com.coding.fitness.dtos.ProductDetailsReviewDTO;
import com.coding.fitness.dtos.ReviewDTO;
import com.coding.fitness.entity.*;
import com.coding.fitness.exceptions.ProcessingImgException;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.Mapper;
import com.coding.fitness.repository.OrderRepository;
import com.coding.fitness.repository.ProductRepository;
import com.coding.fitness.repository.ReviewRepository;
import com.coding.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductDetailsReviewServiceImpl implements ProductDetailsReviewService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    private final Mapper mapper;


    public ProductDetailsReviewDTO getProductDetailsAndReviewByOrderId(Long orderId){
        Optional.ofNullable(orderId)
                .filter(id-> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid OrderID"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(()-> new ValidationException("No Order Found"));

        ProductDetailsReviewDTO productDetailsReviewDTO = new ProductDetailsReviewDTO();
        productDetailsReviewDTO.setOrderAmount(order.getTotalAmount());

        List<ProductDTO> productDTOList = new ArrayList<>();
        for(CartItems cartItems: order.getCartItems()){
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(cartItems.getProduct().getId());
            productDTO.setPrice(cartItems.getPrice());
            productDTO.setName(cartItems.getProduct().getName());
            productDTO.setQuantity(cartItems.getQuantity());
            productDTO.setByteImg(cartItems.getProduct().getImg());

            productDTOList.add(productDTO);
        }
         productDetailsReviewDTO.setProductDTOList(productDTOList);

          return productDetailsReviewDTO;
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(()-> new ValidationException("No Product Found"));
        User user = userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(()-> new ValidationException("No User Found"));

        Review review = new Review();
        review.setRating(reviewDTO.getRating());
        review.setProduct(product);
        review.setUser(user);
        review.setDescription(reviewDTO.getDescription());

        try{
            review.setImg(reviewDTO.getImg().getBytes());

        } catch(IOException ex){
            throw new ProcessingImgException("Error Processing Img");
        }

        Review savedReview = reviewRepository.save(review);
       return mapper.getReviewDTO(savedReview);
    }


}
