package banking;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

public class RegularOperationsController {
    private DatabaseManager dbManager;

    public RegularOperationsController(DatabaseManager manager){
        this.dbManager = manager;
    }

    public void createAccount(String username, CurrencyType currency) throws SQLException {
        String query = "INSERT INTO accounts (username, currency, balance) VALUES (?, ?, 0.00)";

        PreparedStatement statement = dbManager.getConnection().prepareStatement(query);
        statement.setString(1, username);

        String currencyStr = "";
        switch(currency) {
            case EUR:
                currencyStr = "EUR";
                break;
            case USD:
                currencyStr = "USD";
                break;
            case RON:
                currencyStr = "RON";
                break;
            default:
                System.out.print("Invalid input!");
                break;
        }
        statement.setString(2, currencyStr);
        statement.executeUpdate();

        System.out.println("Successfully created account!");
    }

    public void viewAccounts(String username) throws SQLException {
        String query = "SELECT account_id, currency, balance FROM accounts WHERE username = ?";
        PreparedStatement statement = dbManager.getConnection().prepareStatement(query);
        statement.setString(1, username);
        ResultSet result =  statement.executeQuery();

        while(result.next()) {
            String accountId = result.getString("account_id");
            String currency = result.getString("currency");
            Float balance = result.getFloat("balance");

            System.out.println("Acc number: " + accountId + " Curr: " + currency + " Balance: " + balance);
        }
    }

    public void depositMoney(String username, int accountId, double amount) throws SQLException{
        String selectQuery = "SELECT balance, currency FROM accounts WHERE account_id = ? AND username = ?";
        String updateQuery = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        PreparedStatement selectStatement = dbManager.getConnection().prepareStatement(selectQuery);
        PreparedStatement updateStatement = dbManager.getConnection().prepareStatement(updateQuery);

        double currentBalance, newBalance;

        selectStatement.setInt(1, accountId);
        selectStatement.setString(2, username);
        ResultSet resultSet = selectStatement.executeQuery();

        String currency = "";
        if (resultSet.next()) {
            currentBalance = resultSet.getDouble("balance");
            currency = resultSet.getString("currency");
        } else {
            System.out.println("Account not found.");
            return;
        }

        newBalance = currentBalance + amount;

        updateStatement.setDouble(1, newBalance);
        updateStatement.setInt(2, accountId);

        int rowsAffected = updateStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Update successful.");
        } else {
            System.out.println("No rows affected. Username or account might not exist.");
        }

