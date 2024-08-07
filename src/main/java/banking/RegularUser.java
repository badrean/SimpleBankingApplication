package banking;

import java.sql.SQLException;
import java.util.Scanner;

public class RegularUser extends User {
    private RegularOperationsController operationsController;
    public RegularUser(String username, DatabaseManager manager) {
        super(username, RoleType.USER, manager);
        this.operationsController = new RegularOperationsController(manager);
    }

    @Override
    public void displayMenu(Scanner scanner) {
        System.out.println("Welcome " + getUsername() + "!");
        boolean loggedIn = true;

        while(loggedIn) {
            System.out.println("\nSelect one:\n" +
                    "1. Create new account\n" +
                    "2. View accounts\n" +
                    "3. Deposit\n" +
                    "4. Withdraw\n" +
                    "5. Transfer\n" +
                    "6. Close account\n" +
                    "7. View transactions\n" +
                    "8. Logout");

            int option = scanner.nextInt();
            scanner.nextLine();

            switch(option) {
                case 1: // Create account
                    System.out.println("Select currency:");

                    boolean currencySelected = false;

                    while(!currencySelected) {
                        System.out.println("1. EUR\n2. USD\n3. RON");
                        int curr = scanner.nextInt();
                        scanner.nextLine();

                        try {
                            if (1 == curr) {
                                currencySelected = true;
                                operationsController.createAccount(getUsername(), CurrencyType.EUR);
                            }

                            if (2 == curr) {
                                currencySelected = true;
                                operationsController.createAccount(getUsername(), CurrencyType.USD);
                            }

                            if (3 == curr) {
                                currencySelected = true;
                                operationsController.createAccount(getUsername(), CurrencyType.RON);
                            }
                        } catch (SQLException exception) {
                            System.err.println("Database error: " + exception.getMessage());
                            exception.printStackTrace();
                        }
                    }

                    break;
                case 2: // View accounts
                    try {
                        operationsController.viewAccounts(getUsername());
                    } catch (SQLException exception) {
                        System.err.println("Database error: " + exception.getMessage());
                        exception.printStackTrace();
                    }

                    break;
                case 3: // Deposit
                    System.out.println("Account number: ");
                    int accountNumber = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Amount: ");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();

                    try {
                        operationsController.depositMoney(getUsername(), accountNumber, amount);
                    } catch (SQLException exception) {
                        System.err.println("Database error: " + exception.getMessage());
                        exception.printStackTrace();
                    }

                    break;
                case 4: // Withdraw
                    System.out.println("Account number: ");
                    int withdrawAccountNumber = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Amount: ");
                    double witdrawAmount = scanner.nextDouble();
                    scanner.nextLine();

                    try {
                        operationsController.withdrawMoney(getUsername(), withdrawAccountNumber, witdrawAmount);
                    } catch (SQLException exception) {
                        System.err.println("Database error: " + exception.getMessage());
                        exception.printStackTrace();
                    }

                    break;
                case 5:
                    System.out.println("Your account number: ");
                    int senderAccountNumber = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Amount: ");
                    double senderAmount = scanner.nextDouble();
                    scanner.nextLine();

                    System.out.println("Receiver account number: ");
                    int receiverAccountNumber = scanner.nextInt();
                    scanner.nextLine();

                    System.out.println("Receiver username: ");
                    String receiverUsername = scanner.nextLine();

                    try {
                        operationsController.transferMoney(getUsername(), receiverUsername, senderAccountNumber, receiverAccountNumber, senderAmount);
                    } catch (SQLException exception) {
                        System.err.println("Database error: " + exception.getMessage());
                        exception.printStackTrace();
                    }
                    break;
                case 6:
                    System.out.println("Account number: ");
                    int deletedAccountNumber = scanner.nextInt();
                    scanner.nextLine();

                    try {
                        operationsController.closeAccount(getUsername(), deletedAccountNumber);
                    } catch (SQLException exception) {
                        System.err.println("Database error: " + exception.getMessage());
                        exception.printStackTrace();
                    }
                    break;
                case 7:
                    try {
                        operationsController.viewTransactions(getUsername());
                    } catch (SQLException exception) {
                        System.err.println("Database error: " + exception.getMessage());
                        exception.printStackTrace();
                    }
                    break;
                case 8:
                    loggedIn = false;
                    System.out.println("Bye " + getUsername() + "!");
                    break;
                default:
                    System.out.println("Invalid input!");
                    break;
            }
        }
    }
}
