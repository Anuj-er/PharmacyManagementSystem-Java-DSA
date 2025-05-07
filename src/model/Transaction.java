package model;

import java.time.LocalDate;


public class Transaction {
    private String transactionId;
    private String customerId;
    private String medicineId;
    private int quantity;
    private double totalAmount;
    private LocalDate transactionDate;

    public Transaction(String transactionId, String customerId, String medicineId, int quantity, double totalAmount, LocalDate transactionDate) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.totalAmount = totalAmount;
        this.transactionDate = transactionDate;
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMedicineId() {
        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", medicineId='" + medicineId + '\'' +
                ", quantity=" + quantity +
                ", totalAmount=" + totalAmount +
                ", transactionDate=" + transactionDate +
                '}';
    }
}
