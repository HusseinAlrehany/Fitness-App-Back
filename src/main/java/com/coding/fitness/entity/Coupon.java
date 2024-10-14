package com.coding.fitness.entity;

import com.coding.fitness.dtos.CouponDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "coupons")
@Data
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    private Long discount;
    private Date expirationDate;


    public CouponDTO getCouponDTO(){
        CouponDTO couponDTO = new CouponDTO();
        couponDTO.setId(id);
        couponDTO.setCode(code);
        couponDTO.setName(name);
        couponDTO.setDiscount(discount);
        couponDTO.setExpirationDate(expirationDate);

        return couponDTO;
    }

}
