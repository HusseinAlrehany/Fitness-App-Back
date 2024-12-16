package com.coding.fitness.services.customer.cart;

import com.coding.fitness.dtos.*;
import com.coding.fitness.entity.CartItems;
import com.coding.fitness.entity.Coupon;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {

    ResponseEntity<?> addProductToCart(AddProductInCartDTO addProductInCartDTO);

    List<CartItemsDTO> getCartByUserId(Long userId);

    OrderDTO placeOrder(PlaceOrderDTO orderDTO);


    void clearCart(Long userId);

    void deleteCartItemById(Long itemId);

    OrderSummary getOrderSummary(Long userId);

    CartItemsDTO increaseItemQuantity(AddProductInCartDTO addProductInCartDTO);

    CartItemsDTO decreaseItemQuantity(AddProductInCartDTO addProductInCartDTO);

    boolean IsCouponExpired(Coupon coupon);
    OrderSummary applyCoupon(Long userId, String code);

    List<OrderDTO> findAllMyOrders(Long userId);
}
