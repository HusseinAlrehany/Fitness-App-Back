package com.coding.fitness.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetailsReviewDTO {
    private List<ProductDTO> productDTOList;
    private Long orderAmount;
}
