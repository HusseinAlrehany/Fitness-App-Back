package com.coding.fitness.controller.customer;

import com.coding.fitness.dtos.ProductDetailsReviewDTO;
import com.coding.fitness.dtos.ReviewDTO;
import com.coding.fitness.services.customer.review.ProductDetailsReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class ReviewController {

    private final ProductDetailsReviewService productDetailsReviewService;

    @GetMapping("/ordered-products/{orderId}")
    public ResponseEntity<ProductDetailsReviewDTO> getOrderedProductDetailsByOrderId(@PathVariable Long orderId){
        return ResponseEntity.ok(productDetailsReviewService.getProductDetailsAndReviewByOrderId(orderId));
    }

    @PostMapping("/review")
    public ResponseEntity<?> createReview(@ModelAttribute ReviewDTO reviewDTO){
        ReviewDTO reviewDTO1 = productDetailsReviewService.createReview(reviewDTO);

        return reviewDTO1 == null ?
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Something went wrong!") :
                ResponseEntity.status(HttpStatus.CREATED)
                        .body(reviewDTO1);
    }
}
