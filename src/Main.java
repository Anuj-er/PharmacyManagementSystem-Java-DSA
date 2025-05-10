import controller.CustomerManager;
import controller.PharmacyManager;
import model.Medicine;
import model.Customer;
import model.Bill;
import config.AdminConfig;
import data_structure.MyLinkedList;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-z][a-zA-Z\\s]{1,48}[a-zA-Z]$");

    // Add color constants for better interface
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    // Add method to validate phone number patterns
    private static boolean isValidPhonePattern(String phone) {
        // Check if all digits are same
        if (phone.matches("(\\d)\\1{9}")) {
            System.out.println("Error: Phone number cannot have all same digits.");
            return false;
        }

        // Check for sequential numbers (both ascending and descending)
        if (phone.matches("0123456789|1234567890|9876543210|0987654321")) {
            System.out.println("Error: Phone number cannot be sequential.");
            return false;
        }

        // Check for repeated patterns (like 1234123412)
        for (int i = 1; i <= 5; i++) {
            if (phone.length() % i == 0) {
                String pattern = phone.substring(0, i);
                String repeated = pattern.repeat(phone.length() / i);
                if (phone.equals(repeated)) {
                    System.out.println("Error: Phone number cannot have repeated patterns.");
                    return false;
                }
            }
        }

        return true;
    }

    // Add method to validate name patterns
    private static boolean isValidNamePattern(String name) {
        // Check for repeated characters (like "aaaa")
        if (name.matches(".*(.)\\1{3,}.*")) {
            System.out.println("Error: Name cannot have 4 or more repeated characters.");
            return false;
        }

        // Check for all same characters
        if (name.matches("(.)\\1*")) {
            System.out.println("Error: Name cannot have all same characters.");
            return false;
        }

        // Check for sequential characters (like "abcd")
        if (name.matches(".*(abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opq|pqr|qrs|rst|stu|tuv|uvw|vwx|wxy|xyz).*")) {
            System.out.println("Error: Name cannot contain sequential characters.");
            return false;
        }

        // Check for minimum number of unique characters
        long uniqueChars = name.chars().distinct().count();
        if (uniqueChars < 2) {
            System.out.println("Error: Name must contain at least 2 different characters.");
            return false;
        }

        return true;
    }

    // Add method to validate email patterns
    private static boolean isValidEmailPattern(String email) {
        // Check for common disposable email domains
        String[] disposableDomains = {
            "tempmail", "temp", "throwaway", "fake", "test", "example", "dummy"
        };
        for (String domain : disposableDomains) {
            if (email.toLowerCase().contains(domain)) {
                System.out.println("Error: Disposable email addresses are not allowed.");
                return false;
            }
        }

        // Check for repeated characters in local part
        String localPart = email.split("@")[0];
        if (localPart.matches(".*(.)\\1{3,}.*")) {
            System.out.println("Error: Email local part cannot have 4 or more repeated characters.");
            return false;
        }

        return true;
    }

    // Add method to validate password strength
    private static boolean isValidPassword(String password) {
        if (password.length() < 8) {
            System.out.println("Error: Password must be at least 8 characters long.");
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            System.out.println("Error: Password must contain at least one uppercase letter.");
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            System.out.println("Error: Password must contain at least one lowercase letter.");
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            System.out.println("Error: Password must contain at least one number.");
            return false;
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            System.out.println("Error: Password must contain at least one special character.");
            return false;
        }

        // Check for common patterns
        if (password.matches(".*(123|abc|qwerty|password).*")) {
            System.out.println("Error: Password contains common patterns that are not allowed.");
            return false;
        }

        return true;
    }

    // Add validation methods for medicine
    private static boolean isValidMedicineName(String name) {
        // Check for minimum length
        if (name.length() < 3) {
            System.out.println("Error: Medicine name must be at least 3 characters long.");
            return false;
        }

        // Check for maximum length
        if (name.length() > 50) {
            System.out.println("Error: Medicine name cannot exceed 50 characters.");
            return false;
        }

        // Check for valid characters (letters, numbers, spaces, and common medicine-related characters)
        if (!name.matches("^[a-zA-Z0-9\\s\\-\\/\\(\\)\\+]+$")) {
            System.out.println("Error: Medicine name can only contain letters, numbers, spaces, and -/() characters.");
            return false;
        }

        // Check for repeated characters
        if (name.matches(".*(.)\\1{3,}.*")) {
            System.out.println("Error: Medicine name cannot have 4 or more repeated characters.");
            return false;
        }

        return true;
    }

    private static boolean isValidManufacturer(String manufacturer) {
        // Check for minimum length
        if (manufacturer.length() < 2) {
            System.out.println("Error: Manufacturer name must be at least 2 characters long.");
            return false;
        }

        // Check for maximum length
        if (manufacturer.length() > 50) {
            System.out.println("Error: Manufacturer name cannot exceed 50 characters.");
            return false;
        }

        // Check for valid characters
        if (!manufacturer.matches("^[a-zA-Z0-9\\s\\-\\/\\(\\)\\.,]+$")) {
            System.out.println("Error: Manufacturer name can only contain letters, numbers, spaces, and -/()., characters.");
            return false;
        }

        return true;
    }

    private static boolean isValidPrice(double price) {
        if (price <= 0) {
            System.out.println("Error: Price must be greater than 0.");
            return false;
        }

        if (price > 1000000) {
            System.out.println("Error: Price cannot exceed 1,000,000.");
            return false;
        }

        // Check for reasonable decimal places
        String priceStr = String.valueOf(price);
        if (priceStr.contains(".") && priceStr.split("\\.")[1].length() > 2) {
            System.out.println("Error: Price can only have up to 2 decimal places.");
            return false;
        }

        return true;
    }

    private static boolean isValidQuantity(int quantity) {
        if (quantity <= 0) {
            System.out.println("Error: Quantity must be greater than 0.");
            return false;
        }

        if (quantity > 10000) {
            System.out.println("Error: Quantity cannot exceed 10,000.");
            return false;
        }

        return true;
    }

    private static boolean isValidExpiryDate(String expiryDate) {
        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            LocalDate today = LocalDate.now();
            
            // Check if date is in the future
            if (!expiry.isAfter(today)) {
                System.out.println("Error: Expiry date must be in the future.");
                return false;
            }

            // Check if date is not too far in the future (e.g., 10 years)
            if (expiry.isAfter(today.plusYears(10))) {
                System.out.println("Error: Expiry date cannot be more than 10 years in the future.");
                return false;
            }

            return true;
        } catch (Exception e) {
            System.out.println("Error: Invalid date format. Please use YYYY-MM-DD.");
            return false;
        }
    }

    // Add method for attractive medicine display
    private static void displayMedicineTable(MyLinkedList<Medicine> medicines) {
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

    public static void main(String[] args) {
        // Initialize managers
        PharmacyManager pharmacyManager = new PharmacyManager();
        CustomerManager customerManager = new CustomerManager(pharmacyManager);
        Scanner scanner = new Scanner(System.in);

        boolean exit = false;

        // Display welcome message with colors
        System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
        System.out.println("                WELCOME TO PHARMACY MANAGEMENT SYSTEM");
        System.out.println("=".repeat(60) + RESET);

        System.out.println(PURPLE + BOLD + "\n" + "        ___           ___           ___     ");
        System.out.println("        /\\  \\         /\\__\\         /\\  \\    ");
        System.out.println("       /::\\  \\       /::|  |       /::\\  \\   ");
        System.out.println("      /:/\\:\\  \\     /:|:|  |      /:/\\ \\  \\  ");
        System.out.println("     /::\\~\\:\\  \\   /:/|:|__|__   _\\:\\~\\ \\  \\ ");
        System.out.println("    /:/\\:\\ \\:\\__\\ /:/ |::::\\__\\ /\\ \\:\\ \\ \\__\\");
        System.out.println("    \\/__\\:\\/:/  / \\/__/~~/:/  / \\:\\ \\:\\ \\/__/");
        System.out.println("         \\::/  /        /:/  /   \\:\\ \\:\\__\\  ");
        System.out.println("          \\/__/        /:/  /     \\:\\/:/  /  ");
        System.out.println("                      /:/  /       \\::/  /   ");
        System.out.println("                      \\/__/         \\/__/    " + RESET);

        System.out.println(YELLOW + BOLD + "\nProject: Pharmacy Management System" + RESET);
        System.out.println(YELLOW + "Developed for: Java and Data Structures & Algorithms (DSA)" + RESET);
        System.out.println(YELLOW + "\nTeam Members:" + RESET);
        System.out.println(GREEN + "• Anuj Kumar" + RESET);
        System.out.println(GREEN + "• Anushi" + RESET);
        System.out.println(GREEN + "• Akanksha Mishra" + RESET);
        System.out.println(GREEN + "• Abhinav Rathee" + RESET);

        while (!exit) {
            System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
            System.out.println("                           MAIN MENU");
            System.out.println("=".repeat(60) + RESET);
            System.out.println(GREEN + "1. " + RESET + "Pharmacy Staff Interface");
            System.out.println(GREEN + "2. " + RESET + "Customer Interface");
            System.out.println(RED + "0. " + RESET + "Exit Application");
            System.out.print(YELLOW + "\nSelect an option: " + RESET);

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    if (authenticateAdmin(scanner)) {
                        pharmacyStaffInterface(pharmacyManager, scanner);
                    } else {
                        System.out.println(RED + "\nInvalid admin credentials. Access denied." + RESET);
                    }
                    break;
                case 2:
                    customerInterface(customerManager, pharmacyManager, scanner);
                    break;
                case 0:
                    exit = true;
                    displayThankYouMessage();
                    break;
                default:
                    System.out.println(RED + "\nInvalid option. Please try again." + RESET);
            }
        }

        scanner.close();
    }

    private static boolean authenticateAdmin(Scanner scanner) {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter admin password: ");
        String password = readPassword(scanner);
        
        return AdminConfig.validateAdminCredentials(username, password);
    }

    private static String readPassword(Scanner scanner) {
        if (System.console() != null) {
            return new String(System.console().readPassword());
        } else {
            // Fallback for IDEs that don't support console
            return scanner.nextLine();
        }
    }

    private static void pharmacyStaffInterface(PharmacyManager pharmacyManager, Scanner scanner) {
        boolean backToMain = false;

        while (!backToMain) {
            System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
            System.out.println("                    PHARMACY STAFF INTERFACE");
            System.out.println("=".repeat(60) + RESET);
            System.out.println(GREEN + "1. " + RESET + "Medicine Management");
            System.out.println(GREEN + "2. " + RESET + "Customer Management");
            System.out.println(GREEN + "3. " + RESET + "Inventory Management");
            System.out.println(GREEN + "4. " + RESET + "Transaction History");
            System.out.println(RED + "0. " + RESET + "Back to Main Menu");
            System.out.print(YELLOW + "\nSelect an option: " + RESET);

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
                    System.out.println(RED + "\nInvalid option. Please try again." + RESET);
            }
        }
    }

    private static void medicineManagementMenu(PharmacyManager pharmacyManager, Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
            System.out.println("                    MEDICINE MANAGEMENT");
            System.out.println("=".repeat(60) + RESET);
            System.out.println(GREEN + "1. " + RESET + "Show All Medicines");
            System.out.println(GREEN + "2. " + RESET + "Add New Medicine");
            System.out.println(GREEN + "3. " + RESET + "Find Medicine by ID");
            System.out.println(GREEN + "4. " + RESET + "Find Medicines by Name");
            System.out.println(GREEN + "5. " + RESET + "Remove Medicine");
            System.out.println(GREEN + "6. " + RESET + "Update Medicine Price");
            System.out.println(RED + "0. " + RESET + "Back");
            System.out.print(YELLOW + "\nSelect an option: " + RESET);

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    displayMedicineTable(pharmacyManager.getMedicines());
                    waitForEnter(scanner);
                    break;

                case 2:
                    // Generate medicine ID automatically
                    String id = pharmacyManager.generateMedicineId();
                    System.out.println("Generated Medicine ID: " + id);

                    // Validate medicine name
                    String name;
                    boolean validName = false;
                    do {
                        System.out.print("Enter Name: ");
                        name = scanner.nextLine();
                        if (isValidMedicineName(name)) {
                            validName = true;
                        }
                    } while (!validName);

                    // Validate manufacturer
                    String manufacturer;
                    boolean validManufacturer = false;
                    do {
                        System.out.print("Enter Manufacturer: ");
                        manufacturer = scanner.nextLine();
                        if (isValidManufacturer(manufacturer)) {
                            validManufacturer = true;
                        }
                    } while (!validManufacturer);

                    // Validate price
                    double price = 0.0;
                    boolean validPrice = false;
                    do {
                        System.out.print("Enter Price: ");
                        try {
                            price = Double.parseDouble(scanner.nextLine());
                            if (isValidPrice(price)) {
                                validPrice = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Please enter a valid number.");
                        }
                    } while (!validPrice);

                    // Validate expiry date
                    String expiryDate;
                    boolean validExpiry = false;
                    do {
                        System.out.print("Enter Expiry Date (YYYY-MM-DD): ");
                        expiryDate = scanner.nextLine();
                        if (isValidExpiryDate(expiryDate)) {
                            validExpiry = true;
                        }
                    } while (!validExpiry);

                    // Validate quantity
                    int quantity = 0;
                    boolean validQuantity = false;
                    do {
                        System.out.print("Enter Quantity: ");
                        try {
                            quantity = Integer.parseInt(scanner.nextLine());
                            if (isValidQuantity(quantity)) {
                                validQuantity = true;
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Please enter a valid number.");
                        }
                    } while (!validQuantity);

                    Medicine newMed = new Medicine(id, name, manufacturer, price, expiryDate, quantity);
                    pharmacyManager.addMedicine(newMed);
                    waitForEnter(scanner);
                    break;

                case 3:
                    System.out.print(GREEN + "\nEnter Medicine ID: " + RESET);
                    String searchId = scanner.nextLine();

                    if (searchId.matches("\\d+")) {
                        searchId = "MED" + searchId;
                        System.out.println(CYAN + "Searching for ID: " + searchId + RESET);
                    }

                    Medicine found = pharmacyManager.findMedicineById(searchId);
                    if (found != null) {
                        MyLinkedList<Medicine> singleMed = new MyLinkedList<>();
                        singleMed.add(found);
                        displayMedicineTable(singleMed);
                    } else {
                        System.out.println(RED + "\nMedicine not found." + RESET);
                    }
                    waitForEnter(scanner);
                    break;

                case 4:
                    System.out.print(GREEN + "\nEnter Medicine Name to search: " + RESET);
                    String searchName = scanner.nextLine();
                    displayMedicineTable(pharmacyManager.findMedicinesByName(searchName));
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
            System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
            System.out.println("                    CUSTOMER MANAGEMENT");
            System.out.println("=".repeat(60) + RESET);
            System.out.println(GREEN + "1. " + RESET + "Show All Customers");
            System.out.println(GREEN + "2. " + RESET + "Add New Customer");
            System.out.println(GREEN + "3. " + RESET + "Find Customer by ID");
            System.out.println(GREEN + "4. " + RESET + "Find Customers by Name");
            System.out.println(GREEN + "5. " + RESET + "Edit Customer Information");
            System.out.println(RED + "0. " + RESET + "Back");
            System.out.print(YELLOW + "\nSelect an option: " + RESET);

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                    ALL CUSTOMERS");
                    System.out.println("=".repeat(60) + RESET);
                    pharmacyManager.showAllCustomers();
                    waitForEnter(scanner);
                    break;

                case 2:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                    ADD NEW CUSTOMER");
                    System.out.println("=".repeat(60) + RESET);
                    // Generate customer ID automatically
                    String customerId = pharmacyManager.generateCustomerId();
                    System.out.println(GREEN + "Generated Customer ID: " + customerId + RESET);

                    // Collect and validate customer information
                    System.out.print(GREEN + "Enter Customer Name: " + RESET);
                    String customerName = scanner.nextLine();
                    while (customerName.trim().isEmpty()) {
                        System.out.println(RED + "Error: Name cannot be empty." + RESET);
                        System.out.print(GREEN + "Enter Customer Name: " + RESET);
                        customerName = scanner.nextLine();
                    }

                    System.out.print(GREEN + "Enter Customer Phone (10 digits): " + RESET);
                    String customerPhone = scanner.nextLine();
                    while (!customerPhone.matches("\\d{10}")) {
                        System.out.println(RED + "Error: Phone must be 10 digits." + RESET);
                        System.out.print(GREEN + "Enter Customer Phone (10 digits): " + RESET);
                        customerPhone = scanner.nextLine();
                    }

                    System.out.print(GREEN + "Enter Customer Email: " + RESET);
                    String customerEmail = scanner.nextLine();
                    while (!customerEmail.contains("@") || !customerEmail.contains(".")) {
                        System.out.println(RED + "Error: Invalid email format." + RESET);
                        System.out.print(GREEN + "Enter Customer Email: " + RESET);
                        customerEmail = scanner.nextLine();
                    }

                    Customer newCustomer = new Customer(customerId, customerName, customerPhone, customerEmail, "default123");
                    pharmacyManager.addCustomer(newCustomer);

                    // Print customer description
                    System.out.println(GREEN + "\nCustomer added successfully. Details:" + RESET);
                    System.out.println(newCustomer);

                    waitForEnter(scanner);
                    break;

                case 3:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                    FIND CUSTOMER");
                    System.out.println("=".repeat(60) + RESET);
                    System.out.print(GREEN + "Enter Customer ID: " + RESET);
                    String searchId = scanner.nextLine();

                    if (searchId.matches("\\d+")) {
                        searchId = "C" + searchId;
                        System.out.println(CYAN + "Searching for ID: " + searchId + RESET);
                    }

                    Customer foundCustomer = pharmacyManager.findCustomerById(searchId);
                    if (foundCustomer != null) {
                        System.out.println(GREEN + "\nCustomer found:" + RESET);
                        System.out.println(foundCustomer);
                    } else {
                        System.out.println(RED + "\nCustomer not found." + RESET);
                    }
                    waitForEnter(scanner);
                    break;

                case 4:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                    SEARCH CUSTOMERS");
                    System.out.println("=".repeat(60) + RESET);
                    System.out.print(GREEN + "Enter Customer Name to search: " + RESET);
                    String searchName = scanner.nextLine();
                    pharmacyManager.findCustomersByName(searchName).display();
                    waitForEnter(scanner);
                    break;

                case 5:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                    EDIT CUSTOMER");
                    System.out.println("=".repeat(60) + RESET);
                    System.out.print(GREEN + "Enter Customer ID to edit: " + RESET);
                    String editId = scanner.nextLine();

                    if (editId.matches("\\d+")) {
                        editId = "C" + editId;
                        System.out.println(CYAN + "Editing customer with ID: " + editId + RESET);
                    }

                    Customer customerToEdit = pharmacyManager.findCustomerById(editId);
                    if (customerToEdit != null) {
                        System.out.println(GREEN + "\nCurrent Information:" + RESET);
                        System.out.println(customerToEdit);

                        System.out.print(GREEN + "Enter new Name (or press Enter to keep current): " + RESET);
                        String newName = scanner.nextLine();
                        if (newName.trim().isEmpty()) {
                            newName = customerToEdit.getName();
                        }

                        System.out.print(GREEN + "Enter new Phone (or press Enter to keep current): " + RESET);
                        String newPhone = scanner.nextLine();
                        if (newPhone.trim().isEmpty()) {
                            newPhone = customerToEdit.getPhoneNumber();
                        } else if (!newPhone.matches("\\d{10}")) {
                            System.out.println(RED + "Error: Phone must be 10 digits. Keeping original phone." + RESET);
                            newPhone = customerToEdit.getPhoneNumber();
                        }

                        System.out.print(GREEN + "Enter new Email (or press Enter to keep current): " + RESET);
                        String newEmail = scanner.nextLine();
                        if (newEmail.trim().isEmpty()) {
                            newEmail = customerToEdit.getEmail();
                        } else if (!newEmail.contains("@") || !newEmail.contains(".")) {
                            System.out.println(RED + "Error: Invalid email format. Keeping original email." + RESET);
                            newEmail = customerToEdit.getEmail();
                        }

                        pharmacyManager.updateCustomerInfo(editId, newName, newPhone, newEmail);
                        System.out.println(GREEN + "\nCustomer information updated successfully!" + RESET);
                    } else {
                        System.out.println(RED + "\nCustomer not found." + RESET);
                    }
                    waitForEnter(scanner);
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println(RED + "\nInvalid option. Please try again." + RESET);
            }
        }
    }

    private static void inventoryManagementMenu(PharmacyManager pharmacyManager, Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
            System.out.println("                    INVENTORY MANAGEMENT");
            System.out.println("=".repeat(60) + RESET);
            System.out.println(GREEN + "1. " + RESET + "Update Medicine Quantity");
            System.out.println(GREEN + "2. " + RESET + "Display Low Stock Medicines");
            System.out.println(RED + "0. " + RESET + "Back");
            System.out.print(YELLOW + "\nSelect an option: " + RESET);

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                UPDATE MEDICINE QUANTITY");
                    System.out.println("=".repeat(60) + RESET);
                    System.out.print(GREEN + "Enter Medicine ID to update quantity: " + RESET);
                    String updateId = scanner.nextLine();

                    if (updateId.matches("\\d+")) {
                        updateId = "MED" + updateId;
                        System.out.println(CYAN + "Updating quantity for ID: " + updateId + RESET);
                    }

                    System.out.print(GREEN + "Enter new quantity: " + RESET);
                    int newQty = getIntInput(scanner);
                    pharmacyManager.updateMedicineQuantity(updateId, newQty);
                    waitForEnter(scanner);
                    break;

                case 2:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                LOW STOCK MEDICINES");
                    System.out.println("=".repeat(60) + RESET);
                    System.out.print(GREEN + "Enter threshold quantity: " + RESET);
                    int threshold = getIntInput(scanner);
                    pharmacyManager.displayLowStockMedicines(threshold);
                    waitForEnter(scanner);
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println(RED + "\nInvalid option. Please try again." + RESET);
            }
        }
    }

    private static void transactionMenu(PharmacyManager pharmacyManager, Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
            System.out.println("                    TRANSACTION MANAGEMENT");
            System.out.println("=".repeat(60) + RESET);
            System.out.println(GREEN + "1. " + RESET + "Show All Transactions");
            System.out.println(GREEN + "2. " + RESET + "Create Transaction");
            System.out.println(RED + "0. " + RESET + "Back");
            System.out.print(YELLOW + "\nSelect an option: " + RESET);

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                    ALL TRANSACTIONS");
                    System.out.println("=".repeat(60) + RESET);
                    pharmacyManager.showAllTransactions();
                    waitForEnter(scanner);
                    break;

                case 2:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                    CREATE TRANSACTION");
                    System.out.println("=".repeat(60) + RESET);
                    System.out.print(GREEN + "Enter Customer ID for the transaction: " + RESET);
                    String transCustomerId = scanner.nextLine();
                    System.out.print(GREEN + "Enter Medicine ID: " + RESET);
                    String transMedicineId = scanner.nextLine();
                    System.out.print(GREEN + "Enter Quantity: " + RESET);
                    int transQuantity = getIntInput(scanner);

                    pharmacyManager.createTransaction(transCustomerId, transMedicineId, transQuantity);
                    waitForEnter(scanner);
                    break;

                case 0:
                    back = true;
                    break;

                default:
                    System.out.println(RED + "\nInvalid option. Please try again." + RESET);
            }
        }
    }

    private static void customerInterface(CustomerManager customerManager, PharmacyManager pharmacyManager, Scanner scanner) {
        if (!customerManager.isLoggedIn()) {
            customerLoginMenu(customerManager, pharmacyManager, scanner);
            if (!customerManager.isLoggedIn()) {
                return;
            }
        }
        customerMainMenu(customerManager, scanner);
    }

    private static void customerLoginMenu(CustomerManager customerManager, PharmacyManager pharmacyManager, Scanner scanner) {
        boolean backToMain = false;

        while (!backToMain && !customerManager.isLoggedIn()) {
            System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
            System.out.println("                CUSTOMER LOGIN/REGISTRATION");
            System.out.println("=".repeat(60) + RESET);
            System.out.println(GREEN + "1. " + RESET + "Login");
            System.out.println(GREEN + "2. " + RESET + "Register");
            System.out.println(RED + "0. " + RESET + "Back to Main Menu");
            System.out.print(YELLOW + "\nSelect an option: " + RESET);

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    System.out.print(GREEN + "\nEnter Customer ID: " + RESET);
                    String customerId = scanner.nextLine();
                    System.out.print(GREEN + "Enter Password: " + RESET);
                    String password = readPassword(scanner);
                    
                    if (customerManager.login(customerId, password)) {
                        // Check if the password is default and prompt for change
                        if (password.equals("default123")) {
                            System.out.println(YELLOW + "\nYou are using a default password. Please set a new password." + RESET);
                            String newPassword;
                            boolean validPassword = false;
                            do {
                                System.out.print("Enter new password (min 8 characters, must include uppercase, lowercase, number, and special character): ");
                                newPassword = readPassword(scanner);
                                if (!isValidPassword(newPassword)) {
                                    continue;
                                }
                                validPassword = true;
                            } while (!validPassword);
                            // Update password in the system
                            customerManager.getCurrentCustomer().setPassword(newPassword);
                            pharmacyManager.addCustomer(customerManager.getCurrentCustomer()); // This will overwrite the customer in the list and save
                            System.out.println(GREEN + "\nPassword updated successfully!" + RESET);
                        }
                        System.out.println(GREEN + "\nLogin successful! Welcome, " +
                                customerManager.getCurrentCustomer().getName() + "!" + RESET);
                    } else {
                        System.out.println(RED + "\nLogin failed: Invalid credentials." + RESET);
                    }
                    break;

                case 2:
                    // First validate name
                    String name;
                    boolean validName = false;
                    do {
                        name = getValidatedInput(scanner, "Enter your name: ", NAME_PATTERN, 
                            "Name should start and end with a letter, contain only letters and spaces (2-50 characters)");
                        
                        if (!isValidNamePattern(name)) {
                            continue;
                        }
                        validName = true;
                    } while (!validName);
                    
                    // Then validate phone
                    String phone;
                    boolean validPhone = false;
                    do {
                        phone = getValidatedInput(scanner, "Enter your phone number: ", PHONE_PATTERN,
                            "Phone number must be 10 digits");
                        
                        // Check if phone is all zeros
                        if (phone.matches("0{10}")) {
                            System.out.println("Error: Phone number cannot be all zeros.");
                            continue;
                        }
                        
                        // Check for valid phone pattern
                        if (!isValidPhonePattern(phone)) {
                            continue;
                        }
                        
                        // Check for duplicate phone
                        if (customerManager.isPhoneRegistered(phone)) {
                            System.out.println("Error: Phone number already registered with customer ID: " + 
                                customerManager.getCustomerIdByPhone(phone));
                            continue;
                        }
                        
                        validPhone = true;
                    } while (!validPhone);
                    
                    // Then validate email
                    String email;
                    boolean validEmail = false;
                    do {
                        email = getValidatedInput(scanner, "Enter your email: ", EMAIL_PATTERN,
                            "Invalid email format");
                        
                        if (!isValidEmailPattern(email)) {
                            continue;
                        }
                        
                        // Check for duplicate email
                        if (customerManager.isEmailRegistered(email)) {
                            System.out.println("Error: Email already registered with customer ID: " + 
                                customerManager.getCustomerIdByEmail(email));
                            continue;
                        }
                        
                        validEmail = true;
                    } while (!validEmail);
                    
                    // Only ask for password if all other validations pass
                    String newPassword;
                    boolean validPassword = false;
                    do {
                        System.out.print("Enter password (min 8 characters, must include uppercase, lowercase, number, and special character): ");
                        newPassword = readPassword(scanner);
                        
                        if (!isValidPassword(newPassword)) {
                            continue;
                        }
                        
                        validPassword = true;
                    } while (!validPassword);

                    if (customerManager.registerCustomer(name, phone, email, newPassword)) {
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
                    System.out.println(RED + "\nInvalid option. Please try again." + RESET);
            }
        }
    }

    private static String getValidatedInput(Scanner scanner, String prompt, Pattern pattern, String errorMessage) {
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
            if (!pattern.matcher(input).matches()) {
                System.out.println("Error: " + errorMessage);
            }
        } while (!pattern.matcher(input).matches());
        return input;
    }

    private static void customerMainMenu(CustomerManager customerManager, Scanner scanner) {
        boolean logout = false;

        while (!logout && customerManager.isLoggedIn()) {
            System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
            System.out.println("                    CUSTOMER DASHBOARD");
            System.out.println("=".repeat(60) + RESET);
            
            System.out.println(GREEN + BOLD + "\nWelcome, " + customerManager.getCurrentCustomer().getName() + "!" + RESET);
            System.out.println(CYAN + BOLD + "\n" + "-".repeat(60));
            System.out.println("                           MENU");
            System.out.println("-".repeat(60) + RESET);
            
            System.out.println(GREEN + "1. " + RESET + "Browse Medicines");
            System.out.println(GREEN + "2. " + RESET + "Search Medicines");
            System.out.println(GREEN + "3. " + RESET + "View Cart");
            System.out.println(GREEN + "4. " + RESET + "Checkout");
            System.out.println(RED + "5. " + RESET + "Logout");
            System.out.print(YELLOW + "\nSelect an option: " + RESET);

            int choice = getUserChoice(scanner);

            switch (choice) {
                case 1:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                    AVAILABLE MEDICINES");
                    System.out.println("=".repeat(60) + RESET);
                    customerManager.viewAvailableMedicines();
                    addToCartOption(customerManager, scanner);
                    break;

                case 2:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                    SEARCH MEDICINES");
                    System.out.println("=".repeat(60) + RESET);
                    System.out.print(GREEN + "Enter medicine name to search: " + RESET);
                    String searchName = scanner.nextLine();
                    customerManager.searchMedicinesByName(searchName);
                    addToCartOption(customerManager, scanner);
                    break;

                case 3:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                        YOUR CART");
                    System.out.println("=".repeat(60) + RESET);
                    customerManager.viewCart();
                    manageCartMenu(customerManager, scanner);
                    break;

                case 4:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                      CHECKOUT");
                    System.out.println("=".repeat(60) + RESET);
                    Bill bill = customerManager.checkout();
                    if (bill != null) {
                        waitForEnter(scanner);
                    }
                    break;

                case 5:
                    System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                    System.out.println("                    LOGGING OUT");
                    System.out.println("=".repeat(60) + RESET);
                    System.out.println(GREEN + "\nThank you for using our service, " + 
                        customerManager.getCurrentCustomer().getName() + "!" + RESET);
                    customerManager.logout();
                    logout = true;
                    break;

                default:
                    System.out.println(RED + "\nInvalid option. Please try again." + RESET);
            }
        }
    }

    private static void addToCartOption(CustomerManager customerManager, Scanner scanner) {
        boolean continueAdding = true;
        
        while (continueAdding) {
            System.out.println(CYAN + BOLD + "\n" + "-".repeat(60));
            System.out.println("                    ADD TO CART");
            System.out.println("-".repeat(60) + RESET);
            
            System.out.print(YELLOW + "\nDo you want to add a medicine to your cart? (y/n): " + RESET);
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("y")) {
                System.out.print(GREEN + "Enter Medicine ID to add to cart: " + RESET);
                String medicineId = scanner.nextLine();
                System.out.print(GREEN + "Enter quantity: " + RESET);
                int quantity = getIntInput(scanner);
                customerManager.addToCart(medicineId, quantity);
                waitForEnter(scanner);
            } else {
                continueAdding = false;
            }
        }
    }

    private static void manageCartMenu(CustomerManager customerManager, Scanner scanner) {
        if (customerManager.getCart().isEmpty()) {
            waitForEnter(scanner);
            return;
        }

        System.out.println(CYAN + BOLD + "\n" + "-".repeat(60));
        System.out.println("                    CART OPTIONS");
        System.out.println("-".repeat(60) + RESET);
        
        System.out.println(GREEN + "1. " + RESET + "Update Item Quantity");
        System.out.println(GREEN + "2. " + RESET + "Remove Item");
        System.out.println(GREEN + "3. " + RESET + "Clear Cart");
        System.out.println(GREEN + "4. " + RESET + "Checkout");
        System.out.println(RED + "0. " + RESET + "Back");
        System.out.print(YELLOW + "\nSelect an option: " + RESET);

        int choice = getUserChoice(scanner);

        switch (choice) {
            case 1:
                System.out.println(CYAN + BOLD + "\n" + "-".repeat(60));
                System.out.println("                UPDATE ITEM QUANTITY");
                System.out.println("-".repeat(60) + RESET);
                System.out.print(GREEN + "Enter Medicine ID to update: " + RESET);
                String updateId = scanner.nextLine();
                System.out.print(GREEN + "Enter new quantity: " + RESET);
                int newQty = getIntInput(scanner);
                customerManager.updateCartItemQuantity(updateId, newQty);
                customerManager.viewCart();
                waitForEnter(scanner);
                break;

            case 2:
                System.out.println(CYAN + BOLD + "\n" + "-".repeat(60));
                System.out.println("                  REMOVE ITEM");
                System.out.println("-".repeat(60) + RESET);
                System.out.print(GREEN + "Enter Medicine ID to remove: " + RESET);
                String removeId = scanner.nextLine();
                customerManager.removeFromCart(removeId);
                customerManager.viewCart();
                waitForEnter(scanner);
                break;

            case 3:
                System.out.println(CYAN + BOLD + "\n" + "-".repeat(60));
                System.out.println("                  CLEAR CART");
                System.out.println("-".repeat(60) + RESET);
                customerManager.clearCart();
                waitForEnter(scanner);
                break;

            case 4:
                System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
                System.out.println("                      CHECKOUT");
                System.out.println("=".repeat(60) + RESET);
                Bill bill = customerManager.checkout();
                if (bill != null) {
                    waitForEnter(scanner);
                }
                break;

            case 0:
                break;

            default:
                System.out.println(RED + "\nInvalid option. Please try again." + RESET);
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

    private static void displayThankYouMessage() {
        System.out.println(CYAN + BOLD + "\n" + "=".repeat(60));
        System.out.println("                    THANK YOU FOR USING OUR SYSTEM");
        System.out.println("=".repeat(60) + RESET);
        
        System.out.println(PURPLE + "\n" + "        ___           ___           ___     ");
        System.out.println("        /\\  \\         /\\__\\         /\\  \\    ");
        System.out.println("       /::\\  \\       /::|  |       /::\\  \\   ");
        System.out.println("      /:/\\:\\  \\     /:|:|  |      /:/\\ \\  \\  ");
        System.out.println("     /::\\~\\:\\  \\   /:/|:|__|__   _\\:\\~\\ \\  \\ ");
        System.out.println("    /:/\\:\\ \\:\\__\\ /:/ |::::\\__\\ /\\ \\:\\ \\ \\__\\");
        System.out.println("    \\/__\\:\\/:/  / \\/__/~~/:/  / \\:\\ \\:\\ \\/__/");
        System.out.println("         \\::/  /        /:/  /   \\:\\ \\:\\__\\  ");
        System.out.println("          \\/__/        /:/  /     \\:\\/:/  /  ");
        System.out.println("                      /:/  /       \\::/  /   ");
        System.out.println("                      \\/__/         \\/__/    " + RESET);
        
        System.out.println(GREEN + "\nWe hope you had a great experience with our Pharmacy Management System!");
        System.out.println("Your health and satisfaction are our top priorities.");
        System.out.println("\nProject: Java and Data Structures & Algorithms (DSA)");
        System.out.println("\nDeveloped by:" + RESET);
        System.out.println(YELLOW + "• Anuj Kumar" + RESET);
        System.out.println(YELLOW + "• Anushi" + RESET);
        System.out.println(YELLOW + "• Akanksha Mishra" + RESET);
        System.out.println(YELLOW + "• Abhinav Rathee" + RESET);
        System.out.println(GREEN + "\nPlease visit us again!" + RESET);
        
        System.out.println(CYAN + BOLD + "\n" + "=".repeat(60) + RESET);
    }
}