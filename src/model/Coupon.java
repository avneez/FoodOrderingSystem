package model;

public class Coupon {
    private final String code;
    private final double discountPercentage;

    public Coupon(String code, double discountPercentage) {
        this.code = code;
        this.discountPercentage = discountPercentage;
    }

    public String getCode() {
        return code;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }
}
