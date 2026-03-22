package service;

import model.MenuItem;
import model.Restaurant;

import java.util.List;

public interface RestaurantService {
    long restaurantRegistration(String restaurantName, String gstNumber, String emailId, String phoneNumber);
    void addItemsInCatalog(String restaurantName, String itemName, double price, int quantity);
    MenuItem searchItem(String restaurantName, String itemName);
    Restaurant getByName(String restaurantName);
    List<String> getItemNames(String restaurantName);
}
