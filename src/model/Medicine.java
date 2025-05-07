package model;

public class Medicine {
    private final String id;
    private final String name;
    private final String manufacturer;
    private double price;
    private final String expiryDate; // Format: YYYY-MM-DD
    private int quantity;

    // Constructor
    public Medicine(String id, String name, String manufacturer, double price, String expiryDate, int quantity) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.price = price;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public double getPrice() {
        return price;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // toString method for easy display
    @Override
    public String toString() {
        return String.format(
                "ID: %s | Name: %s | Manufacturer: %s | Price: ₹%.2f | Expiry: %s | Quantity: %d",
                id, name, manufacturer, price, expiryDate, quantity
        );
    }
}
