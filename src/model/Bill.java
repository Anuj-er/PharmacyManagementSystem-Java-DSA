package model;


import data_structure.MyLinkedList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Bill {
    private final String billId;
    private final String customerId;
    private final String customerName;
    private final MyLinkedList<CartItem> items;
    private final double totalAmount;
    private final LocalDate billDate;

    public Bill(String customerId, String customerName, MyLinkedList<CartItem> items, double totalAmount) {
        this.billId = UUID.randomUUID().toString().substring(0, 8);
        this.customerId = customerId;
        this.customerName = customerName;
        this.items = items;
        this.totalAmount = totalAmount;
        this.billDate = LocalDate.now();
    }

    public void displayBill() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("\n===============================================");
        System.out.println("             PHARMACY BILL RECEIPT             ");
        System.out.println("===============================================");
        System.out.println("Bill No: " + billId);
        System.out.println("Date: " + billDate.format(formatter));
        System.out.println("Customer ID: " + customerId);
        System.out.println("Customer Name: " + customerName);
        System.out.println("-----------------------------------------------");
        System.out.printf("%-5s %-20s %-10s %-10s %-10s\n", "No.", "Medicine", "Price", "Qty", "Amount");
        System.out.println("-----------------------------------------------");

        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            System.out.printf("%-5d %-20s ₹%-9.2f %-10d ₹%-9.2f\n",
                    (i+1),
                    item.getMedicineName(),
                    item.getPrice(),
                    item.getQuantity(),
                    item.getSubtotal()
            );
        }

        System.out.println("-----------------------------------------------");
        System.out.printf("%-46s ₹%-9.2f\n", "Subtotal:", totalAmount);
        System.out.printf("%-46s ₹%-9.2f\n", "Tax (5%):", totalAmount * 0.05);
        System.out.printf("%-46s ₹%-9.2f\n", "TOTAL:", totalAmount * 1.05);
        System.out.println("===============================================");
        System.out.println("Thank you for your purchase!");
        System.out.println("Please visit again.");
        System.out.println("===============================================");
    }

    public void saveToFile() {
        util.BillUtility.saveBillToFile(this);
    }
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
}
