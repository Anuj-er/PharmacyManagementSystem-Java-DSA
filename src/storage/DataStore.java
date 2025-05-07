package storage;

import model.Customer;
import model.Medicine;
import model.Transaction;
import util.CSVUtility;
import data_structure.MyLinkedList;

public class DataStore {
    private MyLinkedList<Medicine> medicines;
    private MyLinkedList<Customer> customers;
    private MyLinkedList<Transaction> transactions;

    public DataStore() {
        medicines = new MyLinkedList<>();
        customers = new MyLinkedList<>();
        transactions = new MyLinkedList<>();
    }

    // Load data from CSV files using MyLinkedList
    public void loadData() {
        medicines = CSVUtility.loadMedicines("data/medicines.csv");
        customers = CSVUtility.loadCustomers("data/customers.csv");
        transactions = CSVUtility.loadTransactions("data/transactions.csv");
    }

    public MyLinkedList<Medicine> getMedicines() {
        return medicines;
    }

    public MyLinkedList<Customer> getCustomers() {
        return customers;
    }

    public MyLinkedList<Transaction> getTransactions() {
        return transactions;
    }

    // Save data back to CSV files using MyLinkedList
    public void saveData() {
        CSVUtility.saveMedicines("data/medicines.csv", medicines);
        CSVUtility.saveCustomers("data/customers.csv", customers);
        CSVUtility.saveTransactions("data/transactions.csv", transactions);
    }
}
