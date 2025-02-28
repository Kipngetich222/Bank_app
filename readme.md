## Distributed systems using Java RMI

# Steps for running the app
1. Compile Java files: 
  javac -d bin -cp "lib/mysql-connector-j-9.2.0.jar;bin" src/common/*.java src/server/*.java src/client/*.java
2. Start the Bank Server (which automatically starts RMI Registry on port 3000): 
  java -cp "lib/mysql-connector-j-9.2.0.jar;bin" server.BankServer
3. Start the Bank Client: 
  java -cp "bin" client.BankClient
