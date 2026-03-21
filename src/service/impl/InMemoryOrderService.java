package service.impl;

import model.MenuItem;
import model.Order;
import model.OrderStatus;
import service.OrderService;
import service.RestaurantService;
import service.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryOrderService implements OrderService {

    private final Map<Long, Order> ordersById = new HashMap<>();
    private final Map<Long, List<Order>> ordersByUserId = new HashMap<>();
    private long nextOrderId = 1;

    private final UserService userService;
    private final RestaurantService restaurantService;

    public InMemoryOrderService(UserService userService, RestaurantService restaurantService) {
        this.userService = userService;
        this.restaurantService = restaurantService;
    }

    @Override
    public long placeOrder(long userId, String restaurantName, String itemName, int quantity) {
        userService.getUser(userId);
        MenuItem item = restaurantService.searchItem(restaurantName, itemName);
        if (item == null) {
            throw new IllegalArgumentException("Item not found: " + itemName + " at " + restaurantName);
        }
        item.reduceQuantity(quantity);

        long id = nextOrderId++;
        Order order = new Order(id, userId, restaurantName, itemName, quantity, OrderStatus.CONFIRMED);
        ordersById.put(id, order);

        List<Order> userOrders = ordersByUserId.get(userId);
        if (userOrders == null) {
            userOrders = new ArrayList<>();
            ordersByUserId.put(userId, userOrders);
        }
        userOrders.add(order);
        return id;
    }

    @Override
    public List<Order> getOrders(long userId) {
        List<Order> orders = ordersByUserId.get(userId);
        if (orders == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(orders);
    }

    @Override
    public void cancelOrder(long orderId) {
        Order order = ordersById.get(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found: " + orderId);
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return;
        }
        MenuItem item = restaurantService.searchItem(order.getRestaurantName(), order.getItemName());
        if (item != null) {
            item.increaseQuantity(order.getQuantity());
        }
        order.cancel();
    }
}
