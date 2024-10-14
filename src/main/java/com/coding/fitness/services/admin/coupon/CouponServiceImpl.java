package com.coding.fitness.services.admin.coupon;

import com.coding.fitness.dtos.CouponDTO;
import com.coding.fitness.dtos.OrderSummary;
import com.coding.fitness.entity.Coupon;
import com.coding.fitness.entity.User;
import com.coding.fitness.enums.UserRole;
import com.coding.fitness.exceptions.ValidationException;
import com.coding.fitness.repository.CouponRepository;
import com.coding.fitness.services.customer.cart.CartService;
import com.coding.fitness.tokens.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements  CouponService{


    private final CouponRepository couponRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Coupon createCoupon(CouponDTO couponDTO) {
        if(couponRepository.existsByCode(couponDTO.getCode())){
            throw new ValidationException("Coupon Is Already Exists");
        }
        //new update to prevent the JWT for a customer to create a coupon
        User user = jwtUtils.getLoggedInUser();
        if(user.getRole() == UserRole.ADMIN) {
            Coupon coupon = new Coupon();
            coupon.setName(couponDTO.getName());
            coupon.setCode(couponDTO.getCode());
            coupon.setDiscount(couponDTO.getDiscount());
            coupon.setExpirationDate(couponDTO.getExpirationDate());
            return couponRepository.save(coupon);
        } else {
            throw new ValidationException("Not Authorized");
        }

    }

    @Override
    public List<CouponDTO> findAll() {
        User user = jwtUtils.getLoggedInUser();
        if(user.getRole() != UserRole.ADMIN){
            throw new ValidationException("Not Authorized User");
        }
        return couponRepository.findAll().stream()
                .map(Coupon::getCouponDTO)
                .collect(Collectors.toList());
    }


    //should move to cart service impl


}