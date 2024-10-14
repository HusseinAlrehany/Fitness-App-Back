package com.coding.fitness.entity;

import com.coding.fitness.dtos.CartItemsDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
public class CartItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;

    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    //new update from one to one -> many to one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    // convert cart items to cartItems DTO

    public CartItemsDTO getCartDTO(){
         CartItemsDTO cartItemsDTO = new CartItemsDTO();
         cartItemsDTO.setId(id);
         cartItemsDTO.setQuantity(quantity);
         cartItemsDTO.setPrice(price);
         cartItemsDTO.setProductName(product.getName());
         cartItemsDTO.setReturnedImg(product.getImg());
         cartItemsDTO.setProductId(product.getId());

         return cartItemsDTO;
    }

}
