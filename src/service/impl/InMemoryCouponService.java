package service.impl;

import model.Coupon;
import service.CouponService;

import java.util.HashMap;
import java.util.Map;

public class InMemoryCouponService implements CouponService {

    private final Map<String, Coupon> couponsByCode = new HashMap<>();

    @Override
    public void addCoupon(String code, double discountPercentage) {
        couponsByCode.put(code, new Coupon(code, discountPercentage));
    }

    @Override
    public Coupon getCoupon(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        return couponsByCode.get(code);
    }
}
