package com.coding.fitness.dtos;

import lombok.Data;

@Data
public class AddProductInCartDTO {

    private Long userId;
    private Long productId;
}
