package com.coding.fitness.dtos;

import lombok.Data;

@Data
public class PlaceOrderDTO {

    private Long userId;
    private String address;

    private String orderDescription;

    private String couponCode;

}
