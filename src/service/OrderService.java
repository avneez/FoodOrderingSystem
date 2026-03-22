package service;

import model.Order;

import java.util.List;

public interface OrderService {
    long placeOrder(long userId, String restaurantName, String itemName, int quantity);
    long placeOrder(long userId, String restaurantName, String itemName, int quantity, String couponCode);
    List<Order> getOrders(long userId);
    void cancelOrder(long orderId);
}
