package com.coding.fitness.services.admin.orders;
import com.coding.fitness.dtos.OrderDTO;
import com.coding.fitness.entity.Coupon;
import com.coding.fitness.entity.Order;
import com.coding.fitness.enums.OrderStatus;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.Mapper;
import com.coding.fitness.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;

    private final Mapper mapper;
    @Override
    public List<OrderDTO> findAllOrders() {
       List<Order> orders = Optional.of(orderRepository.findAllByOrderStatusIn(List.of(OrderStatus.PLACED)))
               .filter(ord-> !ord.isEmpty())
               .orElseThrow(()-> new ValidationException("No Orders Found"));
        return orders.stream()
                .map(mapper::getOrderDTO)
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
     Optional.of(order.get())
             .filter(ord-> !isOrderUpdateExpired(ord))
             .orElseThrow(()-> new ValidationException("Can not update Order, Max Period for update is 24h"));

         order.get().setOrderStatus(OrderStatus.PLACED);
         order.get().setOrderDescription(orderDTO.getOrderDescription());
         order.get().setDate(new Date());
         order.get().setTrackingId(UUID.randomUUID());
         order.get().setAddress(orderDTO.getAddress());

        Order orderDB = orderRepository.save(order.get());

        return mapper.getOrderDTO(orderDB);
    }

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

    @Override
    public OrderDTO changeOrderStatus(Long orderId, String orderStatus) {
        Optional.ofNullable(orderId)
                .filter(id-> id > 0)
                .orElseThrow(()-> new ValidationException("Invalid order id"));

        String trimmedStatus = Optional.ofNullable(orderStatus)
                .map(String::trim)
                .filter(status -> !status.isEmpty())
                .orElseThrow(() -> new ValidationException("Invalid order status"));

          Order order   = orderRepository.findById(orderId)
                  .orElseThrow(()-> new ValidationException("No Order Found"));

           Map<String, Consumer<Order>> statusActions = Map.of(
                "Shipped", ord-> ord.setOrderStatus(OrderStatus.SHIPPED),
                "Delivered", ord-> ord.setOrderStatus(OrderStatus.DELIVERED));

          Optional.ofNullable(statusActions.get(trimmedStatus))
                  .ifPresentOrElse(
                          action-> action.accept(order),
                          ()-> {throw new ValidationException("Invalid order status Too");}
                  );

        Order dbOrder = orderRepository.save(order);
        return mapper.getOrderDTO(dbOrder);
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
