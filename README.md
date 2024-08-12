# Simple Banking Application

## Introduction
This is a simple banking application developed in Java, utilizing OOP principles. The application is designed to run from the console, providing a basic interface for users and administrators to manage banking operations. It connects to a MySQL database to store and manage data related to users, accounts, and transactions. The application has separate functionalities for regular users and administrators.

## Features

### User Features
After logging in, users can perform the following operations:

1. <b>Create New Account:</b> Users can open a new bank account with a specified currency.
2. <b>View Accounts:</b> Users can view a list of all their bank accounts and check their balances.
3. <b>Deposit:</b> Users can deposit money into their specified account.
4. <b>Withdraw:</b> Users can withdraw money from their specified account, provided there is enough balance.
5. <b>Transfer:</b> Users can transfer money between their accounts or to other user accounts.
6. <b>Close Account:</b> Users may close any of their bank accounts, removing it from the system.
7. <b>View Transactions:</b> Users can view a history of all transactions associated with their accounts.
8. <b>Logout:</b> Users can log out of the application.

### Admin Features
Administrators have enhanced capabilities to manage the banking system:

1. <b>View Accounts:</b> Admins can view details of all accounts in the system.
2. <b>View Users:</b> Admins can see a list of all registered users in the system.
3. <b>Change Account Balance:</b> Admins can modify the balance of any account.
4. <b>Delete User:</b> Admins can remove a user from the system, which also deletes their associated accounts and transactions.
5. <b>Delete Account:</b> Admins can delete any account from the system.
6. <b>Logout:</b> Admins can log out of the application.

## Database Structure
The application uses a MySQL database with the following tables:

* <b>Users Table:</b> Stores user information, including their username, password, and role (admin or user).
  ```SQL
  CREATE TABLE `users` (
    `id` int NOT NULL AUTO_INCREMENT,
    `username` varchar(20) NOT NULL,
    `password` varchar(60) NOT NULL,
    `role` varchar(10) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
  )
  ```

* <b>Accounts Table:</b> Stores details of bank accounts, including the username of the account holder, currency type, and balance.
  ```SQL
  CREATE TABLE `accounts` (
    `account_id` int NOT NULL AUTO_INCREMENT,
    `username` varchar(20) DEFAULT NULL,
    `currency` varchar(3) NOT NULL,
    `balance` decimal(10,2) DEFAULT NULL,
    PRIMARY KEY (`account_id`),
    FOREIGN KEY (`username`) REFERENCES `users` (`username`)
  )
  ```

* <b>Transactions Table:</b> Stores transaction details, including the account involved, transaction type, and amount.
  ```SQL
  CREATE TABLE `transactions` (
    `transaction_id` int NOT NULL AUTO_INCREMENT,
    `account_id_from` int NOT NULL,
    `account_id_to` int DEFAULT NULL,
    `amount` decimal(10,2) DEFAULT NULL,
    `currency` varchar(3) DEFAULT NULL,
    `transaction_date` datetime DEFAULT NULL,
    `transaction_type` varchar(20) NOT NULL,
    PRIMARY KEY (`transaction_id`),
    FOREIGN KEY (`account_id_from`) REFERENCES `accounts` (`account_id`),
    FOREIGN KEY (`account_id_to`) REFERENCES `accounts` (`account_id`)
  )
  ```

## Getting Started
### Prerequisites
  * JDK 8 or higher
  * MySQL Server
  * JDBC Driver for MySQL

### Installation
1. <b>Clone the repository:</b>
  ```
  git clone git@github.com:badrean/SimpleBankingApplication.git
  cd SimpleBankingApplication
  ```

2. <b>Set Up the Database:</b>
  * Create a MySQL database and import the provided SQL scripts to create the necessary tables.
  * Update the database connection details in the application's configuration file (src/main/resources/application.properties).

3. <b>Build and Run the Application:</b>
   * Compile the Project:
     ```
       mvn clean compile
     ```
   * Run the Application:
     ```
       mvn exec:java -Dexec.mainClass="com.yourpackage.Main"
     ```
### Usage
* <b>Admin Login:</b> Use the credentials of an admin user to log in and access administrative features.
* <b>User Login:</b> After registering, log in as a regular user to manage your bank accounts.

## Future Enhancements
* Add GUI
* Modify architecture to be client-server so it can be used as a web app.

## Contributing
Contributions are welcome!

## License
This project is licensed under the MIT License - see the [LICENSE](https://github.com/badrean/SimpleBankingApplication/blob/main/LICENSE) file for details.
