package banking;

import java.sql.*;

public class UserOperations {
    private DatabaseManager dbManager;

    public UserOperations(DatabaseManager manager) {
        this.dbManager = manager;
    }

    public void registerUser(String username, String password) throws SQLException {
        String hashedPassword = PasswordUtils.hashPassword(password);
        String query = "INSERT INTO users (username, password, role) VALUES (?,?,'user')";

        PreparedStatement statement = dbManager.getConnection().prepareStatement(query);
        statement.setString(1, username);
        statement.setString(2, hashedPassword);
        statement.executeUpdate();
    }

    public User loginUser(String username, String password) throws SQLException {
        String query = "SELECT password, role FROM users WHERE username = ?";

        PreparedStatement statement = dbManager.getConnection().prepareStatement(query);
        statement.setString(1, username);
        ResultSet result = statement.executeQuery();

        if (result.next()) {
            String hashedPassword = result.getString("password");
            String role = result.getString("role");

            if (!PasswordUtils.checkPassword(password, hashedPassword)) {
                return null;
            }

            if (role.equals("admin")) {
                return new AdminUser(username, dbManager);
            } else {
                return new RegularUser(username, dbManager);
            }
        } else {
            System.out.print("User not registered or wrong password.\n");
            return null;
        }
    }
}
