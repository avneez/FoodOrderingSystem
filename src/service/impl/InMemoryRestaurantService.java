package service.impl;

import model.MenuItem;
import model.Restaurant;
import service.RestaurantService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryRestaurantService implements RestaurantService {

    private final Map<Long, Restaurant> restaurantsById = new HashMap<>();
    private final Map<String, Restaurant> restaurantsByName = new HashMap<>();
    private long nextRestaurantId = 1;

    @Override
    public long restaurantRegistration(String restaurantName, String gstNumber, String emailId, String phoneNumber) {
        if (restaurantsByName.containsKey(restaurantName)) {
            throw new IllegalArgumentException("Restaurant already registered: " + restaurantName);
        }
        long id = nextRestaurantId++;
        Restaurant restaurant = new Restaurant(id, restaurantName, gstNumber, emailId, phoneNumber);
        restaurantsById.put(id, restaurant);
        restaurantsByName.put(restaurantName, restaurant);
        return id;
    }

    @Override
    public void addItemsInCatalog(String restaurantName, String itemName, BigDecimal price, int quantity) {
        Restaurant restaurant = getByName(restaurantName);
        restaurant.addOrUpdateItem(itemName, price, quantity);
    }

    @Override
    public MenuItem searchItem(String restaurantName, String itemName) {
        Restaurant restaurant = getByName(restaurantName);
        return restaurant.getItem(itemName);
    }

    @Override
    public Restaurant getByName(String restaurantName) {
        Restaurant restaurant = restaurantsByName.get(restaurantName);
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant not found: " + restaurantName);
        }
        return restaurant;
    }

    @Override
    public List<String> getItemNames(String restaurantName) {
        Restaurant restaurant = getByName(restaurantName);
        List<MenuItem> items = new ArrayList<>(restaurant.getAllItems());
        items.sort((a, b) -> a.getPrice().compareTo(b.getPrice()));
        List<String> names = new ArrayList<>();
        for (MenuItem item : items) {
            names.add(item.getName());
        }
        return names;
    }
}
