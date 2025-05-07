package controller;

import model.Customer;
import model.Medicine;
import model.Cart;
import model.Bill;
import data_structure.MyLinkedList;

public class CustomerManager {
    private final PharmacyManager pharmacyManager;
    private Customer currentCustomer;
    private Cart cart;

    public CustomerManager(PharmacyManager pharmacyManager) {
        this.pharmacyManager = pharmacyManager;
    }

    // Login a customer with ID
    public boolean login(String customerId) {
        Customer customer = pharmacyManager.findCustomerById(customerId);
        if (customer != null) {
            this.currentCustomer = customer;
            this.cart = new Cart(customerId);
            return true;
        }
        return false;
    }

    // Check if customer is logged in
    public boolean isLoggedIn() {
        return currentCustomer != null;
    }

    // Get current customer
    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    // Logout current customer
    public void logout() {
        currentCustomer = null;
        cart = null;
        System.out.println("Logged out successfully.");
    }

    // Search for medicines by name
    public void searchMedicinesByName(String name) {
        MyLinkedList<Medicine> results = pharmacyManager.findMedicinesByName(name);
        if (results.isEmpty()) {
            System.out.println("No medicines found matching '" + name + "'");
            return;
        }

        System.out.println("\n===== Search Results for '" + name + "' =====");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i+1) + ". " + results.get(i));
        }
    }

    // Display available medicines
    public void viewAvailableMedicines() {
        System.out.println("\n===== Available Medicines =====");
        System.out.printf("%-5s %-10s %-20s %-15s %-10s %-10s\n",
                "No.", "ID", "Name", "Manufacturer", "Price", "Stock");
        System.out.println("--------------------------------------------------------------------");
        pharmacyManager.showAllMedicines();
    }

    public void addToCart(String medicineId, int quantity) {
        if (!isLoggedIn()) {
            System.out.println("Please login first.");
            return;
        }

        Medicine medicine = pharmacyManager.findMedicineById(medicineId);
        if (medicine == null) {
            System.out.println("Medicine not found.");
            return;
        }

        cart.addItem(medicine, quantity);
    }

    // View current cart
    public void viewCart() {
        if (!isLoggedIn()) {
            System.out.println("Please login first.");
            return;
        }

        cart.displayCart();
    }

    // Update quantity of an item in cart
    public void updateCartItemQuantity(String medicineId, int newQuantity) {
        if (!isLoggedIn()) {
            System.out.println("Please login first.");
            return;
        }

        Medicine medicine = pharmacyManager.findMedicineById(medicineId);
        if (medicine == null) {
            System.out.println("Medicine not found.");
            return;
        }

        cart.updateItemQuantity(medicineId, newQuantity, medicine);
    }

    // Remove item from cart
    public void removeFromCart(String medicineId) {
        if (!isLoggedIn()) {
            System.out.println("Please login first.");
            return;
        }

        cart.removeItem(medicineId);
    }

    // Clear cart
    public void clearCart() {
        if (!isLoggedIn()) {
            System.out.println("Please login first.");
            return;
        }

        cart.clearCart();
    }

    // Checkout and generate bill
    public Bill checkout() {
        if (!isLoggedIn()) {
            System.out.println("Please login first.");
            return null;
        }

        if (cart.isEmpty()) {
            System.out.println("Your cart is empty. Nothing to checkout.");
            return null;
        }

        Bill bill = pharmacyManager.processCheckout(cart, currentCustomer);
        if (bill != null) {
            bill.displayBill();
            clearCart(); // Clear cart after successful checkout
        }

        return bill;
    }

    public boolean registerCustomer(String name, String phoneNumber, String email) {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Name cannot be empty.");
            return false;
        }

        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            System.out.println("Error: Phone number must be 10 digits.");
            return false;
        }

        if (email == null || !email.contains("@") || !email.contains(".")) {
            System.out.println("Error: Invalid email format.");
            return false;
        }

        // Generate a customer ID using PharmacyManager method
        String customerId = pharmacyManager.generateCustomerId();

        Customer newCustomer = new Customer(customerId, name, phoneNumber, email);
        pharmacyManager.addCustomer(newCustomer);

        System.out.println("\nCustomer Information:");
        System.out.println(newCustomer);

        // Auto login the new customer
        return login(customerId);
    }

    // Get cart
    public Cart getCart() {
        return cart;
    }
}
