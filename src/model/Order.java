package model;

public class Order {
    private final long id;
    private final long userId;
    private final String restaurantName;
    private final String itemName;
    private final int quantity;
    private final double itemPrice;
    private final String couponCode;
    private final double discountPercentage;
    private final double totalPrice;
    private final double finalPrice;
    private OrderStatus status;

    public Order(long id,
                 long userId,
                 String restaurantName,
                 String itemName,
                 int quantity,
                 double itemPrice,
                 String couponCode,
                 double discountPercentage,
                 double totalPrice,
                 double finalPrice,
                 OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.restaurantName = restaurantName;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.couponCode = couponCode;
        this.discountPercentage = discountPercentage;
        this.totalPrice = totalPrice;
        this.finalPrice = finalPrice;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }
}
