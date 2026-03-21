package model;

public class Order {
    private final long id;
    private final long userId;
    private final String restaurantName;
    private final String itemName;
    private final int quantity;
    private OrderStatus status;

    public Order(long id,
                 long userId,
                 String restaurantName,
                 String itemName,
                 int quantity,
                 OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.restaurantName = restaurantName;
        this.itemName = itemName;
        this.quantity = quantity;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }
}
