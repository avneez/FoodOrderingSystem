package model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Restaurant {
    private final long id;
    private final String name;
    private final String gstNumber;
    private final String email;
    private final String phone;
    private final Map<String, MenuItem> catalog = new LinkedHashMap<>();

    public Restaurant(long id, String name, String gstNumber, String email, String phone) {
        this.id = id;
        this.name = name;
        this.gstNumber = gstNumber;
        this.email = email;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuItem addOrUpdateItem(String itemName, java.math.BigDecimal price, int quantity) {
        MenuItem existing = catalog.get(itemName);
        if (existing == null) {
            existing = new MenuItem(itemName, price, quantity);
            catalog.put(itemName, existing);
        } else {
            existing.increaseQuantity(quantity);
        }
        return existing;
    }

    public MenuItem getItem(String itemName) {
        return catalog.get(itemName);
    }

    public Collection<MenuItem> getAllItems() {
        return catalog.values();
    }
}
