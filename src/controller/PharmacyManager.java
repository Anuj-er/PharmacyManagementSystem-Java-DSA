package controller;

import model.*;
import util.CSVUtility;
import data_structure.MyLinkedList;

import java.time.LocalDate;
import java.util.UUID;

public class PharmacyManager {
    // Add color constants for better interface
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    private final MyLinkedList<Medicine> medicines;
    private final MyLinkedList<Customer> customers;
    private final MyLinkedList<Transaction> transactions;
    private final MyLinkedList<Bill> bills;

    // Constructor: loads medicines, customers, and transactions from CSV files
    public PharmacyManager() {
        medicines = CSVUtility.loadMedicines("data/medicines.csv");
        customers = CSVUtility.loadCustomers("data/customers.csv");
        transactions = CSVUtility.loadTransactions("data/transactions.csv");
        bills = new MyLinkedList<>();
    }

    // Get medicines list
    public MyLinkedList<Medicine> getMedicines() {
        return medicines;
    }

    // Show all medicines
    public void showAllMedicines() {
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

    // Show all customers
    public void showAllCustomers() {
        if (customers.isEmpty()) {
            System.out.println(RED + "\nNo customers found." + RESET);
            return;
        }

        // Calculate column widths
        int idWidth = 8;
        int nameWidth = 25;
        int phoneWidth = 15;
        int emailWidth = 30;

        // Print header
        System.out.println(CYAN + BOLD + "\n" + "=".repeat(85));
        System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + phoneWidth + "s | %-" + emailWidth + "s\n", 
            "ID", "Name", "Phone", "Email");
        System.out.println("=".repeat(85) + RESET);

        // Print customers
        for (int i = 0; i < customers.size(); i++) {
            Customer cust = customers.get(i);
            System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + phoneWidth + "s | %-" + emailWidth + "s\n",
                cust.getCustomerId(),
                cust.getName(),
                cust.getPhoneNumber(),
                cust.getEmail());
        }

