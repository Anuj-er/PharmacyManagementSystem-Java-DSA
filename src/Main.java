import controller.CustomerManager;
import controller.PharmacyManager;
import model.Medicine;
import model.Customer;
import model.Bill;

import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Initialize managers
        PharmacyManager pharmacyManager = new PharmacyManager();
        CustomerManager customerManager = new CustomerManager(pharmacyManager);
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;

        // Display welcome message
        System.out.println("        ___           ___           ___     ");
        System.out.println("        /\\  \\         /\\__\\         /\\  \\    ");
        System.out.println("       /::\\  \\       /::|  |       /::\\  \\   ");
        System.out.println("      /:/\\:\\  \\     /:|:|  |      /:/\\ \\  \\  ");
        System.out.println("     /::\\~\\:\\  \\   /:/|:|__|__   _\\:\\~\\ \\  \\ ");
        System.out.println("    /:/\\:\\ \\:\\__\\ /:/ |::::\\__\\ /\\ \\:\\ \\ \\__\\");
        System.out.println("    \\/__\\:\\/:/  / \\/__/~~/:/  / \\:\\ \\:\\ \\/__/");
        System.out.println("         \\::/  /        /:/  /   \\:\\ \\:\\__\\  ");
        System.out.println("          \\/__/        /:/  /     \\:\\/:/  /  ");
        System.out.println("                      /:/  /       \\::/  /   ");
        System.out.println("                      \\/__/         \\/__/    ");




        System.out.println("======================================");
        System.out.println("   PHARMACY MANAGEMENT SYSTEM");
        System.out.println("======================================");

        while (!exit) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Pharmacy Staff Interface");
            System.out.println("2. Customer Interface");
            System.out.println("0. Exit Application");
            System.out.print("Select an option: ");

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    pharmacyStaffInterface(pharmacyManager, scanner);
                    break;
                case 2:
                    customerInterface(customerManager, scanner);
                    break;
                case 0:
                    exit = true;
                    System.out.println("\nThank you for using the Pharmacy Management System!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void pharmacyStaffInterface(PharmacyManager pharmacyManager, Scanner scanner) {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("           PHARMACY STAFF INTERFACE");
            System.out.println("=".repeat(50));
            System.out.println("1. Medicine Management");
            System.out.println("2. Customer Management");
            System.out.println("3. Inventory Management");
            System.out.println("4. Transaction History");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    medicineManagementMenu(pharmacyManager, scanner);
                    break;
                case 2:
                    customerManagementMenu(pharmacyManager, scanner);
                    break;
                case 3:
                    inventoryManagementMenu(pharmacyManager, scanner);
                    break;
                case 4:
                    transactionMenu(pharmacyManager, scanner);
                    break;
                case 0:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void medicineManagementMenu(PharmacyManager pharmacyManager, Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\n----- Medicine Management -----");
            System.out.println("1. Show All Medicines");
            System.out.println("2. Add New Medicine");
            System.out.println("3. Find Medicine by ID");
            System.out.println("4. Find Medicines by Name");
            System.out.println("5. Remove Medicine");
            System.out.println("6. Update Medicine Price");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    pharmacyManager.showAllMedicines();
                    waitForEnter(scanner);
                    break;

                case 2:
                    // Generate medicine ID automatically
                    String id = pharmacyManager.generateMedicineId();
                    System.out.println("Generated Medicine ID: " + id);

                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Manufacturer: ");
                    String manufacturer = scanner.nextLine();
                    System.out.print("Enter Price: ");
                    double price = getDoubleInput(scanner);

                    // Validate expiry date
                    String expiryDate;
                    while (true) {
                        System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
                        expiryDate = scanner.nextLine();

                        // Basic validation of date format
                        if (expiryDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                            try {
                                LocalDate expiry = LocalDate.parse(expiryDate);
                                if (expiry.isAfter(LocalDate.now())) {
                                    break; // Valid date
                                } else {
                                    System.out.println("Error: Expiry date must be in the future.");
                                }
                            } catch (Exception e) {
                                System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
                            }
                        } else {
                            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
                        }
                    }

                    System.out.print("Enter Quantity: ");
                    int quantity = getIntInput(scanner);

                    Medicine newMed = new Medicine(id, name, manufacturer, price, expiryDate, quantity);
                    pharmacyManager.addMedicine(newMed);
                    waitForEnter(scanner);
                    break;

                case 3:
                    System.out.print("Enter Medicine ID: ");
                    String searchId = scanner.nextLine();

                    if (searchId.matches("\\d+")) {
                        searchId = "MED" + searchId;
                        System.out.println("Searching for ID: " + searchId);
                    }

                    Medicine found = pharmacyManager.findMedicineById(searchId);
                    if (found != null) {
                        System.out.println(found);
                    } else {
                        System.out.println("Medicine not found.");
                    }
                    waitForEnter(scanner);
                    break;

                case 4:
                    System.out.print("Enter Medicine Name to search: ");
                    String searchName = scanner.nextLine();
                    pharmacyManager.findMedicinesByName(searchName).display();
                    waitForEnter(scanner);
                    break;

                case 5:
                    System.out.print("Enter Medicine ID to remove: ");
                    String removeId = scanner.nextLine();

                    if (removeId.matches("\\d+")) {
                        removeId = "MED" + removeId;
                        System.out.println("Removing medicine with ID: " + removeId);
                    }

                    pharmacyManager.removeMedicineById(removeId);
                    waitForEnter(scanner);
                    break;

                case 6:
                    System.out.print("Enter Medicine ID to update price: ");
                    String updatePriceId = scanner.nextLine();

                    if (updatePriceId.matches("\\d+")) {
                        updatePriceId = "MED" + updatePriceId;
                        System.out.println("Updating price for ID: " + updatePriceId);
                    }

                    System.out.print("Enter new price: ");
                    double newPrice = getDoubleInput(scanner);
                    pharmacyManager.updateMedicinePrice(updatePriceId, newPrice);
                    waitForEnter(scanner);
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    private static void customerManagementMenu(PharmacyManager pharmacyManager, Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\n----- Customer Management -----");
            System.out.println("1. Show All Customers");
            System.out.println("2. Add New Customer");
            System.out.println("3. Find Customer by ID");
            System.out.println("4. Find Customers by Name");
            System.out.println("5. Edit Customer Information");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    pharmacyManager.showAllCustomers();
                    waitForEnter(scanner);
                    break;

                case 2:
                    // Generate customer ID automatically
                    String customerId = pharmacyManager.generateCustomerId();
                    System.out.println("Generated Customer ID: " + customerId);

                    // Collect and validate customer information
                    System.out.print("Enter Customer Name: ");
                    String customerName = scanner.nextLine();
                    while (customerName.trim().isEmpty()) {
                        System.out.println("Error: Name cannot be empty.");
                        System.out.print("Enter Customer Name: ");
                        customerName = scanner.nextLine();
                    }

                    System.out.print("Enter Customer Phone (10 digits): ");
                    String customerPhone = scanner.nextLine();
                    while (!customerPhone.matches("\\d{10}")) {
                        System.out.println("Error: Phone must be 10 digits.");
                        System.out.print("Enter Customer Phone (10 digits): ");
                        customerPhone = scanner.nextLine();
                    }

                    System.out.print("Enter Customer Email: ");
                    String customerEmail = scanner.nextLine();
                    while (!customerEmail.contains("@") || !customerEmail.contains(".")) {
                        System.out.println("Error: Invalid email format.");
                        System.out.print("Enter Customer Email: ");
                        customerEmail = scanner.nextLine();
                    }

                    Customer newCustomer = new Customer(customerId, customerName, customerPhone, customerEmail);
                    pharmacyManager.addCustomer(newCustomer);

                    // Print customer description
                    System.out.println("\nCustomer added successfully. Details:");
                    System.out.println(newCustomer);

                    waitForEnter(scanner);
                    break;

                case 3:
                    System.out.print("Enter Customer ID: ");
                    String searchId = scanner.nextLine();

                    if (searchId.matches("\\d+")) {
                        searchId = "C" + searchId;
                        System.out.println("Searching for ID: " + searchId);
                    }

                    Customer foundCustomer = pharmacyManager.findCustomerById(searchId);
                    if (foundCustomer != null) {
                        System.out.println(foundCustomer);
                    } else {
                        System.out.println("Customer not found.");
                    }
                    waitForEnter(scanner);
                    break;

                case 4:
                    System.out.print("Enter Customer Name to search: ");
                    String searchName = scanner.nextLine();
                    pharmacyManager.findCustomersByName(searchName).display();
                    waitForEnter(scanner);
                    break;

                case 5:
                    System.out.print("Enter Customer ID to edit: ");
                    String editId = scanner.nextLine();

                    if (editId.matches("\\d+")) {
                        editId = "C" + editId;
                        System.out.println("Editing customer with ID: " + editId);
                    }

                    Customer customerToEdit = pharmacyManager.findCustomerById(editId);
                    if (customerToEdit != null) {
                        System.out.println("\nCurrent Information:");
                        System.out.println(customerToEdit);

                        System.out.print("Enter new Name (or press Enter to keep current): ");
                        String newName = scanner.nextLine();
                        if (newName.trim().isEmpty()) {
                            newName = customerToEdit.getName();
                        }

                        System.out.print("Enter new Phone (or press Enter to keep current): ");
                        String newPhone = scanner.nextLine();
                        if (newPhone.trim().isEmpty()) {
                            newPhone = customerToEdit.getPhoneNumber();
                        } else if (!newPhone.matches("\\d{10}")) {
                            System.out.println("Error: Phone must be 10 digits. Keeping original phone.");
                            newPhone = customerToEdit.getPhoneNumber();
                        }

                        System.out.print("Enter new Email (or press Enter to keep current): ");
                        String newEmail = scanner.nextLine();
                        if (newEmail.trim().isEmpty()) {
                            newEmail = customerToEdit.getEmail();
                        } else if (!newEmail.contains("@") || !newEmail.contains(".")) {
                            System.out.println("Error: Invalid email format. Keeping original email.");
                            newEmail = customerToEdit.getEmail();
                        }

                        pharmacyManager.updateCustomerInfo(editId, newName, newPhone, newEmail);
                    } else {
                        System.out.println("Customer not found.");
                    }
                    waitForEnter(scanner);
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void inventoryManagementMenu(PharmacyManager pharmacyManager, Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\n----- Inventory Management -----");
            System.out.println("1. Update Medicine Quantity");
            System.out.println("2. Display Low Stock Medicines");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    System.out.print("Enter Medicine ID to update quantity: ");
                    String updateId = scanner.nextLine();

                    if (updateId.matches("\\d+")) {
                        updateId = "M" + updateId;
                        System.out.println("Updating quantity for ID: " + updateId);
                    }

                    // The medicine info will be displayed by the updated updateMedicineQuantity method
                    System.out.print("Enter new quantity: ");
                    int newQty = getIntInput(scanner);
                    pharmacyManager.updateMedicineQuantity(updateId, newQty);
                    waitForEnter(scanner);
                    break;

                case 2:
                    System.out.print("Enter threshold quantity: ");
                    int threshold = getIntInput(scanner);
                    pharmacyManager.displayLowStockMedicines(threshold);
                    waitForEnter(scanner);
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void transactionMenu(PharmacyManager pharmacyManager, Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\n----- Transaction Management -----");
            System.out.println("1. Show All Transactions");
            System.out.println("2. Create Transaction");
            System.out.println("0. Back");
            System.out.print("Select an option: ");

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    pharmacyManager.showAllTransactions();
                    waitForEnter(scanner);
                    break;

                case 2:
                    System.out.print("Enter Customer ID for the transaction: ");
                    String transCustomerId = scanner.nextLine();
                    System.out.print("Enter Medicine ID: ");
                    String transMedicineId = scanner.nextLine();
                    System.out.print("Enter Quantity: ");
                    int transQuantity = getIntInput(scanner);

                    pharmacyManager.createTransaction(transCustomerId, transMedicineId, transQuantity);
                    waitForEnter(scanner);
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void customerInterface(CustomerManager customerManager, Scanner scanner) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("               CUSTOMER INTERFACE");
        System.out.println("=".repeat(50));

        if (!customerManager.isLoggedIn()) {
            customerLoginMenu(customerManager, scanner);
            if (!customerManager.isLoggedIn()) {
                return;
            }
        }

        customerMainMenu(customerManager, scanner);
    }

    private static void customerLoginMenu(CustomerManager customerManager, Scanner scanner) {
        boolean backToMain = false;

        while (!backToMain && !customerManager.isLoggedIn()) {
            System.out.println("\n----- Customer Login/Registration -----");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    System.out.print("Enter Customer ID: ");
                    String customerId = scanner.nextLine();
                    if (customerManager.login(customerId)) {
                        System.out.println("Login successful! Welcome, " +
                                customerManager.getCurrentCustomer().getName() + "!");
                    } else {
                        System.out.println("Login failed: Customer ID not found.");
                    }
                    break;

                case 2:
                    System.out.print("Enter your name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter your phone number: ");
                    String phone = scanner.nextLine();
                    System.out.print("Enter your email: ");
                    String email = scanner.nextLine();

                    if (customerManager.registerCustomer(name, phone, email)) {
                        System.out.println("Registration successful! Welcome, " + name + "!");
                        System.out.println("Your customer ID is: " +
                                customerManager.getCurrentCustomer().getCustomerId());
                        System.out.println("Please keep this ID for future logins.");
                    } else {
                        System.out.println("Registration failed.");
                    }
                    break;

                case 0:
                    backToMain = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void customerMainMenu(CustomerManager customerManager, Scanner scanner) {
        boolean logout = false;

        while (!logout && customerManager.isLoggedIn()) {
            System.out.println("\n----- Customer Menu -----");
            System.out.println("Welcome, " + customerManager.getCurrentCustomer().getName() + "!");
            System.out.println("1. Browse Medicines");
            System.out.println("2. Search Medicines");
            System.out.println("3. View Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Logout");
            System.out.print("Select an option: ");

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    customerManager.viewAvailableMedicines();
                    addToCartOption(customerManager, scanner);
                    break;

                case 2:
                    System.out.print("Enter medicine name to search: ");
                    String searchName = scanner.nextLine();
                    customerManager.searchMedicinesByName(searchName);
                    addToCartOption(customerManager, scanner);
                    break;

                case 3:
                    customerManager.viewCart();
                    manageCartMenu(customerManager, scanner);
                    break;

                case 4:
                    Bill bill = customerManager.checkout();
                    if (bill != null) {
                        waitForEnter(scanner);
                    }
                    break;

                case 5:
                    customerManager.logout();
                    logout = true;
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addToCartOption(CustomerManager customerManager, Scanner scanner) {
        System.out.print("\nDo you want to add a medicine to your cart? (y/n): ");
        String choice = scanner.nextLine().trim().toLowerCase();

        if (choice.equals("y")) {
            System.out.print("Enter Medicine ID to add to cart: ");
            String medicineId = scanner.nextLine();
            System.out.print("Enter quantity: ");
            int quantity = getIntInput(scanner);
            customerManager.addToCart(medicineId, quantity);
            waitForEnter(scanner);
        }
    }

    private static void manageCartMenu(CustomerManager customerManager, Scanner scanner) {
        if (customerManager.getCart().isEmpty()) {
            waitForEnter(scanner);
            return;
        }

        System.out.println("\n----- Cart Options -----");
        System.out.println("1. Update Item Quantity");
        System.out.println("2. Remove Item");
        System.out.println("3. Clear Cart");
        System.out.println("4. Checkout");
        System.out.println("0. Back");
        System.out.print("Select an option: ");

        int choice = getUserChoice(scanner);

        switch (choice) {
            case 1:
                System.out.print("Enter Medicine ID to update: ");
                String updateId = scanner.nextLine();
                System.out.print("Enter new quantity: ");
                int newQty = getIntInput(scanner);
                customerManager.updateCartItemQuantity(updateId, newQty);
                customerManager.viewCart();
                waitForEnter(scanner);
                break;

            case 2:
                System.out.print("Enter Medicine ID to remove: ");
                String removeId = scanner.nextLine();
                customerManager.removeFromCart(removeId);
                customerManager.viewCart();
                waitForEnter(scanner);
                break;

            case 3:
                customerManager.clearCart();
                waitForEnter(scanner);
                break;

            case 4:
                Bill bill = customerManager.checkout();
                if (bill != null) {
                    waitForEnter(scanner);
                }
                break;

            case 0:
                break;

            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    // Utility methods for input handling

    private static int getUserChoice(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1; // Return invalid choice
        }
    }

    private static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    private static double getDoubleInput(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Please enter a valid number: ");
            }
        }
    }

    private static void waitForEnter(Scanner scanner) {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}