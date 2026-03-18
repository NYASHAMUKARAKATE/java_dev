public class CheckingAccount extends Account {
    private double overdraftLimit;

    public CheckingAccount(String accountNumber, String accountHolder, double initialBalance, double overdraftLimit) {

        super(accountNumber, accountHolder, initialBalance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        // Check if the withdrawal keeps us within the overdraft limit
        if (amount <= balance + overdraftLimit) {
            balance -= amount;
            recordTransaction("Withdrawal", amount);
            System.out.println("Withdrawn from Checking: $" + amount);
            System.out.println("Current Balance: $" + balance);
            return true;
        } else {
            System.out.println("Transaction failed: Exceeds overdraft limit of $" + overdraftLimit);
            return false;
        }
    }


    public double getOverdraftLimit() {
        return overdraftLimit;
    }
}