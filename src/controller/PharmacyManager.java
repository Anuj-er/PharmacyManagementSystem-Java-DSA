package controller;

import model.*;
import util.CSVUtility;
import data_structure.MyLinkedList;

import java.time.LocalDate;
import java.util.UUID;

public class PharmacyManager {
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
        System.out.println("Medicine Inventory:");
        medicines.display();
    }

    // Show all customers
    public void showAllCustomers() {
        System.out.println("Customer List:");
        customers.display();
    }

    // Show all transactions
    public void showAllTransactions() {
        System.out.println("Transaction History:");
        transactions.display();
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
        return results;
    }

    // Add method to update customer information
    public void updateCustomerInfo(String id, String name, String phone, String email) {
        Customer customer = findCustomerById(id);
        if (customer != null) {
            System.out.println("\nCurrent Customer Information:");
            System.out.println(customer);

            customer.setName(name);
            customer.setPhoneNumber(phone);
            customer.setEmail(email);

            CSVUtility.saveCustomers("data/customers.csv", customers);
            System.out.println("\nUpdated Customer Information:");
            System.out.println(customer);
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