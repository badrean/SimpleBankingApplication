package banking;

import java.util.Scanner;

public class AdminUser extends User {
    public AdminUser(String username, DatabaseManager manager) {
        super(username, RoleType.ADMIN, manager);
    }

    @Override
    public void displayMenu(Scanner scanner) {
        System.out.println("Congrats admin");
    }
}
