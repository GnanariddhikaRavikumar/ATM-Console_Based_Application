package newpackage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Initialize user and other necessary objects
        User user = new User("Ravi", "1234", 1000.0);
        TransactionHistory transactionHistory = new TransactionHistory();
        ATMOperations atmOperations = new ATMOperations(user, transactionHistory);

        // Authenticate user
        if (authenticateUser(scanner, user)) {
            // User authenticated successfully
            int choice;
            do {
                // Display menu and get user choice
                displayMenu();
                choice = scanner.nextInt();

                // Perform operation based on user choice
                atmOperations.performOperation(choice);

            } while (choice != 6); // Quit when user chooses option 6
        } else {
            System.out.println("Authentication failed. Exiting...");
        }
    }

    private static boolean authenticateUser(Scanner scanner, User user) {
        System.out.print("Enter user ID: ");
        String userId = scanner.next();
        System.out.print("Enter PIN: ");
        String pin = scanner.next();

        // Check if user ID and PIN match
        return user.authenticate(userId, pin);
    }

    private static void displayMenu() {
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Transfer");
        System.out.println("4. Transaction History");
        System.out.println("5. View Balance");
        System.out.println("6. Quit");
        System.out.print("Enter your choice: ");
    }
}

class User {
    private String userId;
    private String pin;
    private double accountBalance;
    private List<Transaction> transactionHistory;

    public User(String userId, String pin, double accountBalance) {
        this.userId = userId;
        this.pin = pin;
        this.accountBalance = accountBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public boolean authenticate(String userId, String pin) {
        return this.userId.equals(userId) && this.pin.equals(pin);
    }

    public void viewTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions available.");
        } else {
            System.out.println("Transaction History:");
            for (Transaction transaction : transactionHistory) {
                System.out.println(transaction);
            }
        }
    }

    public void viewBalance() {
        System.out.println("Current Balance: $" + accountBalance);
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= accountBalance) {
            accountBalance -= amount;
            addTransaction("Withdrawal", amount);
            System.out.println("Withdrawal successful. Remaining balance: " + accountBalance);
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds.");
        }
    }

    public void deposit(double amount) {
        if (amount > 0) {
            accountBalance += amount;
            addTransaction("Deposit", amount);
            System.out.println("Deposit successful. New balance: " + accountBalance);
        } else {
            System.out.println("Invalid deposit amount.");
        }
    }

    public void transfer(double amount, User targetUser) {
        if (amount > 0 && amount <= accountBalance) {
            accountBalance -= amount;
            targetUser.accountBalance += amount;
            addTransaction("Transfer to " + targetUser.userId, amount);
            System.out.println("Transfer successful. Remaining balance: " + accountBalance);
        } else {
            System.out.println("Invalid transfer amount or insufficient funds.");
        }
    }

    private void addTransaction(String type, double amount) {
        Transaction transaction = new Transaction(new Date(), type, amount);
        transactionHistory.add(transaction);
    }
}

class Transaction {
    private Date timestamp;
    private String type;
    private double amount;

    public Transaction(Date timestamp, String type, double amount) {
        this.timestamp = timestamp;
        this.type = type;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Timestamp: " + timestamp +
               ", Type: " + type +
               ", Amount: $" + amount;
    }
}

class TransactionHistory {
    // Placeholder class for transaction history
}

class ATMOperations {
    private User user;
    private TransactionHistory transactionHistory;

    public ATMOperations(User user, TransactionHistory transactionHistory) {
        this.user = user;
        this.transactionHistory = transactionHistory;
    }

    public void performOperation(int choice) {
        switch (choice) {
            case 4:
                user.viewTransactionHistory();
                break;
            case 5:
                user.viewBalance();
                break;
            case 2:
                System.out.print("Enter withdrawal amount: ");
                double withdrawalAmount = new Scanner(System.in).nextDouble();
                user.withdraw(withdrawalAmount);
                break;
            case 1:
                System.out.print("Enter deposit amount: ");
                double depositAmount = new Scanner(System.in).nextDouble();
                user.deposit(depositAmount);
                break;
            case 3:
                System.out.print("Enter transfer amount: ");
                double transferAmount = new Scanner(System.in).nextDouble();
                System.out.print("Enter target user ID: ");
                String targetUserId = new Scanner(System.in).next();
                // For simplicity, assuming targetUser is another instance of the User class
                User targetUser = new User("TargetUser", "0000", 0.0);
                user.transfer(transferAmount, targetUser);
                break;
            case 6:
                System.out.println("Exiting ATM system. Goodbye!");
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
        }
    }
}
