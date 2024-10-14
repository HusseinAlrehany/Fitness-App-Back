package com.coding.fitness.dtos;

import com.coding.fitness.enums.OrderStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDTO {

   private Long userId;

    private Long id;

    private String orderDescription;

    private Date date;

    private Long amount;

    private String address;

    private String payment;

    private OrderStatus orderStatus;

    private Long totalAmount;

    private Long discount;

    private UUID trackingId;

    private String userName;

 private List<CartItemsDTO> cartItems = new ArrayList<>();

}
