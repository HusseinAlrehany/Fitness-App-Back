package com.coding.fitness.mapper;

import com.coding.fitness.dtos.*;
import com.coding.fitness.entity.*;
import org.springframework.stereotype.Service;

@Service
public class Mapper {

    //convert the Product to ProductDTO
    public ProductDTO getProductDTO(Product product){
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setByteImg(product.getImg());
        productDTO.setCategoryName(product.getCategory().getName());

        return productDTO;
    }

    public CouponDTO getCouponDTO(Coupon coupon){
        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setId(coupon.getId());
        couponDTO.setCode(coupon.getCode());
        couponDTO.setName(coupon.getName());
        couponDTO.setDiscount(coupon.getDiscount());
        couponDTO.setExpirationDate(coupon.getExpirationDate());

        return couponDTO;
    }

    public Coupon getCoupon(CouponDTO couponDTO){
        Coupon coupon = new Coupon();
        coupon.setName(couponDTO.getName());
        coupon.setCode(couponDTO.getCode());
        coupon.setDiscount(couponDTO.getDiscount());
        coupon.setExpirationDate(couponDTO.getExpirationDate());

        return coupon;
    }

    public CartItemsDTO getCartDTO(CartItems cartItems){
        CartItemsDTO cartItemsDTO = new CartItemsDTO();
        cartItemsDTO.setId(cartItems.getId());
        cartItemsDTO.setQuantity(cartItems.getQuantity());
        cartItemsDTO.setPrice(cartItems.getPrice());
        cartItemsDTO.setProductName(cartItems.getProduct().getName());
        cartItemsDTO.setReturnedImg(cartItems.getProduct().getImg());
        cartItemsDTO.setProductId(cartItems.getProduct().getId());
        cartItemsDTO.setOrderStatus(cartItems.getOrderStatus());

        return cartItemsDTO;
    }

    public UserDTO getUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserRole(user.getRole());

        return userDTO;
    }

    //convert order to order DTO
    public OrderDTO getOrderDTO(Order order){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setOrderDescription(order.getOrderDescription());
        orderDTO.setOrderStatus(order.getOrderStatus());
        orderDTO.setAmount(order.getAmount());
        orderDTO.setTotalAmount(order.getTotalAmount());
        orderDTO.setDate(order.getDate());
        orderDTO.setAddress(order.getAddress());
        orderDTO.setTrackingId(order.getTrackingId());
        orderDTO.setUserName(order.getUser().getName());
        orderDTO.setUserId(order.getUser().getId());
        //setting the discount only if the coupon not null , else it will be null
        orderDTO.setDiscount(order.getCoupon()!= null ? order.getCoupon().getDiscount() : null);
        return orderDTO;

    }
}
