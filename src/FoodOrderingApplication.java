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

public class FoodOrderingApplication {

    public static void main(String[] args) {
        UserService userService = new InMemoryUserService();
        RestaurantService restaurantService = new InMemoryRestaurantService();
        OrderService orderService = new InMemoryOrderService(userService, restaurantService);

        long donaldId = restaurantService.restaurantRegistration(
                "Donald", "GST10905884580", "donald@mail.com", "1234567890");
        System.out.println("Log:-");
        System.out.println("a. Registered!!! RestaurantId:" + donaldId);

        long avneezId = restaurantService.restaurantRegistration(
                "Avneez", "GST10905884580", "avneez@mail.com", "1234567890");
        System.out.println("Log:-");
        System.out.println("A new restaurant Registered!!! RestaurantId:" + avneezId);

        restaurantService.addItemsInCatalog("Donald", "Sandwich", new BigDecimal("100.00"), 4);
        restaurantService.addItemsInCatalog("Donald", "Burger", new BigDecimal("250.00"), 2);
        restaurantService.addItemsInCatalog("Donald", "Pizza", new BigDecimal("500.00"), 10);
        restaurantService.addItemsInCatalog("Avneez", "Pasta", new BigDecimal("500.00"), 10);

        List<String> catalogNames = restaurantService.getItemNames("Donald");
        System.out.println(catalogNames);
        List<String> catalogNames2 = restaurantService.getItemNames("Avneez");
        System.out.println(catalogNames2);

        long userId = userService.userRegistration("User1", "user@mail.com", "1234567890");
        System.out.println();
        System.out.println("Log:- User Registered!!!  UserId: " + userId);

        MenuItem searched = restaurantService.searchItem("Donald", "Sandwich");
        System.out.println();
        System.out.println("Search Result:");
        if (searched != null) {
            System.out.println("item, price, quantity");
            System.out.println(searched.getName() + ", " + searched.getPrice() + ", " + searched.getAvailableQuantity());
        } else {
            System.out.println("Item not found");
        }

        long orderId1 = orderService.placeOrder(userId, "Donald", "Sandwich", 2);
        System.out.println();
        System.out.println("Order placed successfully orderId: " + orderId1);

        long orderId2 = orderService.placeOrder(userId, "Donald", "Pizza", 1);
        orderService.cancelOrder(orderId2);

        printOrders(orderService, userId);

        System.out.println();
        System.out.println("Cancel order " + orderId1);
        orderService.cancelOrder(orderId1);
        System.out.println("Order " + orderId1 + " canceled successfully.");
    }

    private static void printOrders(OrderService orderService, long userId) {
        System.out.println();
        System.out.println("Orders for user " + userId + ":");
        System.out.println("orderId, item, quantity, status");
        for (Order order : orderService.getOrders(userId)) {
            System.out.println(order.getId() + ", "
                    + order.getItemName() + ", "
                    + order.getQuantity() + ", "
                    + formatStatus(order.getStatus()));
        }
    }

    private static String formatStatus(OrderStatus status) {
        return switch (status) {
            case CONFIRMED -> "CONFIRMED";
            case CANCELLED -> "CANCELLED";
        };
    }
}
