# 🚇 Metro Route and Fare Calculator 📧

## Overview

The Metro Route and Fare Calculator is a Java Swing-based desktop application designed to help users find optimal metro routes, calculate fares, and book tickets. It features a user-friendly graphical interface, an interactive map view, and the ability to send ticket confirmations via **email, replacing the previous SMS functionality**.

## Features ✨

* **Welcome Screen:** A friendly splash screen greets users upon application launch. 👋
* **Interactive Map View:** Visualizes the metro network with different colored lines for various routes and clearly labeled station names. 🗺️🌈
* **Route Calculation:** Finds and displays the **top 5 shortest/fastest routes** between any two selected metro stations. 🚀
* **Fare Calculation:** Calculates the fare for each route based on distance, with an optional **15% discount** for students/seniors. 🎓👵
* **Estimated Travel Time:** Shows the estimated travel time in **minutes** for each route. ⏱️ (Assumes 1 unit of distance = 1 minute of travel time)
* **Interchange Information:** Clearly highlights stations where a change of metro line is required within a route. 🔄
* **Date Picker:** A custom calendar interface for easy and accurate selection of the travel date, preventing selection of past dates. 📅
* **Ticket Booking System:**
    * Prompts for customer name and recipient email address. 📝
    * Validates customer name (letters and spaces only) and email format. ✅
    * Confirms booking details and final fare before finalizing.
    * Saves ticket information to a MySQL database. 💾
    * Sends a detailed ticket confirmation **email** to the customer's provided email address. 📧
    * Includes a "Have a safe journey and visit again!" message. 🎉

## Technologies Used 💻

* **Java (Swing):** For the desktop application GUI. 🎨
* **MySQL Database:** For persistent storage of ticket booking information. 🗄️
* **JDBC (Java Database Connectivity):** To facilitate communication between the Java application and the MySQL database. 🔗
* **Jakarta Mail API:** For **sending email confirmations** from the application. ✉️

## Project Structure 🏗️

The project follows a modular Java structure. You should be running commands from the `MetroRouteApp` directory.
# Project Directory: MetroRouteApp

```python
MetroRouteApp/
│
├── .vscode/                     # VS Code specific settings (optional)
│   ├── launch.json
│   └── settings.json
│
├── bin/                         # Compiled .class files will be stored here
│
├── lib/                         # External JAR libraries
│   ├── jakarta.activation-api-2.1.3.jar
│   ├── jakarta.mail-2.0.1.jar
│   └── mysql-connector-j-9.4.0.jar
│
└── MetroApp/                    # Source code package
    ├── EmailSender.java         # Handles sending emails
    ├── Graph_M.java             # Manages metro graph logic (stations, routes, distances)
    ├── MapView.java             # Renders the metro map GUI
    └── MetroApp.java            # Main application, GUI setup, and business logic
```

* **`bin/`**: This directory is where the compiled `.class` files will be placed after successful compilation.
* **`lib/`**: This directory holds all the external JAR libraries required by the project.
* **`MetroApp/`**: This is the main package containing all the Java source (`.java`) files for your application.
    * `EmailSender.java`: Manages the logic for sending email confirmations.
    * `Graph_M.java`: Defines the metro graph structure, stations, edges, and implements pathfinding algorithms.
    * `MapView.java`: Handles the graphical rendering of the metro map.
    * `MetroApp.java`: The main application class that sets up the GUI, handles user interactions, and integrates all other components.

## Setup Instructions 🛠️

To set up and run this project locally, please follow these detailed steps:

### 1. Prerequisites ✅