        addTransactionToDatabase(accountId, amount, currency, TransactionType.DEPOSIT);
    }

    public void withdrawMoney(String username, int accountId, double amount) throws SQLException {
        String selectQuery = "SELECT balance, currency FROM accounts WHERE account_id = ? AND username = ?";
        String updateQuery = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        PreparedStatement selectStatement = dbManager.getConnection().prepareStatement(selectQuery);
        PreparedStatement updateStatement = dbManager.getConnection().prepareStatement(updateQuery);

        double currentBalance, newBalance;

        selectStatement.setInt(1, accountId);
        selectStatement.setString(2, username);
        ResultSet resultSet = selectStatement.executeQuery();

        String currency = "";
        if (resultSet.next()) {
            currentBalance = resultSet.getDouble("balance");
            currency = resultSet.getString("currency");
        } else {
            System.out.println("Account not found.");
            return;
        }

        if (amount > currentBalance) {
            System.out.println("Your balance is too low.");
            return;
        }

        newBalance = currentBalance - amount;

        updateStatement.setDouble(1, newBalance);
        updateStatement.setInt(2, accountId);

        int rowsAffected = updateStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Update successful.");
        } else {
            System.out.println("No rows affected. Username or account might not exist.");
        }

        addTransactionToDatabase(accountId, amount, currency, TransactionType.WITHDRAW);
    }

    public void transferMoney(String senderUsername, String receiverUsername, int senderAccountId, int receiverAccountId, double amount) throws SQLException {
        String selectSenderQuery = "SELECT balance, currency FROM accounts WHERE account_id = ? AND username = ?";
        String selectReceiverQuery = "SELECT balance, currency FROM accounts WHERE account_id = ? AND username = ?";
        String updateSenderQuery = "UPDATE accounts SET balance = ? WHERE account_id = ?";
        String updateReceiverQuery = "UPDATE accounts SET balance = ? WHERE account_id = ?";

        PreparedStatement selectSenderStatement = dbManager.getConnection().prepareStatement(selectSenderQuery);
        PreparedStatement selectReceiverStatement = dbManager.getConnection().prepareStatement(selectReceiverQuery);
        PreparedStatement updateSenderStatement = dbManager.getConnection().prepareStatement(updateSenderQuery);
        PreparedStatement updateReceiverStatement = dbManager.getConnection().prepareStatement(updateReceiverQuery);

        double senderCurrentBalance, receiverCurrentBalance, senderNewBalance, receiverNewBalance;

        selectSenderStatement.setInt(1, senderAccountId);
        selectSenderStatement.setString(2, senderUsername);
        selectReceiverStatement.setInt(1, receiverAccountId);
        selectReceiverStatement.setString(2, receiverUsername);

        ResultSet senderResultSet = selectSenderStatement.executeQuery();
        ResultSet receiverResultSet = selectReceiverStatement.executeQuery();

        String senderCurrency = "";
        if (senderResultSet.next() && receiverResultSet.next()) {
            senderCurrency = senderResultSet.getString("currency");
            String receiverCurrency = receiverResultSet.getString("currency");

            if (!senderCurrency.equals(receiverCurrency)) {
                System.out.println("Currencies do not match. Receiver uses " + receiverCurrency);
                return;
            }

            senderCurrentBalance = senderResultSet.getDouble("balance");
            receiverCurrentBalance = receiverResultSet.getDouble("balance");
        } else {
            System.out.println("Account not found.");
            return;
        }

        if (amount > senderCurrentBalance) {
            System.out.println("Your balance is too low.");
            return;
        }

        senderNewBalance = senderCurrentBalance - amount;
        receiverNewBalance = receiverCurrentBalance + amount;

        updateSenderStatement.setDouble(1, senderNewBalance);
        updateSenderStatement.setInt(2, senderAccountId);
        updateReceiverStatement.setDouble(1, receiverNewBalance);
        updateReceiverStatement.setInt(2, receiverAccountId);

        int senderRowsAffected = updateSenderStatement.executeUpdate();
        int receiverRowsAffected = updateReceiverStatement.executeUpdate();

        if (senderRowsAffected > 0 && receiverRowsAffected > 0) {
            System.out.println("Update successful.");
        } else {
            System.out.println("No rows affected.");
        }

        addTransactionToDatabase(senderAccountId, receiverAccountId, amount, senderCurrency);
    }

    public void closeAccount(String username, int accountId) throws SQLException {
        String query = "DELETE FROM accounts WHERE username = ? AND account_id = ?";
        PreparedStatement statement = dbManager.getConnection().prepareStatement(query);

        statement.setString(1, username);
        statement.setInt(2, accountId);

        int rowsAffected = statement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Account " + accountId + " deleted successfully.");
        } else {
            System.out.println("Delete failed. Account might not exist.");
        }
    }

    private void addTransactionToDatabase(int fromAccountId, int toAccountId, double amount, String currency) throws SQLException{
        String query = "INSERT INTO transactions (account_id_from, account_id_to, amount, currency, transaction_date, transaction_type) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = dbManager.getConnection().prepareStatement(query);

        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);

        String transactionType = "transfer";

        statement.setInt(1, fromAccountId);
        statement.setInt(2, toAccountId);
        statement.setDouble(3, amount);
        statement.setString(4, currency);
        statement.setTimestamp(5, timestamp);
        statement.setString(6, transactionType);

        statement.executeUpdate();
    }

    private void addTransactionToDatabase(int fromAccountId, double amount, String currency, TransactionType transaction) throws SQLException{
        String query = "INSERT INTO transactions (account_id_from, account_id_to, amount, currency, transaction_date, transaction_type) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = dbManager.getConnection().prepareStatement(query);

        LocalDateTime currentDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentDateTime);

        String transactionType = "";
        switch(transaction) {
            case DEPOSIT:
                transactionType = "deposit";
                break;
            case WITHDRAW:
                transactionType = "withdraw";
                break;
            default:
                transactionType = "unknown";
                break;
        }

        statement.setInt(1, fromAccountId);
        statement.setNull(2, Types.INTEGER);
        statement.setDouble(3, amount);
        statement.setString(4, currency);
        statement.setTimestamp(5, timestamp);
        statement.setString(6, transactionType);

        statement.executeUpdate();
    }
}
