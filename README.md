# 🚗 CMR Management System (Car Maintenance & Repair)

A robust, enterprise-grade Car Maintenance and Repair (CMR) Management System developed in Java. This system leverages modern software design patterns to provide a seamless experience for managing customer records, scheduling appointments, generating diagnostic/service reports, and handling billing.

## ✨ Key Features

- **👤 Customer Management**: Register different types of customers (Private, Fleet, and Staff) with specialized logic for each.
- **📅 Appointment Scheduling**: Efficiently schedule and manage vehicle maintenance slots.
- **🛠️ Service & Diagnostic Reports**: Detailed tracking of repairs, parts used, and technical recommendations.
- **💳 Billing System**: Automated bill generation with support for multiple payment methods including Cash and Credit Card.
- **📊 Daily Reporting**: Generate comprehensive daily summaries of all serviced vehicles and financial transactions.
- **🧩 Design Patterns**: Implements **Facade**, **Factory**, and **Iterator** patterns for clean, maintainable, and scalable code.

## 📸 System Screenshots

| Overview | Billing Module | Reports |
| :---: | :---: | :---: |
| ![Overview](./screenshots/overview.png) | ![Billing](./screenshots/billing.png) | ![Report](./screenshots/report.png) |

*(Please place your screenshots in a folder named `screenshots` or update the paths above)*

## 📂 Project Structure

- `CMRManagementSystem.java`: The central **Facade** that coordinates all system operations.
- `CustomerFactory.java`: Implements the **Factory Pattern** to create different customer types.
- `AppointmentIterator.java`: Uses the **Iterator Pattern** to traverse appointment records efficiently.
- `Bill.java` & `PaymentMethod.java`: Handles the financial logic and payment processing.
- `ReportGenerator.java`: Logic for creating detailed daily and service reports.

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher.
- A terminal or your favorite IDE (VS Code, IntelliJ, or Eclipse).

### Running the System
#### 🖥️ GUI Version (Recommended)
1. Compile the Java files:
   ```bash
   javac *.java
   ```
2. Run the GUI application:
   ```bash
   java CMRGui
   ```

#### ⌨️ Console Version
1. Compile the Java files:
   ```bash
   javac *.java
   ```
2. Run the main application:
   ```bash
   java Main
   ```

## 🛠️ Built With
- **Java**: Core programming language.
- **Design Patterns**: Facade, Factory, Iterator, Strategy.
- **File I/O**: Automated report logging to `.txt` files.

---
*Developed as part of the Advanced Software Engineering coursework.*
