package banking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() throws SQLException {
        Properties properties = new Properties();

        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (input == null) {
                throw new IllegalArgumentException("Unable to find config.properties");
            }
            properties.load(input);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new SQLException("Unable to load database configuration", exception);
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        this.connection = DriverManager.getConnection(url, user, password);
    }

    public Connection getConnection() {
        return connection;
    }
}
