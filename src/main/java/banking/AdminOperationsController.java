package banking;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminOperationsController {
    private DatabaseManager dbManager;

    public AdminOperationsController(DatabaseManager manager){
        this.dbManager = manager;
    }

    public void viewAccounts() throws SQLException {
        String query = "SELECT account_id, username, currency, balance FROM accounts";
        PreparedStatement statement = dbManager.getConnection().prepareStatement(query);

        ResultSet result =  statement.executeQuery();

        while(result.next()) {
            String accountId = result.getString("account_id");
            String username = result.getString("username");
            String currency = result.getString("currency");
            Float balance = result.getFloat("balance");

            System.out.println("Acc number: " + accountId + "\n" +
                    " User: " + username + "\n" +
                    " Curr: " + currency + "\n" +
                    " Balance: " + balance + "\n" +
                    "----------------");
        }
    }

    public void viewUsers() throws SQLException {
        String query = "SELECT username from users";
        PreparedStatement statement = dbManager.getConnection().prepareStatement(query);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String username = resultSet.getString("username");

            System.out.println(username);
        }
    }

    public void changeAccountBalance(int accountId, double newAmount) throws SQLException {
        String updateQuery = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        PreparedStatement updateStatement = dbManager.getConnection().prepareStatement(updateQuery);

        updateStatement.setDouble(1, newAmount);
        updateStatement.setInt(2, accountId);

        int rowsAffected = updateStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Update successful.");
        } else {
            System.out.println("No rows affected. Username or account might not exist.");
        }
    }

    public void deleteAccount(int accountId) throws SQLException {
        String query = "DELETE FROM accounts WHERE account_id = ?";
        PreparedStatement statement = dbManager.getConnection().prepareStatement(query);

        statement.setInt(1, accountId);

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Account " + accountId + " deleted successfully.");
        } else {
            System.out.println("Delete failed. Account might not exist.");
        }
    }

    public void deleteUser(String username) throws SQLException {
        String selectAccountsQuery = "SELECT account_id FROM accounts WHERE username = ?";
        String accountsQuery = "DELETE FROM accounts WHERE username = ?";
        String userQuery = "DELETE FROM users WHERE username = ?";

        PreparedStatement selectAccountsStatement = dbManager.getConnection().prepareStatement(selectAccountsQuery);
        PreparedStatement accountsStatement = dbManager.getConnection().prepareStatement(accountsQuery);
        PreparedStatement userStatement = dbManager.getConnection().prepareStatement(userQuery);

        selectAccountsStatement.setString(1, username);
        ResultSet selectionResult = selectAccountsStatement.executeQuery();

        List<Integer> accounts = new ArrayList<>();
        while (selectionResult.next()) {
            int account = selectionResult.getInt("account_id");
            accounts.add(account);
        }

        StringBuilder transactionsQueryBuilder = new StringBuilder();
        transactionsQueryBuilder.append("DELETE FROM transactions WHERE account_id_from IN (");
        for (int i = 0; i < accounts.size(); i++) {
            transactionsQueryBuilder.append("?");
            if (i < accounts.size() - 1) {
                transactionsQueryBuilder.append(",");
            }
        }
        transactionsQueryBuilder.append(") OR account_id_to IN (");
        for (int i = 0; i < accounts.size(); i++) {
            transactionsQueryBuilder.append("?");
            if (i < accounts.size() - 1) {
                transactionsQueryBuilder.append(",");
            }
        }
        transactionsQueryBuilder.append(")");

        PreparedStatement transactionsStatement = dbManager.getConnection().prepareStatement(transactionsQueryBuilder.toString());

        int index = 1;
        for (int account : accounts) {
            transactionsStatement.setInt(index++, account);
        }
        for (int account : accounts) {
            transactionsStatement.setInt(index++, account);
        }

        int transactionsDeleted = transactionsStatement.executeUpdate();

        accountsStatement.setString(1, username);
        int accountsDeleted = accountsStatement.executeUpdate();

        userStatement.setString(1, username);
        int userDeleted = userStatement.executeUpdate();

        if (userDeleted > 0) {
            System.out.println("User and associated accounts deleted successfully.");
        } else {
            System.out.println("No user found with the given username.");
        }
    }
}
