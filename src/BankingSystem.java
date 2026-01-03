import java.util.Scanner;

public class BankingSystem {
    private static Bank bank = new Bank();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
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


            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createAccount("Savings");
                case 2 -> createAccount("Checking");
                case 3 -> handleDeposit();
                case 4 -> handleWithdraw();
                case 5 -> handleTransfer();
                case 6 -> bank.displayAllAccounts();
                case 7 -> {
                    running = false;
                    System.out.println("Thank you for using our bank. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void createAccount(String type) {
        System.out.print("Enter Account Number: ");
        String num = scanner.nextLine();
        System.out.print("Enter Account Holder Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Initial Deposit: ");
        double balance = scanner.nextDouble();

        if (type.equals("Savings")) {
            System.out.print("Enter Interest Rate (%): ");
            double rate = scanner.nextDouble();
            bank.openAccount(new SavingsAccount(num, name, balance, rate));
        } else {
            System.out.print("Enter Overdraft Limit: ");
            double limit = scanner.nextDouble();
            bank.openAccount(new CheckingAccount(num, name, balance, limit));
        }
    }

    private static void handleDeposit() {
        System.out.print("Enter Account Number: ");
        String num = scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();

        Account acc = bank.findAccount(num);
        if (acc != null) acc.deposit(amount);
        else System.out.println("Account not found.");
    }

    private static void handleWithdraw() {
        System.out.print("Enter Account Number: ");
        String num = scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();

        Account acc = bank.findAccount(num);
        if (acc != null) acc.withdraw(amount);
        else System.out.println("Account not found.");
    }

    private static void handleTransfer() {
        System.out.print("From Account Number: ");
        String from = scanner.nextLine();
        System.out.print("To Account Number: ");
        String to = scanner.nextLine();
        System.out.print("Amount: ");
        double amount = scanner.nextDouble();

        bank.transfer(from, to, amount);
    }
}