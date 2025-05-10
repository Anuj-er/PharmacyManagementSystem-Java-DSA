package controller;

import model.Customer;
import model.Medicine;
import model.Cart;
import model.Bill;
import data_structure.MyLinkedList;

public class CustomerManager {
    // Add color constants for better interface
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    private final PharmacyManager pharmacyManager;
    private Customer currentCustomer;
    private Cart cart;

    public CustomerManager(PharmacyManager pharmacyManager) {
        this.pharmacyManager = pharmacyManager;
    }

    // Login a customer with ID and password
    public boolean login(String customerId, String password) {
        Customer customer = pharmacyManager.findCustomerById(customerId);
        if (customer != null && customer.getPassword().equals(password)) {
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
            System.out.println(RED + "\nNo medicines found matching '" + name + "'" + RESET);
            return;
        }

        // Calculate column widths
        int idWidth = 8;
        int nameWidth = 25;
        int manufacturerWidth = 20;
        int priceWidth = 10;
        int expiryWidth = 12;
        int quantityWidth = 10;

        // Print header
        System.out.println(CYAN + BOLD + "\n" + "=".repeat(95));
        System.out.println("Search Results for '" + name + "'");
        System.out.println("=".repeat(95));
        System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + manufacturerWidth + "s | %-" + 
            priceWidth + "s | %-" + expiryWidth + "s | %-" + quantityWidth + "s\n", 
            "ID", "Name", "Manufacturer", "Price", "Expiry", "Stock");
        System.out.println("=".repeat(95) + RESET);

        // Print medicines
        for (int i = 0; i < results.size(); i++) {
            Medicine med = results.get(i);
            String price = String.format("$%.2f", med.getPrice());
            String stock = med.getQuantity() < 10 ? 
                RED + med.getQuantity() + RESET : 
                GREEN + med.getQuantity() + RESET;

            System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + manufacturerWidth + "s | %-" + 
                priceWidth + "s | %-" + expiryWidth + "s | %-" + quantityWidth + "s\n",
                med.getId(),
                med.getName(),
                med.getManufacturer(),
                price,
                med.getExpiryDate(),
                stock);
        }

        // Print footer
        System.out.println(CYAN + BOLD + "=".repeat(95) + RESET);
        
        // Print legend
        System.out.println(YELLOW + "\nLegend:" + RESET);
        System.out.println(RED + "● " + RESET + "Low Stock (< 10 units)");
        System.out.println(GREEN + "● " + RESET + "Adequate Stock (≥ 10 units)");
    }

    // Display available medicines
    public void viewAvailableMedicines() {
        MyLinkedList<Medicine> medicines = pharmacyManager.getMedicines();
        if (medicines.isEmpty()) {
            System.out.println(RED + "\nNo medicines found." + RESET);
            return;
        }

        // Calculate column widths
        int idWidth = 8;
        int nameWidth = 25;
        int manufacturerWidth = 20;
        int priceWidth = 10;
        int expiryWidth = 12;
        int quantityWidth = 10;

        // Print header
        System.out.println(CYAN + BOLD + "\n" + "=".repeat(95));
        System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + manufacturerWidth + "s | %-" + 
            priceWidth + "s | %-" + expiryWidth + "s | %-" + quantityWidth + "s\n", 
            "ID", "Name", "Manufacturer", "Price", "Expiry", "Stock");
        System.out.println("=".repeat(95) + RESET);

        // Print medicines
        for (int i = 0; i < medicines.size(); i++) {
            Medicine med = medicines.get(i);
            String price = String.format("$%.2f", med.getPrice());
            String stock = med.getQuantity() < 10 ? 
                RED + med.getQuantity() + RESET : 
                GREEN + med.getQuantity() + RESET;

            System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + manufacturerWidth + "s | %-" + 
                priceWidth + "s | %-" + expiryWidth + "s | %-" + quantityWidth + "s\n",
                med.getId(),
                med.getName(),
                med.getManufacturer(),
                price,
                med.getExpiryDate(),
                stock);
        }

        // Print footer
        System.out.println(CYAN + BOLD + "=".repeat(95) + RESET);
        
        // Print legend
        System.out.println(YELLOW + "\nLegend:" + RESET);
        System.out.println(RED + "● " + RESET + "Low Stock (< 10 units)");
        System.out.println(GREEN + "● " + RESET + "Adequate Stock (≥ 10 units)");
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

    public boolean registerCustomer(String name, String phoneNumber, String email, String password) {
        // Validate inputs
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Error: Name cannot be empty.");
            return false;
        }

        // Validate phone number format and content
        if (phoneNumber == null || !phoneNumber.matches("\\d{10}")) {
            System.out.println("Error: Phone number must be exactly 10 digits.");
            return false;
        }
        
        // Check if phone number contains all zeros
        if (phoneNumber.matches("0{10}")) {
            System.out.println("Error: Phone number cannot be all zeros.");
            return false;
        }

        // Check for duplicate phone number
        MyLinkedList<Customer> existingCustomers = pharmacyManager.findCustomersByPhone(phoneNumber);
        if (!existingCustomers.isEmpty()) {
            System.out.println("Error: Phone number already registered with customer ID: " + 
                existingCustomers.get(0).getCustomerId());
            return false;
        }

        // Validate email format
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            System.out.println("Error: Invalid email format.");
            return false;
        }

        // Check for duplicate email
        MyLinkedList<Customer> existingEmails = pharmacyManager.findCustomersByEmail(email);
        if (!existingEmails.isEmpty()) {
            System.out.println("Error: Email already registered with customer ID: " + 
                existingEmails.get(0).getCustomerId());
            return false;
        }

        if (password == null || password.length() < 6) {
            System.out.println("Error: Password must be at least 6 characters long.");
            return false;
        }

        // Generate a customer ID using PharmacyManager method
        String customerId = pharmacyManager.generateCustomerId();

        Customer newCustomer = new Customer(customerId, name, phoneNumber, email, password);
        pharmacyManager.addCustomer(newCustomer);

        System.out.println("\nCustomer Information:");
        System.out.println(newCustomer);

        // Auto login the new customer
        return login(customerId, password);
    }

    // Check if phone number is already registered
    public boolean isPhoneRegistered(String phoneNumber) {
        return !pharmacyManager.findCustomersByPhone(phoneNumber).isEmpty();
    }

    // Get customer ID by phone number
    public String getCustomerIdByPhone(String phoneNumber) {
        MyLinkedList<Customer> customers = pharmacyManager.findCustomersByPhone(phoneNumber);
        return customers.isEmpty() ? null : customers.get(0).getCustomerId();
    }

    // Check if email is already registered
    public boolean isEmailRegistered(String email) {
        return !pharmacyManager.findCustomersByEmail(email).isEmpty();
    }

    // Get customer ID by email
    public String getCustomerIdByEmail(String email) {
        MyLinkedList<Customer> customers = pharmacyManager.findCustomersByEmail(email);
        return customers.isEmpty() ? null : customers.get(0).getCustomerId();
    }

    // Get cart
    public Cart getCart() {
        return cart;
    }
}
