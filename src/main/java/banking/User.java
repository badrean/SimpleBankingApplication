package banking;

import java.util.Scanner;

public abstract class User {
    private String username;
    private RoleType role;
    private DatabaseManager dbManager;

    public User(String username, RoleType role, DatabaseManager manager) {
        this.username = username;
        this.role = role;
        this.dbManager = manager;
    }

    public String getUsername() {
        return username;
    }

    public RoleType getRole() {
        return role;
    }

    public DatabaseManager getDbManager() {
        return dbManager;
    }

    public abstract void displayMenu(Scanner scanner);
}
