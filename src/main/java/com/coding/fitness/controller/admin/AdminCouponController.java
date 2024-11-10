package com.coding.fitness.controller.admin;

import com.coding.fitness.dtos.CouponDTO;
import com.coding.fitness.dtos.OrderSummary;
import com.coding.fitness.entity.Coupon;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.mapper.Mapper;
import com.coding.fitness.services.admin.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    private final CouponService couponService;

    private final Mapper mapper;

    @PostMapping
    public ResponseEntity<?> createCoupon(@RequestBody CouponDTO couponDTO) {
        try {
            Coupon createdCoupon =  couponService.createCoupon(couponDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(mapper.getCouponDTO(createdCoupon));
        }
        catch(ValidationException ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ex.getMessage());
        }
    }
    @GetMapping
    public ResponseEntity<List<CouponDTO>> findAllCoupons(){
         return ResponseEntity.ok(couponService.findAll());
    }

}
