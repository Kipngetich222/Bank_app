package client;

import common.Account;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.Naming;

public class BankClient {
    private static Account account;
    private static JLabel labelBalance;

    public static void main(String[] args) {
        try {
            // Ask the user for the server IP or use localhost
            String serverIP = JOptionPane.showInputDialog("Enter server IP (or leave blank for localhost):");
            if (serverIP == null || serverIP.trim().isEmpty()) {
                serverIP = "127.0.0.1"; // Default to localhost
            }

            int port = 3000; // Ensure this matches the BankServer port

            // Lookup the remote Account object
            String registryURL = "//" + serverIP.trim() + ":" + port + "/BankAccount";
            account = (Account) Naming.lookup(registryURL);

            // Creating the GUI
            createGUI();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error connecting to server:\n" + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private static void createGUI() {
        JFrame frame = new JFrame("Kisii Bank");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(350, 300);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));

        // Balance label
        labelBalance = new JLabel();
        updateBalanceLabel();

        // Input fields
        JTextField amountField = new JTextField();

        // Status label
        JLabel statusLabel = new JLabel("Status: Waiting...");

        // Deposit button
        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener((ActionEvent e) -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount > 0) {
                    account.deposit(amount);
                    statusLabel.setText("Deposit successful.");
                    updateBalanceLabel();
                } else {
                    statusLabel.setText("Enter a valid amount.");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid amount entered.");
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Withdraw button
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener((ActionEvent e) -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount > 0) {
                    boolean success = account.withdraw(amount);
                    if (success) {
                        statusLabel.setText("Withdrawal successful.");
                        updateBalanceLabel();
                    } else {
                        statusLabel.setText("Insufficient funds.");
                    }
                } else {
                    statusLabel.setText("Enter a valid amount.");
                }
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid amount entered.");
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Adding components to panel
        panel.add(new JLabel("Current Balance:"));
        panel.add(labelBalance);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(new JLabel("Status:"));
        panel.add(statusLabel);

        // Adding panel to frame
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void updateBalanceLabel() {
        try {
            labelBalance.setText("Ksh " + account.getBalance());
        } catch (Exception e) {
            labelBalance.setText("Error fetching balance.");
            e.printStackTrace();
        }
    }
}
