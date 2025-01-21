package com.coding.fitness.services.customer.review;

import com.coding.fitness.dtos.ProductDetailsReviewDTO;
import com.coding.fitness.dtos.ReviewDTO;

public interface ProductDetailsReviewService {

    public ProductDetailsReviewDTO getProductDetailsAndReviewByOrderId(Long orderId);

    ReviewDTO createReview(ReviewDTO reviewDTO);
}
