package com.coding.fitness.dtos;

import com.coding.fitness.enums.OrderStatus;
import lombok.Data;

@Data
public class CartItemsDTO {

    private Long id;

    private Long price;

    private Long quantity;

    private OrderStatus orderStatus;

    private Long productId;

    private Long orderId;

    private String productName;

    private Long userId;

    private byte[] returnedImg;


}
