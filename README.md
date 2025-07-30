# 🚇 Metro Route and Fare Calculator 💰

## Overview

The Metro Route and Fare Calculator is a Java Swing-based desktop application designed to help users find metro routes 🗺️, calculate fares 💷, view station maps 📍, and book tickets 🎟️ for a simplified metro network. It provides a user-friendly interface with features like a welcoming splash screen 👋, detailed route information including interchanges 🔄 and estimated travel time ⏱️, and ticket booking with SMS confirmation 📱.

## Features ✨

* **Welcome Screen:** A visually appealing splash screen upon application launch. 👋
* **Interactive Map View:** Displays the metro network with different colored lines for various routes 🌈 and clearly visible station names. 📍
* **Route Calculation:** Finds and displays the top 5 shortest/fastest routes between any two selected metro stations. 🚀
* **Fare Calculation:** Calculates the fare for each route based on distance, with an optional 15% discount for students/seniors. 🎓👵
* **Estimated Travel Time:** Shows the estimated travel time in minutes for each route. ⏱️ (Assumes 1 unit of distance = 2 minutes of travel time)
* **Interchange Information:** Highlights interchange stations within a route. 🔄
* **Ticket Booking System:**
    * Allows users to input their name, phone number, and date of travel. 📝
    * Confirms booking details and fare before finalizing. ✅
    * Saves ticket information to a MySQL database. 💾
    * Sends an SMS confirmation to the user's phone number via the Fast2SMS API. 💬
    * Provides a "Have a safe journey and visit again!" message upon successful booking. 🎉

## Technologies Used 💻

* **Java (Swing):** For the desktop application GUI. 🎨
* **MySQL:** For database storage of ticket information. 🗄️
* **JDBC (Java Database Connectivity):** To connect the Java application with the MySQL database. 🔗
* **Fast2SMS API:** For sending SMS confirmations. ✉️

## Setup Instructions 🛠️

To set up and run this project locally, follow these steps:

### 1. Prerequisites ✅

* **Java Development Kit (JDK) 8 or higher:** [Download and Install JDK](https://www.oracle.com/java/technologies/downloads/) ☕
* **MySQL Database Server:** [Download and Install MySQL Community Server](https://dev.mysql.com/downloads/mysql/) 🗃️
* **MySQL JDBC Connector JAR:** This allows Java applications to connect to MySQL.
    * Download `mysql-connector-java-x.x.x.jar` (e.g., `mysql-connector-j-9.4.0.jar`) from the [MySQL Connector/J Downloads page](https://dev.mysql.com/downloads/connector/j/). Place this `.jar` file in a `lib` directory within your project root (e.g., `MetroRouteApp/lib/`). 📥
* **Fast2SMS API Key:**
    * Go to [Fast2SMS](https://www.fast2sms.com/) and create an account. 🌐
    * After logging in, you can find your API key on your dashboard or in the API documentation section. 🔑
    * **Important:** Replace `"vTGXAfypgIC1x48eaSJK9Du6FQRd0crlzmHOo7iq3WE5wV2BbM2RLDcjliNTkhreHPZ4woFVBt6dnb5X"` in `src/MetroApp/SMSSender.java` with your actual Fast2SMS API key. ⚠️

### 2. Database Setup (MySQL) 📊

1.  **Create Database:** Open your MySQL client (e.g., MySQL Workbench, command line) and create a new database named `metro`:
    ```sql
    CREATE DATABASE metro;
    USE metro;
    ```

2.  **Create Tickets Table:** Create a table named `tickets` within the `metro` database:
    ```sql
    CREATE TABLE tickets (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        source VARCHAR(255) NOT NULL,
        destination VARCHAR(255) NOT NULL,
        fare INT NOT NULL,
        phone VARCHAR(20) NOT NULL,
        travel_date VARCHAR(10) NOT NULL, -- Stored as YYYY-MM-DD
        booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    ```

3.  **Update Database Credentials:** In `src/MetroApp/MetroApp.java`, ensure the database connection details are correct for your MySQL setup:
    ```java
    // Line around 330:
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/metro", "root", "SQL-password")) {
        // Change "localhost:3306" if your MySQL is on a different host/port.
        // Change "root" to your MySQL username.
        // Change "Aman1511@" to your MySQL password.
    }
    ```

### 3. Compile and Run 🚀

1.  **Navigate to Project Root:** Open your terminal or command prompt and navigate to the root directory of your project (e.g., `C:\Projects\MetroRouteApp`). 📁

2.  **Create `bin` Directory:** If it doesn't exist, create a `bin` directory to store compiled `.class` files:
    ```bash
    mkdir bin
    ```

3.  **Compile Java Code:** Compile the Java source files, including the MySQL JDBC connector in the classpath:
    ```bash
    javac -cp "lib/mysql-connector-j-9.4.0.jar" -d bin src/MetroApp/*.java
    ```
    * **Note:** Replace `mysql-connector-j-9.4.0.jar` with the actual filename of your downloaded JDBC connector. ⚠️

4.  **Run the Application:** After successful compilation, run the application from the `bin` directory:
    ```bash
    java -cp "bin;lib/mysql-connector-j-9.4.0.jar" MetroApp.MetroApp
    ```
    * **Note for Windows:** Use `;` as the classpath separator. 🖥️
    * **Note for macOS/Linux:** Use `:` as the classpath separator: 🍎🐧
        ```bash
        java -cp "bin:lib/mysql-connector-j-9.4.0.jar" MetroApp.MetroApp
        ```

## Project Structure 🏗️
