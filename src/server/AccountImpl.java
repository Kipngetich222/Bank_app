package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import common.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountImpl extends UnicastRemoteObject implements Account {
    private String accountNumber;
    private Connection connection; // Database connection passed from BankServer

    public AccountImpl(Connection connection, String accountNumber) throws RemoteException {
        super();
        this.connection = connection;
        this.accountNumber = accountNumber;
    }

    private double getBalanceFromDB() throws SQLException {
        String query = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        }
        return 0.0;
    }

    private void updateBalanceInDB(double newBalance) throws SQLException {
        String query = "UPDATE accounts SET balance = ? WHERE account_number = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);
            stmt.executeUpdate();
        }
    }

    private void logTransaction(String type, double amount) throws SQLException {
        String query = "INSERT INTO transactions (account_number, transaction_type, amount, transaction_date) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, accountNumber);
            stmt.setString(2, type);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        }
    }

    @Override
    public void deposit(double amount) throws RemoteException {
        try {
            double newBalance = getBalanceFromDB() + amount;
            updateBalanceInDB(newBalance);
            logTransaction("deposit", amount);
            System.out.println("Deposited: " + amount + ", New Balance: " + newBalance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean withdraw(double amount) throws RemoteException {
        try {
            double currentBalance = getBalanceFromDB();
            if (amount <= currentBalance) {
                double newBalance = currentBalance - amount;
                updateBalanceInDB(newBalance);
                logTransaction("withdraw", amount);
                System.out.println("Withdrawn: " + amount + ", New Balance: " + newBalance);
                return true;
            } else {
                System.out.println("Insufficient funds for withdrawal.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double getBalance() throws RemoteException {
        try {
            return getBalanceFromDB();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    @Override
    public String getAccountNumber() throws RemoteException {
        return accountNumber;
    }

    @Override
public List<String> getTransactionHistory() throws RemoteException {
    List<String> transactions = new ArrayList<>();
    String query = "SELECT transaction_type, amount, transaction_date FROM transactions WHERE account_number = ? ORDER BY transaction_date DESC";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setString(1, accountNumber);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            String transaction = rs.getString("transaction_date") + " - " +
                                 rs.getString("transaction_type") + ": " +
                                 rs.getDouble("amount");
            transactions.add(transaction);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return transactions;
}
}
