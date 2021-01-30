package service;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = System.getenv("DB_CONNECTION_STRING");

    /**
     * Get a connection to database
     * @return Connection object
     */
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        DriverManager.registerDriver(new SQLServerDriver());
        try {
            Connection connection;
            connection = DriverManager.getConnection(URL);
            return connection;
        } catch (SQLException ex) {
            throw new RuntimeException("Error connecting to the database", ex);
        }
    }
}
