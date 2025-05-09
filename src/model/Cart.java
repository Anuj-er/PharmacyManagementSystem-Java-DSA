package model;

import data_structure.MyLinkedList;

public class Cart {
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    private final String customerId;
    private final MyLinkedList<CartItem> items;

    public Cart(String customerId) {
        this.customerId = customerId;
        this.items = new MyLinkedList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void addItem(Medicine medicine, int quantity) {
        if (medicine == null) {
            System.out.println("Medicine not found");
            return;
        }

        if (medicine.getQuantity() < quantity) {
            System.out.println("Not enough stock available for " + medicine.getName() +
                    " (Available: " + medicine.getQuantity() + ")");
            return;
        }

        CartItem existingItem = items.find(item -> item.getMedicineId().equals(medicine.getId()));
        if (existingItem != null) {
            int newTotalQty = existingItem.getQuantity() + quantity;
            if (medicine.getQuantity() < newTotalQty) {
                System.out.println("Not enough stock available for this additional quantity. " +
                        "You already have " + existingItem.getQuantity() + " in your cart.");
                return;
            }

            existingItem.setQuantity(newTotalQty);
            existingItem.setSubtotal(existingItem.getQuantity() * medicine.getPrice());
            System.out.println("Updated quantity in cart.");
        } else {
            // Add new item to cart
            CartItem item = new CartItem(
                    medicine.getId(),
                    medicine.getName(),
                    quantity,
                    medicine.getPrice(),
                    quantity * medicine.getPrice()
            );
            items.add(item);
            System.out.println("Added to cart: " + medicine.getName());
        }
    }

    public void removeItem(String medicineId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getMedicineId().equals(medicineId)) {
                items.remove(i);
                System.out.println("Item removed from cart.");
                return;
            }
        }
        System.out.println("Item not found in cart.");
    }

    public void updateItemQuantity(String medicineId, int newQuantity, Medicine medicine) {
        if (medicine == null) {
            System.out.println("Medicine not found");
            return;
        }

        if (newQuantity <= 0) {
            removeItem(medicineId);
            return;
        }

        if (medicine.getQuantity() < newQuantity) {
            System.out.println("Not enough stock available for " + medicine.getName() +
                    " (Available: " + medicine.getQuantity() + ")");
            return;
        }

        CartItem item = items.find(i -> i.getMedicineId().equals(medicineId));
        if (item != null) {
            item.setQuantity(newQuantity);
            item.setSubtotal(newQuantity * item.getPrice());
            System.out.println("Quantity updated.");
        } else {
            System.out.println("Item not found in cart.");
        }
    }

    public void displayCart() {
        if (items.isEmpty()) {
            System.out.println(RED + "\nCart is empty." + RESET);
            return;
        }

        System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
        System.out.println("                        YOUR CART");
        System.out.println("=".repeat(60) + RESET);
        System.out.println(CYAN + BOLD + "-".repeat(60));
        System.out.printf("%-5s %-20s %-10s %-10s %-10s\n", 
            "No.", "Medicine", "Price", "Quantity", "Subtotal");
        System.out.println("-".repeat(60) + RESET);

        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            System.out.printf("%-5d %-20s $%-9.2f %-10d $%-9.2f\n",
                    (i+1),
                    item.getMedicineName(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getSubtotal()
            );
            total += item.getSubtotal();
        }

        System.out.println(CYAN + BOLD + "-".repeat(60) + RESET);
        System.out.printf("%-46s $%-9.2f\n", 
            GREEN + BOLD + "Total:" + RESET, 
            total);
        System.out.println(CYAN + BOLD + "=".repeat(60) + RESET);
    }

    public double getCartTotal() {
        double total = 0;
        for (int i = 0; i < items.size(); i++) {
            total += items.get(i).getSubtotal();
        }
        return total;
    }

    public MyLinkedList<CartItem> getItems() {
        return items;
    }

    public void clearCart() {
        items.clear();
        System.out.println("Cart cleared.");
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}