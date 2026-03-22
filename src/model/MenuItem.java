package model;

public class MenuItem {
    private final String name;
    private final double price;
    private int availableQuantity;

    public MenuItem(String name, double price, int availableQuantity) {
        this.name = name;
        this.price = price;
        this.availableQuantity = availableQuantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void reduceQuantity(int quantity) {
        if (quantity > availableQuantity) {
            throw new IllegalArgumentException("Insufficient quantity for item " + name);
        }
        this.availableQuantity -= quantity;
    }

    public void increaseQuantity(int quantity) {
        this.availableQuantity += quantity;
    }
}
