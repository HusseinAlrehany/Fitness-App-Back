package com.coding.fitness.dtos;

import lombok.Data;

@Data
public class OrderSummary {

    private Long totalQuantity;
    private Long totalPrice;
    private Long subTotal;
}