        // Print footer
        System.out.println(CYAN + BOLD + "=".repeat(85) + RESET);
    }

    // Show all transactions
    public void showAllTransactions() {
        if (transactions.isEmpty()) {
            System.out.println(RED + "\nNo transactions found." + RESET);
            return;
        }

        // Calculate column widths
        int transIdWidth = 12;
        int customerIdWidth = 10;
        int medicineIdWidth = 10;
        int quantityWidth = 10;
        int amountWidth = 12;
        int dateWidth = 12;

        // Print header
        System.out.println(CYAN + BOLD + "\n" + "=".repeat(80));
        System.out.printf("%-" + transIdWidth + "s | %-" + customerIdWidth + "s | %-" + medicineIdWidth + "s | %-" + 
            quantityWidth + "s | %-" + amountWidth + "s | %-" + dateWidth + "s\n", 
            "Transaction ID", "Customer ID", "Medicine ID", "Quantity", "Amount", "Date");
        System.out.println("=".repeat(80) + RESET);

        // Print transactions
        for (int i = 0; i < transactions.size(); i++) {
            Transaction tx = transactions.get(i);
            String amount = String.format("$%.2f", tx.getTotalAmount());
            System.out.printf("%-" + transIdWidth + "s | %-" + customerIdWidth + "s | %-" + medicineIdWidth + "s | %-" + 
                quantityWidth + "d | %-" + amountWidth + "s | %-" + dateWidth + "s\n",
                tx.getTransactionId(),
                tx.getCustomerId(),
                tx.getMedicineId(),
                tx.getQuantity(),
                amount,
                tx.getTransactionDate());
        }

        // Print footer
        System.out.println(CYAN + BOLD + "=".repeat(80) + RESET);
    }

    // Find a medicine by ID
    public Medicine findMedicineById(String id) {
        return medicines.find(med -> med.getId().equalsIgnoreCase(id));
    }

    // Find a medicine by name (partial match)
    public MyLinkedList<Medicine> findMedicinesByName(String name) {
        MyLinkedList<Medicine> results = new MyLinkedList<>();
        for (int i = 0; i < medicines.size(); i++) {
            Medicine med = medicines.get(i);
            if (med.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(med);
            }
        }
        return results;
    }

    // Add a new medicine
    public void addMedicine(Medicine med) {
        if (findMedicineById(med.getId()) != null) {
            System.out.println("Medicine with ID " + med.getId() + " already exists.");
            return;
        }
        medicines.add(med);
        CSVUtility.saveMedicines("data/medicines.csv", medicines);
        System.out.println("Medicine added successfully.");
    }

    // Add this method to generate sequential medicine ID
    public String generateMedicineId() {
        int highestId = 0;
        for (int i = 0; i < medicines.size(); i++) {
            String id = medicines.get(i).getId();
            if (id.startsWith("MED")) {
                try {
                    int idNum = Integer.parseInt(id.substring(3));
                    highestId = Math.max(highestId, idNum);
                } catch (NumberFormatException e) {
                    // Skip if not numeric
                }
            }
        }
        return "MED" + String.format("%03d", highestId + 1);
    }

    // Remove a medicine by ID
    public void removeMedicineById(String id) {
        for (int i = 0; i < medicines.size(); i++) {
            if (medicines.get(i).getId().equalsIgnoreCase(id)) {
                medicines.remove(i);
                CSVUtility.saveMedicines("data/medicines.csv", medicines);
                System.out.println("Medicine removed successfully.");
                return;
            }
        }
        System.out.println("Medicine with ID " + id + " not found.");
    }

    // Update quantity of a medicine
    public void updateMedicineQuantity(String id, int newQuantity) {
        Medicine med = findMedicineById(id);
        if (med != null) {
            System.out.println("\nMedicine Information:");
            System.out.println(med);
            System.out.println("Current quantity: " + med.getQuantity());
            med.setQuantity(newQuantity);
            CSVUtility.saveMedicines("data/medicines.csv", medicines);
            System.out.println("Quantity updated to: " + newQuantity);
        } else {
            System.out.println("Medicine with ID " + id + " not found.");
        }
    }

    // Update price of a medicine
    public void updateMedicinePrice(String id, double newPrice) {
        Medicine med = findMedicineById(id);
        if (med != null) {
            double oldPrice = med.getPrice();
            System.out.println("Current price: $" + oldPrice);
            med.setPrice(newPrice);
            CSVUtility.saveMedicines("data/medicines.csv", medicines);
            System.out.println("Price updated from $" + oldPrice + " to $" + newPrice);
        } else {
            System.out.println("Medicine with ID " + id + " not found.");
        }
    }

    // Add a new customer
    public void addCustomer(Customer customer) {
        if (findCustomerById(customer.getCustomerId()) != null) {
            System.out.println("Customer with ID " + customer.getCustomerId() + " already exists.");
            return;
        }
        customers.add(customer);
        CSVUtility.saveCustomers("data/customers.csv", customers);
        System.out.println("Customer added successfully.");
    }

    // Add method to generate customer ID
    public String generateCustomerId() {
        int highestId = 0;
        for (int i = 0; i < customers.size(); i++) {
            String id = customers.get(i).getCustomerId();
            if (id.startsWith("C")) {
                try {
                    int idNum = Integer.parseInt(id.substring(1));
                    highestId = Math.max(highestId, idNum);
                } catch (NumberFormatException e) {
                    // Skip if not numeric
                }
            }
        }
        return "C" + String.format("%03d", highestId + 1);
    }

    // Find a customer by ID
    public Customer findCustomerById(String id) {
        return customers.find(c -> c.getCustomerId().equalsIgnoreCase(id));
    }

    // Find a customer by name (partial match)
    public MyLinkedList<Customer> findCustomersByName(String name) {
        MyLinkedList<Customer> results = new MyLinkedList<>();
        for (int i = 0; i < customers.size(); i++) {
            Customer cust = customers.get(i);
            if (cust.getName().toLowerCase().contains(name.toLowerCase())) {
                results.add(cust);
            }
        }

        // Display results in attractive format
        if (results.isEmpty()) {
            System.out.println(RED + "\nNo customers found matching '" + name + "'" + RESET);
            return results;
        }

        // Calculate column widths
        int idWidth = 8;
        int nameWidth = 25;
        int phoneWidth = 15;
        int emailWidth = 30;

        // Print header
        System.out.println(CYAN + BOLD + "\n" + "=".repeat(85));
        System.out.println("Search Results for '" + name + "'");
        System.out.println("=".repeat(85));
        System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + phoneWidth + "s | %-" + emailWidth + "s\n", 
            "ID", "Name", "Phone", "Email");
        System.out.println("=".repeat(85) + RESET);

        // Print customers
        for (int i = 0; i < results.size(); i++) {
            Customer cust = results.get(i);
            System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + phoneWidth + "s | %-" + emailWidth + "s\n",
                cust.getCustomerId(),
                cust.getName(),
                cust.getPhoneNumber(),
                cust.getEmail());
        }

        // Print footer
        System.out.println(CYAN + BOLD + "=".repeat(85) + RESET);

        return results;
    }

    // Add method to update customer information
    public void updateCustomerInfo(String id, String name, String phone, String email) {
        Customer customer = findCustomerById(id);
        if (customer != null) {
            // Calculate column widths
            int idWidth = 8;
            int nameWidth = 25;
            int phoneWidth = 15;
            int emailWidth = 30;

            // Print current information
            System.out.println(CYAN + BOLD + "\nCurrent Customer Information:" + RESET);
            System.out.println("=".repeat(85));
            System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + phoneWidth + "s | %-" + emailWidth + "s\n", 
                "ID", "Name", "Phone", "Email");
            System.out.println("=".repeat(85));
            System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + phoneWidth + "s | %-" + emailWidth + "s\n",
                customer.getCustomerId(),
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getEmail());
            System.out.println("=".repeat(85));

            // Update customer information
            customer.setName(name);
            customer.setPhoneNumber(phone);
            customer.setEmail(email);

            CSVUtility.saveCustomers("data/customers.csv", customers);

            // Print updated information
            System.out.println(CYAN + BOLD + "\nUpdated Customer Information:" + RESET);
            System.out.println("=".repeat(85));
            System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + phoneWidth + "s | %-" + emailWidth + "s\n", 
                "ID", "Name", "Phone", "Email");
            System.out.println("=".repeat(85));
            System.out.printf("%-" + idWidth + "s | %-" + nameWidth + "s | %-" + phoneWidth + "s | %-" + emailWidth + "s\n",
                customer.getCustomerId(),
                customer.getName(),
                customer.getPhoneNumber(),
                customer.getEmail());
            System.out.println("=".repeat(85));
        } else {
            System.out.println("Customer with ID " + id + " not found.");
        }
    }

    // Process checkout from cart
    public Bill processCheckout(Cart cart, Customer customer) {
        if (cart.isEmpty()) {
            System.out.println("Cart is empty. Nothing to checkout.");
            return null;
        }

        // Create transactions for each item in cart
        MyLinkedList<CartItem> items = cart.getItems();
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            String transactionId = UUID.randomUUID().toString();

            // Create transaction
            Transaction transaction = new Transaction(
                    transactionId,
                    customer.getCustomerId(),
                    item.getMedicineId(),
                    item.getQuantity(),
                    item.getSubtotal(),
                    LocalDate.now()
            );

            transactions.add(transaction);

            // Update medicine quantity in inventory
            Medicine medicine = findMedicineById(item.getMedicineId());
            if (medicine != null) {
                medicine.setQuantity(medicine.getQuantity() - item.getQuantity());
            }
        }

        // Save updated data
        CSVUtility.saveTransactions("data/transactions.csv", transactions);
        CSVUtility.saveMedicines("data/medicines.csv", medicines);

        // Create and return bill
        Bill bill = new Bill(
                customer.getCustomerId(),
                customer.getName(),
                items,
                cart.getCartTotal()
        );

        bills.add(bill);

        // Save bill to file
        bill.saveToFile();

        return bill;
    }

    // Create a transaction
    public void createTransaction(String customerId, String medicineId, int quantity) {
        // Find the customer
        Customer customer = findCustomerById(customerId);
        if (customer == null) {
            System.out.println("Customer not found!");
            return;
        }

        // Find the medicine
        Medicine medicine = findMedicineById(medicineId);
        if (medicine == null) {
            System.out.println("Medicine not found!");
            return;
        }
        if (medicine.getQuantity() < quantity) {
            System.out.println("Not enough stock available!");
            return;
        }

        // Calculate total amount
        double totalAmount = medicine.getPrice() * quantity;

        // Generate a unique transaction ID
        String transactionId = UUID.randomUUID().toString();

        // Create the transaction object
        Transaction transaction = new Transaction(transactionId, customerId, medicineId, quantity, totalAmount, LocalDate.now());

        // Add transaction to the list
        transactions.add(transaction);

        // Update the medicine quantity in the data store
        medicine.setQuantity(medicine.getQuantity() - quantity);

        // Save data to persist changes
        CSVUtility.saveTransactions("data/transactions.csv", transactions);
        CSVUtility.saveMedicines("data/medicines.csv", medicines);

        System.out.println("Transaction created successfully: " + transaction);
    }

    // Display low stock medicines (quantity < threshold)
    public void displayLowStockMedicines(int threshold) {
        System.out.println("\n===== Low Stock Medicines =====");
        System.out.printf("%-10s %-20s %-10s\n", "ID", "Name", "Quantity");
        System.out.println("---------------------------------------");

        boolean found = false;
        for (int i = 0; i < medicines.size(); i++) {
            Medicine med = medicines.get(i);
            if (med.getQuantity() < threshold) {
                System.out.printf("%-10s %-20s %-10d\n", med.getId(), med.getName(), med.getQuantity());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No medicines below the threshold quantity.");
        }
    }

    // Find customers by phone number
    public MyLinkedList<Customer> findCustomersByPhone(String phoneNumber) {
        MyLinkedList<Customer> results = new MyLinkedList<>();
        for (int i = 0; i < customers.size(); i++) {
            Customer cust = customers.get(i);
            if (cust.getPhoneNumber().equals(phoneNumber)) {
                results.add(cust);
            }
        }
        return results;
    }

    // Find customers by email
    public MyLinkedList<Customer> findCustomersByEmail(String email) {
        MyLinkedList<Customer> results = new MyLinkedList<>();
        for (int i = 0; i < customers.size(); i++) {
            Customer cust = customers.get(i);
            if (cust.getEmail().equalsIgnoreCase(email)) {
                results.add(cust);
            }
        }
        return results;
    }
}