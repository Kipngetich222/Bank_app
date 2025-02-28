package server;

import common.Account;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;

public class BankServer {
    public static void main(String[] args) {
        try {
            // Get server IP dynamically
            String serverIP = InetAddress.getLocalHost().getHostAddress();
            int port = 3000; // RMI registry port

            // Ensures RMI registry is running
            try {
                Registry registry = LocateRegistry.getRegistry(port);
                registry.list(); // Test if registry responds
                System.out.println("RMI Registry already running on port " + port);
            } catch (Exception e) {
                LocateRegistry.createRegistry(port);
                System.out.println("RMI Registry started on port " + port);
            }

            // Database connection setup
            String url = "jdbc:mysql://localhost:3306/bankdatabase";
            String user = "root";  
            String password = "PHW#84#vic";  

            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully!");

            // Start the RMI service
            Account account = new AccountImpl(connection, "123456789");  // Test account
            String bindLocation = "rmi://" + serverIP + ":" + port + "/BankAccount";
            Naming.rebind(bindLocation, account);

            System.out.println("Server started at: " + bindLocation);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Server failed to start!");
        }
    }
}
