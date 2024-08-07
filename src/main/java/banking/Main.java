package banking;

import java.io.Console;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        try {
            DatabaseManager dbManager = new DatabaseManager();
            UserOperations userOperations = new UserOperations(dbManager);

            System.out.print("\nWelcome to Banking App!\nPlease select an option:");

            while(true){
                System.out.print("\n1. Register\n2. Login\n3. Exit\n");
                int option = userInput.nextInt();
                userInput.nextLine();

                String username = "";
                String password = "";

                switch (option) {
                    case 1:
                        System.out.print("\nEnter username: ");
                        username = userInput.nextLine();
                        System.out.print("\nEnter password: ");
                        password = userInput.nextLine();

                        try {
                            userOperations.registerUser(username, password);
                        } catch (SQLException exception) {
                            System.err.println("Registration failed: " + exception.getMessage());
                            exception.printStackTrace();
                        }

                        System.out.print("Registered successfully!\n");
                        break;

                    case 2:
                        System.out.print("\nEnter username: ");
                        username = userInput.nextLine();
                        System.out.print("\nEnter password: ");
                        password = userInput.nextLine();

                        User user = null;
                        try {
                            user = userOperations.loginUser(username, password);
                        } catch (SQLException exception) {
                            System.err.println("Database error: " + exception.getMessage());
                            exception.printStackTrace();
                        }

                        if (user != null) {
                            System.out.print("Login successful\n");
                            user.displayMenu(userInput);
                        }
                        else {
                            System.out.print("Login failed\n");
                        }

                        break;

                    case 3:
                        System.out.println("Exiting...\n");
                        userInput.close();
                        System.exit(0);

                    default:
                        System.out.print("Invalid input!");
                }
            }
        } catch (SQLException exception) {
            System.err.println("Database exception: " + exception.getMessage());
            exception.printStackTrace();
            System.exit(1);
        } catch (Exception exception) {
            System.err.println("Unexpected exception: " + exception.getMessage());
            exception.printStackTrace();
        }
    }
}