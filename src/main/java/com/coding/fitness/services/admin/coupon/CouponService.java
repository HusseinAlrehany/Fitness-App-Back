package com.coding.fitness.services.admin.coupon;

import com.coding.fitness.dtos.CouponDTO;
import com.coding.fitness.dtos.OrderSummary;
import com.coding.fitness.entity.Coupon;

import java.util.List;

public interface CouponService {
    Coupon createCoupon(CouponDTO couponDTO);

    List<CouponDTO> findAll();

}
