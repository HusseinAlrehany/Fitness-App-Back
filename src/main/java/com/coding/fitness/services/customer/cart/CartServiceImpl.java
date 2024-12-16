package com.coding.fitness.services.customer.cart;
import com.coding.fitness.dtos.*;
import com.coding.fitness.entity.*;
import com.coding.fitness.enums.OrderStatus;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.Mapper;
import com.coding.fitness.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private Mapper mapper;

    @Override
    public ResponseEntity<?> addProductToCart(AddProductInCartDTO addProductInCartDTO) {
       User user = userRepository.findById(addProductInCartDTO.getUserId())
               .orElseThrow(()-> new ValidationException("User Not Found"));
       Product product = productRepository.findById(addProductInCartDTO.getProductId())
               .orElseThrow(()-> new ValidationException("Product Not Found"));

        //check if the product is already in the cart
        List<CartItems> cartItemsList = cartItemRepository.findByUserIdAndOrderIsNull(addProductInCartDTO.getUserId());

        Optional<CartItems> existingCartItems = cartItemsList
                .stream()
                .filter(cartItem -> cartItem.getProduct().getId().equals(addProductInCartDTO.getProductId()))
                .findFirst();


        //if the product is already in the cart, increase the quantity
        if(existingCartItems.isPresent()){
             CartItems cartItems = existingCartItems.get();
             cartItems.setQuantity(cartItems.getQuantity() + 1 );
             return ResponseEntity.status(HttpStatus.CREATED)
                     .body(cartItemRepository.save(cartItems));
        }
            //otherwise, create a new cart Item
            CartItems newCartItem = new CartItems();
            newCartItem.setProduct(product);
            newCartItem.setUser(user);
            newCartItem.setQuantity(1L);
            newCartItem.setPrice(product.getPrice());
            newCartItem.setOrderStatus(OrderStatus.PENDING);

       return ResponseEntity.status(HttpStatus.CREATED)
               .body(cartItemRepository.save(newCartItem));
    }

    @Override
    public List<CartItemsDTO> getCartByUserId(Long userId) {
        return cartItemRepository.findByUserIdAndOrderIsNull(userId)
                .stream()
                .map(mapper::getCartDTO)
                .collect(Collectors.toList());
    }


    @Override
    public void clearCart(Long userId){
          List<CartItems> cartItems = cartItemRepository.findByUserIdAndOrderIsNull(userId);
          if(!cartItems.isEmpty()){
             cartItems.forEach(item-> {
                 cartItemRepository.delete(item);
             });
          } else {
              throw new ValidationException("Items can not be deleted");
          }
    }

    @Override
    public void deleteCartItemById(Long itemId) {
        Optional<CartItems> cartItems = cartItemRepository.findById(itemId);
        if(cartItems.isPresent()){
            cartItemRepository.delete(cartItems.get());
        } else{
            throw new ValidationException("Item can not be deleted");
        }
    }

    @Override
    public OrderSummary getOrderSummary(Long userId) {
       List<CartItems> cartItems = cartItemRepository.findByUserIdAndOrderIsNull(userId);
       if(cartItems.isEmpty()){
           throw new ValidationException("Cart Is Empty");
       }
        OrderSummary orderSummary = new OrderSummary();
        Long totalQuantity = cartItems.stream()
               .mapToLong(item-> item.getQuantity()).sum();
        Long totalPrice = cartItems.stream()
                .mapToLong(item-> item.getQuantity() * item.getPrice()).sum();
        Long subTotal = cartItems.stream()
                .mapToLong(item -> item.getQuantity() * item.getPrice()).sum();

        orderSummary.setTotalPrice(totalPrice);
        orderSummary.setTotalQuantity(totalQuantity);
        orderSummary.setSubTotal(subTotal);

        return orderSummary;
    }

    @Override
    public CartItemsDTO increaseItemQuantity(AddProductInCartDTO addProductInCartDTO) {

        CartItems cartItems = cartItemRepository.findByUserIdAndProductIdAndOrderIsNull(
                addProductInCartDTO.getUserId(),
                addProductInCartDTO.getProductId());
       if(cartItems == null ){
           throw new ValidationException("Cart Is Empty");
       }
       cartItems.setQuantity(cartItems.getQuantity() + 1);
        CartItems savedCartItems = cartItemRepository.save(cartItems);


        return mapper.getCartDTO(cartItems);
    }

    @Override
    public CartItemsDTO decreaseItemQuantity(AddProductInCartDTO addProductInCartDTO) {
        CartItems cartItems = cartItemRepository.findByUserIdAndProductIdAndOrderIsNull(
                addProductInCartDTO.getUserId(),
                addProductInCartDTO.getProductId());

        if(cartItems == null){
            throw new ValidationException("Cart Is Empty");
        }
        cartItems.setQuantity(cartItems.getQuantity() - 1);
              CartItems savedCartItems = cartItemRepository.save(cartItems);
        return mapper.getCartDTO(cartItems);
    }


    // Place Order
    @Override
    public OrderDTO placeOrder(PlaceOrderDTO placeOrderDTO) {
        User user = userRepository.findById(placeOrderDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get the user's cart items that are not yet part of an order
        List<CartItems> cartItems = cartItemRepository.findByUserIdAndOrderIsNull(placeOrderDTO.getUserId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }
        //findByCode is Optional of coupon(orElse(null))
        //to allow placing order without forcing passing a code for a coupon
        Coupon coupon = couponRepository.findByCode(placeOrderDTO.getCouponCode())
                .orElse(null);
        // Calculate total amount
        Long totalAmount = cartItems.stream()
                .mapToLong(item -> item.getPrice() * item.getQuantity())
                .sum();

        //Calculate the Amount
        Long amount = cartItems.stream()
                .mapToLong(CartItems::getQuantity)
                .sum();

        //apply coupon if provided and update the totalAmount
        if(placeOrderDTO.getCouponCode() != null && !placeOrderDTO.getCouponCode().isEmpty()){
            OrderSummary orderSummary = applyCoupon(placeOrderDTO.getUserId(), placeOrderDTO.getCouponCode());
            totalAmount = orderSummary.getTotalPrice();
        }


        // Create new order
        Order order = new Order();
        order.setUser(user);
        order.setDate(new Date());
        order.setOrderDescription(placeOrderDTO.getOrderDescription());
        order.setAddress(placeOrderDTO.getAddress());
        order.setOrderStatus(OrderStatus.PLACED);
        order.setTrackingId(UUID.randomUUID());
        order.setTotalAmount(totalAmount);
        order.setAmount(amount);
        order.setCoupon(coupon);
        if(coupon != null) {
            order.setDiscount(coupon.getDiscount());
        }
        // Save the order
        order = orderRepository.save(order);

        // Associate cart items with the order
        for (CartItems item : cartItems) {
            if(item.getOrder() == null){
                item.setOrder(order);
                cartItemRepository.save(item);
            }
        }

        // Create OrderDTO
        OrderDTO orderDTO = mapper.getOrderDTO(order);
        // Add cart items to the response
        List<CartItemsDTO> cartItemDTOs = cartItems.stream().map(item -> {
            CartItemsDTO dto = new CartItemsDTO();
            dto.setId(item.getId());
            dto.setPrice(item.getPrice());
            dto.setQuantity(item.getQuantity());
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setUserId(user.getId());
            dto.setReturnedImg(item.getProduct().getImg());
            return dto;
        }).collect(Collectors.toList());

        orderDTO.setCartItems(cartItemDTOs);

        return orderDTO;
    }


    @Override
    public OrderSummary applyCoupon(Long userId, String code) {
        Optional.ofNullable(userId)
                .filter(id-> id > 0 )
                .orElseThrow(()-> new ValidationException("Invalid id"));

        Coupon coupon = couponRepository.findByCode(code.trim())
                .orElseThrow(() -> new ValidationException("Invalid Code"));
        if(IsCouponExpired(coupon)&& coupon!= null){
            throw new ValidationException("Coupon Is Expired");
        }

        Order order = Optional.ofNullable(orderRepository.findByUserIdAndCouponNotNull(userId))
                .filter(ord-> ord.getCoupon().getId() != coupon.getId())
                .stream()
                .findFirst()
                .orElseThrow(()-> new ValidationException("This user has a coupon applied before and still valid"));

        OrderSummary orderSummary = getOrderSummary(userId);
        if(coupon != null){
            double discountAmount = ((coupon.getDiscount() / 100.0) * orderSummary.getTotalPrice());
            double netAmount = orderSummary.getTotalPrice() - discountAmount;
            orderSummary.setTotalPrice((long)netAmount);
            orderSummary.setSubTotal((long)netAmount);
        }
        return orderSummary;
    }


    //should move to cart service impl
    @Override
    public boolean IsCouponExpired(Coupon coupon) {
        Date currentDate = new Date();
        if(coupon != null){
           return coupon.getExpirationDate() != null && currentDate.after(coupon.getExpirationDate());
        }
        return false;
    }

    @Override
    public List<OrderDTO> findAllMyOrders(Long userId) {
        Optional.ofNullable(userId)
                .filter(id-> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid userId"));
        List<Order> orders = Optional.of(orderRepository.findByUserIdAndOrderStatusIn(
                userId, List.of(OrderStatus.PLACED, OrderStatus.DELIVERED, OrderStatus.SHIPPED)))
                .filter(ord-> !ord.isEmpty())
                .orElseThrow(()-> new ValidationException("No Orders Found"));

        return orders.stream().map(mapper::getOrderDTO).collect(Collectors.toList());
    }





}



