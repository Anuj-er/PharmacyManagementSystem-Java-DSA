package util;

import model.Medicine;
import model.Customer;
import model.Transaction;
import data_structure.MyLinkedList;

import java.io.*;
import java.time.LocalDate;

public class CSVUtility {

    // Load Medicine data from CSV
    public static MyLinkedList<Medicine> loadMedicines(String filepath) {
        MyLinkedList<Medicine> list = new MyLinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Medicine med = new Medicine(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            Double.parseDouble(parts[3].trim()),
                            parts[4].trim(),
                            Integer.parseInt(parts[5].trim())
                    );
                    list.add(med);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV: " + e.getMessage());
        }
        return list;
    }

    // Save Medicine data to CSV
    public static void saveMedicines(String filepath, MyLinkedList<Medicine> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            bw.write("ID,Name,Manufacturer,Price,ExpiryDate,Quantity\n");
            for (int i = 0; i < list.size(); i++) {
                Medicine med = list.get(i);
                bw.write(String.join(",",
                        med.getId(),
                        med.getName(),
                        med.getManufacturer(),
                        String.valueOf(med.getPrice()),
                        med.getExpiryDate(),
                        String.valueOf(med.getQuantity())
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing CSV: " + e.getMessage());
        }
    }

    // Load Customer data from CSV
    public static MyLinkedList<Customer> loadCustomers(String filepath) {
        MyLinkedList<Customer> list = new MyLinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String password = parts.length > 4 ? parts[4].trim() : ""; // Handle existing records without password
                    Customer customer = new Customer(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            parts[3].trim(),
                            password
                    );
                    list.add(customer);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading customers CSV: " + e.getMessage());
        }
        return list;
    }

    // Save Customer data to CSV
    public static void saveCustomers(String filepath, MyLinkedList<Customer> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            bw.write("customerId,name,phoneNumber,email,password\n");
            for (int i = 0; i < list.size(); i++) {
                Customer cust = list.get(i);
                bw.write(String.join(",",
                        cust.getCustomerId(),
                        cust.getName(),
                        cust.getPhoneNumber(),
                        cust.getEmail(),
                        cust.getPassword()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing customers CSV: " + e.getMessage());
        }
    }

    // Load Transaction data from CSV
    public static MyLinkedList<Transaction> loadTransactions(String filepath) {
        MyLinkedList<Transaction> list = new MyLinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    Transaction tx = new Transaction(
                            parts[0].trim(),
                            parts[1].trim(),
                            parts[2].trim(),
                            Integer.parseInt(parts[3].trim()),
                            Double.parseDouble(parts[4].trim()),
                            LocalDate.parse(parts[5].trim())
                    );
                    list.add(tx);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading transactions CSV: " + e.getMessage());
        }
        return list;
    }

    // Save Transaction data to CSV
    public static void saveTransactions(String filepath, MyLinkedList<Transaction> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filepath))) {
            bw.write("transactionId,customerId,medicineId,quantity,totalAmount,transactionDate\n");
            for (int i = 0; i < list.size(); i++) {
                Transaction tx = list.get(i);
                bw.write(String.join(",",
                        tx.getTransactionId(),
                        tx.getCustomerId(),
                        tx.getMedicineId(),
                        String.valueOf(tx.getQuantity()),
                        String.valueOf(tx.getTotalAmount()),
                        tx.getTransactionDate().toString()
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing transactions CSV: " + e.getMessage());
        }
    }
}
