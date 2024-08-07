package banking;

import java.sql.SQLException;
import java.util.Scanner;

public class AdminUser extends User {
    private AdminOperationsController operationsController;

    public AdminUser(String username, DatabaseManager manager) {
        super(username, RoleType.ADMIN, manager);
        this.operationsController = new AdminOperationsController(manager);
    }

    @Override
    public void displayMenu(Scanner scanner) {
        System.out.println("Admin panel.");
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\nSelect one:\n" +
                    "1. View accounts\n" +
                    "2. View users\n" +
                    "3. Change account balance\n" +
                    "4. Delete user\n" +
                    "5. Delete account\n" +
                    "6. Logout");

            int option = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (option) {
                    case 1:
                        operationsController.viewAccounts();
                        break;
                    case 2:
                        operationsController.viewUsers();
                        break;
                    case 3:
                        System.out.println("Account number: ");
                        int accountNumber = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Amount: ");
                        double amount = scanner.nextDouble();
                        scanner.nextLine();

                        operationsController.changeAccountBalance(accountNumber, amount);
                        break;
                    case 4:
                        System.out.println("Username: ");
                        String username = scanner.nextLine();

                        operationsController.deleteUser(username);
                        break;
                    case 5:
                        System.out.println("Account number: ");
                        int deleteAccountNumber = scanner.nextInt();
                        scanner.nextLine();

                        operationsController.deleteAccount(deleteAccountNumber);
                        break;
                    case 6:
                        loggedIn = false;
                        System.out.println("Bye " + getUsername() + "!");
                        break;
                }
            } catch (SQLException exception) {
                System.err.println("Database error: " + exception.getMessage());
                exception.printStackTrace();
            }
        }
    }
}
