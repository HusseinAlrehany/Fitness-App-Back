package com.coding.fitness.services.admin.orders;
import com.coding.fitness.dtos.OrderDTO;
import com.coding.fitness.entity.Coupon;
import com.coding.fitness.entity.Order;
import com.coding.fitness.enums.OrderStatus;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;
    @Override
    public List<OrderDTO> findAllOrders() {
       List<Order> orders = Optional.of(orderRepository.findAllByOrderStatusIn(List.of(OrderStatus.PLACED)))
               .filter(ord-> !ord.isEmpty())
               .orElseThrow(()-> new ValidationException("No Orders Found for That Status"));
        return orders.stream()
                .map(order -> {
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
                    //this line is for getting the order
                    //even if it did not have coupon or coupon is null
                    //to avoid nullpointerexception
                    //so if the coupon not null it will go to coupon.getDiscount
                    //but if it is null it will set to null and retrieved
                    orderDTO.setDiscount(Optional.ofNullable(order.getCoupon())
                            .map(Coupon::getDiscount)
                            .orElse(null));
                    return orderDTO;
                })
                .collect(Collectors.toList());

    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        Optional.ofNullable(orderDTO.getId())
                .filter(id-> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid OrderId"));
      Optional <Order> order =  Optional.of(orderRepository.findById(orderDTO.getId()))
                .filter(Optional::isPresent)
                .orElseThrow(()-> new ValidationException("No Order Found"));

    if(isOrderUpdateExpired(order.get())){
         throw new ValidationException("Can not update Order, Max Period for update is 24h");
     }

         Coupon coupon = order.get().getCoupon();
         order.get().setOrderStatus(OrderStatus.PLACED);
         order.get().setOrderDescription(orderDTO.getOrderDescription());
         order.get().setDate(new Date());
         order.get().setTrackingId(UUID.randomUUID());
         order.get().setAddress(orderDTO.getAddress());

        Order orderDB = orderRepository.save(order.get());
        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.setId(orderDB.getId());
        orderDTO1.setOrderDescription(orderDB.getOrderDescription());
        orderDTO1.setOrderStatus(orderDB.getOrderStatus());
        orderDTO1.setAmount(orderDB.getAmount());
        orderDTO1.setTotalAmount(orderDB.getTotalAmount());
        orderDTO1.setDate(orderDB.getDate());
        orderDTO1.setAddress(orderDB.getAddress());
        orderDTO1.setTrackingId(orderDB.getTrackingId());
        orderDTO1.setUserName(orderDB.getUser().getName());
        orderDTO1.setUserId(orderDB.getUser().getId());
        orderDTO1.setDiscount(Optional.ofNullable(orderDB.getCoupon())
                .map(Coupon::getDiscount)
                .orElse(null));
        return orderDTO1;
    }

   /* public OrderDTO mapToOrderDTO(Order order){

        return OrderDTO.builder()
                .id(order.getId())
                .date(order.getDate())
                .trackingId(order.getTrackingId())
                .orderStatus(order.getOrderStatus())
                .orderDescription(order.getOrderDescription())
                .address(order.getAddress())
                .userId(order.getUser().getId())
                .totalAmount(order.getTotalAmount())
                .amount(order.getAmount())
                .userName(order.getUser().getName())
                .discount(Optional.ofNullable(order.getCoupon())
                        .map(Coupon::getDiscount)
                        .orElse(null))
                .build();
    }*/

    @Override
    public boolean isOrderUpdateExpired(Order order) {
        Date currentDate = new Date();
        Date orderUpdateExpiration = order.getDate();

        return orderUpdateExpiration != null && currentDate.after(orderUpdateExpiration);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Optional.ofNullable(orderId)
                .filter(id -> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid Id"));
        Optional.of(orderRepository.findById(orderId))
                .filter(ord-> ord.isPresent())
                .orElseThrow(()-> new ValidationException("No Order Found"));

         orderRepository.deleteById(orderId);
    }

    /*@Override
    public boolean isOrderUpdateExpired(Order order) {
        int expirationPeriodInHours = 24; // Define the expiration period
        Date orderDate = order.getDate();

        if (orderDate == null) {
            return true; // Treat null dates as expired
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(orderDate);
        calendar.add(Calendar.HOUR, expirationPeriodInHours);

        Date expirationDate = calendar.getTime();
        Date currentDate = new Date();

        return currentDate.after(expirationDate); // True if the current date is after the expiration date
    }*/



}
