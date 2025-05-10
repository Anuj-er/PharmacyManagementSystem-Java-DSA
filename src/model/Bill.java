package model;

import data_structure.MyLinkedList;
import util.BillUtility;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bill {
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    private final String customerId;
    private final String customerName;
    private final MyLinkedList<CartItem> items;
    private final double totalAmount;
    private final String billId;
    private final String dateTime;

    public Bill(String customerId, String customerName, MyLinkedList<CartItem> items, double totalAmount) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.billId = generateBillId();
        this.dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private String generateBillId() {
        return "BILL" + System.currentTimeMillis() % 10000;
    }

    public void displayBill() {
        // Print header
        System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
        System.out.println("                        PHARMACY BILL");
        System.out.println("=".repeat(60) + RESET);

        // Print bill details
        System.out.println(GREEN + "\nBill Details:" + RESET);
        System.out.printf("%-15s: %s\n", "Bill ID", BOLD + billId + RESET);
        System.out.printf("%-15s: %s\n", "Date & Time", dateTime);
        System.out.printf("%-15s: %s\n", "Customer ID", customerId);
        System.out.printf("%-15s: %s\n", "Customer Name", customerName);

        // Print items header
        System.out.println(CYAN + BOLD + "\n" + "-".repeat(60));
        System.out.printf("%-8s | %-20s | %-8s | %-10s | %-10s\n",
            "Item ID", "Name", "Quantity", "Price", "Subtotal");
        System.out.println("-".repeat(60) + RESET);

        // Print items
        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            System.out.printf("%-8s | %-20s | %-8d | $%-9.2f | $%-9.2f\n",
                item.getMedicineId(),
                item.getMedicineName(),
                item.getQuantity(),
                item.getPrice(),
                item.getSubtotal());
        }

        // Print total
        System.out.println(CYAN + BOLD + "-".repeat(60) + RESET);
        System.out.printf("%-38s | %-10s | $%-9.2f\n",
            "", "Total Amount", totalAmount);
        System.out.println(CYAN + BOLD + "=".repeat(60) + RESET);

        // Print thank you message
        System.out.println(GREEN + "\nThank you for your purchase!" + RESET);
        System.out.println("Please keep this bill for your records.");
        System.out.println("For any queries, please contact our customer service.");
        
        // Print footer
        System.out.println(CYAN + BOLD + "\n" + "=".repeat(60) + RESET);
    }

    public void saveToFile() {
        BillUtility.saveBillToFile(this);
    }

    // Getters
    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public MyLinkedList<CartItem> getItems() {
        return items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getBillId() {
        return billId;
    }

    public String getDateTime() {
        return dateTime;
    }
}
