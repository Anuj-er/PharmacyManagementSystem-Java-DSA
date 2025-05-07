package model;

public class CartItem {
    private final String medicineId;
    private final String medicineName;
    private int quantity;
    private final double price;
    private double subtotal;

    public CartItem(String medicineId, String medicineName, int quantity, double price, double subtotal) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.price = price;
        this.subtotal = subtotal;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public String getMedicineName() {
        return medicineName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}