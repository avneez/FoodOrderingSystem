import model.MenuItem;
import model.Order;
import model.OrderStatus;
import service.OrderService;
import service.RestaurantService;
import service.UserService;
import service.impl.InMemoryOrderService;
import service.impl.InMemoryRestaurantService;
import service.impl.InMemoryUserService;

import java.math.BigDecimal;
import java.util.List;

public class FoodOrderingSystemTest {

    public static void main(String[] args) {
        UserService userService = new InMemoryUserService();
        RestaurantService restaurantService = new InMemoryRestaurantService();
        OrderService orderService = new InMemoryOrderService(userService, restaurantService);

        System.out.println("Running Food Ordering System tests...");

        testUserRegistration(userService);
        testRestaurantRegistration(restaurantService);
        testCatalogAddition(restaurantService);
        testSearchItemsSortedByPrice(restaurantService);
        long userId = testPlaceOrder(userService, restaurantService, orderService);
        testGetOrders(userId, orderService);
        testCancelOrder(userId, restaurantService, orderService);
    }

    private static void testUserRegistration(UserService userService) {
        long userId = userService.userRegistration("Alice", "alice@mail.com", "1111111111");
        boolean passed = userId > 0 && userService.getUser(userId) != null;
        System.out.println("User registration: " + (passed ? "PASS" : "FAIL"));
    }

    private static void testRestaurantRegistration(RestaurantService restaurantService) {
        long restaurantId = restaurantService.restaurantRegistration(
                "TestResto", "GST123", "resto@mail.com", "2222222222");
        boolean passed = restaurantId > 0 && restaurantService.getByName("TestResto") != null;
        System.out.println("Restaurant registration: " + (passed ? "PASS" : "FAIL"));
    }

    private static void testCatalogAddition(RestaurantService restaurantService) {
        restaurantService.restaurantRegistration("CatalogResto", "GST456", "cat@mail.com", "3333333333");
        restaurantService.addItemsInCatalog("CatalogResto", "ItemA", new BigDecimal("10.00"), 5);

        MenuItem item = restaurantService.searchItem("CatalogResto", "ItemA");
        boolean passed = item != null
                && item.getPrice().compareTo(new BigDecimal("10.00")) == 0
                && item.getAvailableQuantity() == 5;
        System.out.println("Catalog addition: " + (passed ? "PASS" : "FAIL"));
    }

    private static void testSearchItemsSortedByPrice(RestaurantService restaurantService) {
        restaurantService.restaurantRegistration("PriceResto", "GST789", "price@mail.com", "4444444444");
        restaurantService.addItemsInCatalog("PriceResto", "Cheap", new BigDecimal("50.00"), 10);
        restaurantService.addItemsInCatalog("PriceResto", "Medium", new BigDecimal("100.00"), 10);
        restaurantService.addItemsInCatalog("PriceResto", "Expensive", new BigDecimal("150.00"), 10);

        List<String> names = restaurantService.getItemNames("PriceResto");
        boolean passed = names.size() == 3
                && "Cheap".equals(names.get(0))
                && "Medium".equals(names.get(1))
                && "Expensive".equals(names.get(2));
        System.out.println("Search items sorted by price: " + (passed ? "PASS" : "FAIL"));
    }

    private static long testPlaceOrder(UserService userService,
                                       RestaurantService restaurantService,
                                       OrderService orderService) {
        long userId = userService.userRegistration("Bob", "bob@mail.com", "5555555555");
        restaurantService.restaurantRegistration("OrderResto", "GST999", "order@mail.com", "6666666666");
        restaurantService.addItemsInCatalog("OrderResto", "Burger", new BigDecimal("80.00"), 3);

        long orderId = orderService.placeOrder(userId, "OrderResto", "Burger", 2);
        boolean passed = orderId > 0;
        System.out.println("Place order: " + (passed ? "PASS" : "FAIL"));
        return userId;
    }

    private static void testGetOrders(long userId, OrderService orderService) {
        List<Order> orders = orderService.getOrders(userId);
        boolean passed = !orders.isEmpty();
        System.out.println("Get orders for user: " + (passed ? "PASS" : "FAIL"));
    }

    private static void testCancelOrder(long userId,
                                        RestaurantService restaurantService,
                                        OrderService orderService) {
        restaurantService.restaurantRegistration("CancelResto", "GST777", "cancel@mail.com", "7777777777");
        restaurantService.addItemsInCatalog("CancelResto", "Pizza", new BigDecimal("120.00"), 5);

        long orderId = orderService.placeOrder(userId, "CancelResto", "Pizza", 1);
        orderService.cancelOrder(orderId);

        List<Order> orders = orderService.getOrders(userId);
        Order found = null;
        for (Order order : orders) {
            if (order.getId() == orderId) {
                found = order;
                break;
            }
        }

        boolean passed = found != null && found.getStatus() == OrderStatus.CANCELLED;
        System.out.println("Cancel order: " + (passed ? "PASS" : "FAIL"));
    }
}
