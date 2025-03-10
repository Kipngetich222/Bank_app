package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://192.168.137.10:3306/Bankdatabase?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "bank_admin"; 
    private static final String PASSWORD = "KISII_Bank@254"; 

    // Method to get a new connection
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL driver 
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Create a new connection each time
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found!", e);
        }
    }
}
