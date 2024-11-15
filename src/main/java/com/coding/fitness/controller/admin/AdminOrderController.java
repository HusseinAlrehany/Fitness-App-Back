package com.coding.fitness.controller.admin;

import com.coding.fitness.dtos.OrderDTO;
import com.coding.fitness.services.admin.orders.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> findAllOrders(){
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminOrderService.findAllOrders());
    }

    @PatchMapping ("/orders")
    public ResponseEntity<OrderDTO> updateOrder(@RequestBody OrderDTO orderDTO){
          return ResponseEntity.ok(adminOrderService.updateOrder(orderDTO));
    }
    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId){
        adminOrderService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PatchMapping("/order/{orderId}/{orderStatus}")
    public ResponseEntity<?> changeOrderStatus(@PathVariable Long orderId, @PathVariable String orderStatus){

        OrderDTO orderDTO = adminOrderService.changeOrderStatus(orderId, orderStatus);

        if(orderDTO == null){
            return new ResponseEntity<>("Something went wrong!", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(orderDTO);
    }



}
