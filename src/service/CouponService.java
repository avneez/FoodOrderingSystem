package service;

import model.Coupon;

public interface CouponService {
    void addCoupon(String code, double discountPercentage);
    Coupon getCoupon(String code);
}
