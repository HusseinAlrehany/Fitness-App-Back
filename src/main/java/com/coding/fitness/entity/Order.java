package com.coding.fitness.entity;

import com.coding.fitness.dtos.OrderDTO;
import com.coding.fitness.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderDescription;

    private Date date;

    private Long amount;

    private String address;

    private String payment;

    private OrderStatus orderStatus;

    private Long totalAmount;

    private Long discount;

    //@Column(unique = true)
    private UUID trackingId;

    //new update from one to one to many to one
    //@JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    private List<CartItems> cartItems;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "coupon_id", referencedColumnName = "id")
    private Coupon coupon;

   //convert order to order DTO
   public OrderDTO getOrderDTO(){
       OrderDTO orderDTO = new OrderDTO();
       orderDTO.setId(id);
       orderDTO.setOrderDescription(orderDescription);
       orderDTO.setOrderStatus(orderStatus);
       orderDTO.setAmount(amount);
       orderDTO.setTotalAmount(totalAmount);
       orderDTO.setDate(date);
       orderDTO.setAddress(address);
       orderDTO.setTrackingId(trackingId);
       orderDTO.setUserName(user.getName());
       orderDTO.setUserId(user.getId());

       return orderDTO;

   }

}
