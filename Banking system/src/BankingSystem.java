import java.util.Scanner;

public class BankingSystem {
    private static Bank bank = new Bank();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        bank.loadFromFile();
        boolean running = true;

        System.out.println("--- Welcome to the NyashaMoney Bank .Please select your option!! ---");

        while (running) {
            System.out.println("\n1. Open Savings Account");
            System.out.println("2. Open Checking Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. Display All Accounts");
            System.out.println("7. Exit");
            System.out.println("8. View Transactions");
            System.out.println("9. Apply Interest (Savings)");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1 -> createAccount("Savings");
                case 2 -> createAccount("Checking");
                case 3 -> handleDeposit();
                case 4 -> handleWithdraw();
                case 5 -> handleTransfer();
                case 6 -> bank.displayAllAccounts();
                case 7 -> {
                    bank.saveToFile();
                    running = false;
                    System.out.println("Thank you for using our bank. Goodbye!");
                }
                case 8 -> handleViewStatement();
                case 9 -> handleApplyInterest();
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private static void createAccount(String type) {
        System.out.print("Enter Account Number: ");
        String num = scanner.nextLine();
        System.out.print("Enter Account Holder Name: ");
        String name = scanner.nextLine();
        double balance = getDoubleInput("Enter Initial Deposit: ");

        if (type.equals("Savings")) {
            double rate = getDoubleInput("Enter Interest Rate (%): ");
            bank.openAccount(new SavingsAccount(num, name, balance, rate));
        } else {
            double limit = getDoubleInput("Enter Overdraft Limit: ");
            bank.openAccount(new CheckingAccount(num, name, balance, limit));
        }
    }

    private static void handleDeposit() {
        System.out.print("Enter Account Number: ");
        String num = scanner.nextLine();
        double amount = getDoubleInput("Enter Amount: ");

        Account acc = bank.findAccount(num);
        if (acc != null) acc.deposit(amount);
        else System.out.println("Account not found.");
    }

    private static void handleWithdraw() {
        System.out.print("Enter Account Number: ");
        String num = scanner.nextLine();
        double amount = getDoubleInput("Enter Amount: ");

        Account acc = bank.findAccount(num);
        if (acc != null) acc.withdraw(amount);
        else System.out.println("Account not found.");
    }

    private static void handleTransfer() {
        System.out.print("From Account Number: ");
        String from = scanner.nextLine();
        System.out.print("To Account Number: ");
        String to = scanner.nextLine();
        double amount = getDoubleInput("Amount: ");

        bank.transfer(from, to, amount);
    }

    private static void handleViewStatement() {
        System.out.print("Enter Account Number: ");
        String num = scanner.nextLine();

        Account acc = bank.findAccount(num);
        if (acc != null) {
            acc.printStatement();
        } else {
            System.out.println("Account not found.");
        }
    }

    private static void handleApplyInterest() {
        System.out.print("Enter Savings Account Number: ");
        String num = scanner.nextLine();
        
        Account acc = bank.findAccount(num);
        if (acc instanceof SavingsAccount) {
            ((SavingsAccount) acc).applyInterest();
        } else if (acc != null) {
            System.out.println("This is not a Savings Account.");
        } else {
            System.out.println("Account not found.");
        }
    }
}