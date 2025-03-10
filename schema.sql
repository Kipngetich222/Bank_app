DROP DATABASE BankDatabase;
CREATE DATABASE BankDatabase;
USE BankDatabase;
CREATE TABLE accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    account_holder VARCHAR(100) NOT NULL,
    balance DOUBLE NOT NULL DEFAULT 0.0
);
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20),
    transaction_type ENUM('deposit', 'withdraw') NOT NULL,
    amount DOUBLE NOT NULL,
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (account_number) REFERENCES accounts(account_number) ON DELETE CASCADE
);
INSERT INTO accounts (account_number, account_holder, balance) 
VALUES ('123456789', 'John Doe', 5000.00);
INSERT INTO transactions (account_number, transaction_type, amount) 
VALUES ('123456789', 'deposit', 2000.00), 
       ('123456789', 'withdraw', 1000.00);
DROP user bank_admin@localhost;
CREATE USER 'bank_admin'@'%' IDENTIFIED BY "KISII_Bank@254";
GRANT ALL PRIVILEGES ON BankDatabase.* TO 'bank_admin'@'%';
-- end