* **Java Development Kit (JDK) 8 or higher:** [Download and Install JDK](https://www.oracle.com/java/technologies/downloads/) ☕
* **MySQL Database Server:** [Download and Install MySQL Community Server](https://dev.mysql.com/downloads/mysql/) 🗄️
* **External Libraries (JAR Files):**
    * **MySQL JDBC Connector (v9.4.0):** Place `mysql-connector-j-9.4.0.jar` into your `lib/` folder.
        * [Download from MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)
    * **Jakarta Mail API (v2.0.1):** Place `jakarta.mail-2.0.1.jar` into your `lib/` folder.
        * [Download from Maven Central](https://search.maven.org/artifact/com.sun.mail/jakarta.mail) (Look for version `2.0.1` and download `jakarta.mail-2.0.1.jar`)
    * **Jakarta Activation API (v2.1.3):** Place `jakarta.activation-api-2.1.3.jar` into your `lib/` folder.
        * [Download from Maven Central](https://search.maven.org/artifact/jakarta.activation/jakarta.activation-api) (Look for version `2.1.3` and download `jakarta.activation-api-2.1.3.jar`)

    *Ensure your `lib` folder contains exactly these three JAR files with their correct names as shown in the project structure.*

### 2. Database Setup (MySQL) 📊

1.  **Create Database:** Open your MySQL client (e.g., MySQL Workbench, command line) and execute:
    ```sql
    CREATE DATABASE metro;
    USE metro;
    ```
2.  **Create Tickets Table:** Create the `tickets` table with the following schema (this includes all necessary columns for the application):
    ```sql
    CREATE TABLE tickets (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        source VARCHAR(255) NOT NULL,
        destination VARCHAR(255) NOT NULL,
        fare INT NOT NULL,
        email VARCHAR(255) NOT NULL, -- Changed from 'phone' to 'email'
        travel_date VARCHAR(10) NOT NULL,
        booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );
    ```
    *If `tickets` table already exists, drop it first using `DROP TABLE tickets;` or use `ALTER TABLE` commands to add missing columns based on previous instructions.*
3.  **Update Database Credentials:** In `MetroApp/MetroApp.java`, ensure the database connection details are correct for your MySQL setup:
    ```java
    // Line around 450:
    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/metro", "root", "Aman1511@")) {
        // Change "localhost:3306" if your MySQL is on a different host/port.
        // Change "root" to your MySQL username.
        // Change "Aman1511@" to your MySQL password.
    }
    ```

### 3. Email Sender Configuration 📧

The application is configured to send emails from a specific Gmail account.

1.  **Open `MetroApp/EmailSender.java`**.
2.  **Locate the `MetroApp/MetroApp.java` file.**
3.  **Update Sender Credentials:** Modify the `senderEmail` and `senderPassword` within the `bookTicketBtn`'s `actionPerformed` method in `MetroApp/MetroApp.java`.
    * **`senderEmail`**: Set this to `amansrivastava1511@gmail.com`.
    * **`senderPassword`**: This **MUST be an App Password** generated from your Google Account (for `amansrivastava1511@gmail.com`). Your regular Gmail password will not work.
        * **How to get an App Password (for Gmail):**
            1.  Ensure 2-Step Verification is enabled on your Google Account.
            2.  Go to [Google Account Security](https://myaccount.google.com/security) -> "App passwords".
            3.  Select "Mail" as the app and "Other (Custom name)" for the device (e.g., "Metro App").
            4.  Generate the 16-character password and **copy it immediately** (it's shown only once).
            5.  Use this copied password for `senderPassword` in your code.
    ```java
    // In MetroApp.java, inside bookTicketBtn.addActionListener:
    final String senderEmail = "amansrivastava1511@gmail.com";
    final String senderPassword = "YOUR_16_CHARACTER_APP_PASSWORD_HERE"; // REPLACE THIS!
    final String smtpHost = "smtp.gmail.com";
    final String smtpPort = "587";
    ```

### 4. Compile and Run the Application 🚀

1.  **Navigate to Project Root:** Open your terminal (e.g., VS Code Terminal, Command Prompt, PowerShell) and navigate to the `MetroRouteApp` directory (e.g., `C:\Projects\MetroRouteApp`).

2.  **Compile Java Code:** Use the following command. This compiles all `.java` files in the `MetroApp` folder and places the `.class` files into the `bin` directory, correctly referencing all libraries.

    ```bash
    javac -cp "lib\mysql-connector-j-9.4.0.jar;lib\jakarta.mail-2.0.1.jar;lib\jakarta.activation-api-2.1.3.jar" -d bin MetroApp\*.java
    ```
    * (If `MetroApp\*.java` doesn't work in your shell, explicitly list files: `MetroApp\MetroApp.java MetroApp\Graph_M.java MetroApp\MapView.java MetroApp\EmailSender.java`)

3.  **Run the Application:** After successful compilation, execute the application:

    ```bash
    java -cp "bin;lib\mysql-connector-j-9.4.0.jar;lib\jakarta.mail-2.0.1.jar;lib\jakarta.activation-api-2.1.3.jar" MetroApp.MetroApp
    ```

The application should now launch, allow you to book tickets, and send email confirmations.

---

## Contributing 🤝

Feel free to fork this repository, contribute, and suggest improvements! Pull requests are welcome.

## License 📄

This project is open-source and available under the [MIT License](LICENSE).
