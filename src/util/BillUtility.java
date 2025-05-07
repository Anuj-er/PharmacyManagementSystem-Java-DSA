package util;

import model.Bill;
import model.CartItem;
import data_structure.MyLinkedList;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BillUtility {

    public static void saveBillToFile(Bill bill) {
        String folderName = "bills";

        String fileName = folderName + "/BILL_" + bill.getCustomerId() + "_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            // Bill header
            writer.println("================================================");
            writer.println("                PHARMACY BILL                   ");
            writer.println("================================================");
            writer.println("Date: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            writer.println("Customer ID: " + bill.getCustomerId());
            writer.println("Customer Name: " + bill.getCustomerName());
            writer.println("------------------------------------------------");

            // Bill items
            writer.println(String.format("%-5s %-20s %-10s %-10s %-10s",
                    "No.", "Medicine", "Price", "Qty", "Subtotal"));
            writer.println("------------------------------------------------");

            MyLinkedList<CartItem> items = bill.getItems();
            for (int i = 0; i < items.size(); i++) {
                CartItem item = items.get(i);
                writer.println(String.format("%-5d %-20s $%-9.2f %-10d $%-9.2f",
                        (i + 1),
                        item.getMedicineName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getSubtotal()));
            }

            // Bill footer
            writer.println("------------------------------------------------");
            writer.println(String.format("TOTAL AMOUNT: $%.2f", bill.getTotalAmount()));
            writer.println("================================================");
            writer.println("Thank you for shopping with us!");
            writer.println("================================================");

            System.out.println("Bill saved to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving bill to file: " + e.getMessage());
        }
    }
}
