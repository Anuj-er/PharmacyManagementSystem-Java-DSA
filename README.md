<div align="center">  

# 💊 Pharmacy Management System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Data Structures](https://img.shields.io/badge/Data_Structures-007396?style=for-the-badge&logo=java&logoColor=white)

## 🌟 ST-3 Final Project for Data Structures and Algorithms (DSA) and Java Subject

<img src="https://media.tenor.com/HHT4dska9YUAAAAM/thats-the-spirit-pharmacist.gif" alt="Pharmacist GIF" width="200">

[Features](#-features) · [Project Structure](#-project-structure) · [Installation](#-installation) · [Usage](#-usage)

</div>

---

## 📌 Table of Contents
- [Overview](#-overview)
- [Features](#-features)
- [Project Structure](#-project-structure)
- [Technical Implementation](#-technical-implementation)
- [Installation](#-installation)
- [Usage](#-usage)
- [Data Structure Implementation](#-data-structure-implementation)
- [Contributors](#-contributors)
- [License](#-license)

## 📋 Overview
The Pharmacy Management System is a comprehensive Java application developed as a final project for the Data Structures and Algorithms (DSA) and Java course. This system provides a complete solution for pharmacy management, including inventory control, customer management, medicine tracking, and sales processing.

<img src="project_files/screenshot.png" alt="Pharmacy Management System" width="600" height="400">

## 🚀 Features

- **🏥 Medicine Management**: Add, search, update, and remove medicines from inventory
- **👥 Customer Management**: Register customers, manage profiles, and track purchase history
- **🛒 Shopping Cart**: Easy-to-use interface for adding medicines to cart and checkout
- **📊 Inventory Management**: Track stock levels and receive low stock alerts
- **💰 Transaction Processing**: Process sales and maintain transaction history
- **🧾 Billing System**: Generate detailed bills for customers
- **💾 Data Persistence**: Save and load data using CSV files

## 🗂 Project Structure
<div style="width: 100%; height:600px ;">
    
```mermaid
graph TD
    A[Pharmacy Management System] --> B[Controllers]
    A --> C[Models]
    A --> D[Data Structures]
    A --> E[Utilities]
    A --> F[Storage]
    
    B --> B1[CustomerManager]
    B --> B2[PharmacyManager]
    
    C --> C1[Medicine]
    C --> C2[Customer]
    C --> C3[Cart]
    C --> C4[CartItem]
    C --> C5[Transaction]
    C --> C6[Bill]
    
    D --> D1[MyLinkedList]
    D --> D2[MergeSort]
    D --> D3[MyQueue]
    
    E --> E1[CSVUtility]
    E --> E2[BillUtility]
    
    F --> F1[DataStore]
```
</div>

## 💻 Technical Implementation

- **MVC Architecture**: Clean separation of concerns with Model-View-Controller pattern
- **Custom Data Structures**: Implementation of LinkedList, Queue, and MergeSort algorithms
- **File-based Storage**: CSV-based data persistence for easy deployment
- **Console UI**: User-friendly command-line interface with intuitive navigation
- **Modularity**: Well-organized code structure with clear separation of functionalities
- **Error Handling**: Robust error and exception handling throughout the application

## 🔧 Installation

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Git (optional)

### Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Anuj-er/PharmacyManagementSystem-Java-DSA.git
   cd PharmacyManagementSystem
   ```

2. **Compile the Project**
   ```bash
   javac -d output src/Main.java src/controller/*.java src/model/*.java src/data_structure/*.java src/util/*.java src/storage/*.java
   ```

3. **Run the Application**
   ```bash
   java -cp output Main
   ```

## 📱 Usage

After launching the application, you'll be presented with the main menu:

1. **For Pharmacy Staff**:
   - Medicine Management: Add, update, and remove medicines
   - Customer Management: Manage customer profiles
   - Inventory Management: Update quantities and check low stock
   - Transaction History: View past transactions

2. **For Customers**:
   - Browse available medicines
   - Search for specific medicines
   - Add medicines to cart
   - View and manage cart
   - Checkout and receive bills

## 🧮 Data Structure Implementation

This project implements several custom data structures:

- **MyLinkedList<T>**: A generic linked list implementation used throughout the system for dynamic data storage
- **MyQueue<T>**: A queue implementation used for processing orders
- **MergeSort**: An efficient sorting algorithm used for sorting medicines and transactions

Example implementation of MyLinkedList:
```java
public class MyLinkedList<T> {
    private Node<T> head;
    private int size;
    
    // Node inner class
    private static class Node<T> {
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    // Add element to the list
    public void add(T element) {
        Node<T> newNode = new Node<>(element);
        
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }
    
    // Other methods: remove, get, size, isEmpty, etc.
}
```

## 👥 Contributors

- [Anushi 2310991700](https://github.com/Anushi13prsnl)
- [Anuj Kumar 2310991699](https://github.com/Anuj-er)
- [Abhinav Rathee 2310991675](https://github.com/abhinavrathee)
- [Akanksha Mishra 2310991681](https://github.com/akankshamishra2)

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

<div align="center">

### 💖 Made with ❤️ for the ST-3 Final Project

*"The best way to predict the future is to invent it." - Alan Kay*

[![Made with Java](https://img.shields.io/badge/Made%20with-Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://github.com/yourname)

</div>
