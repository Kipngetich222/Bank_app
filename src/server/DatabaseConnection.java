package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Bankdatabase?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root"; 
    private static final String PASSWORD = "PHW#84#vic"; 

    // Method to get a new connection
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL driver (only needed once)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create a new connection each time
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found!", e);
        }
    }
}
