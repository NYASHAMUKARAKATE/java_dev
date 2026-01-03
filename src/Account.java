public abstract class Account {
    private String accountNumber;
    private String accountHolder;
    protected double balance; // this is protected so that the subclasses can access it easily

    public Account(String accountNumber, String accountHolder, double balance){
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = balance;

    }
    public void deposit(double amount){
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposited: $" + amount);
        }
    }
    @Override
    public String toString() {
        return String.format("Type: %s | ID: %s | Holder: %s | Balance: $%.2f",
                this.getClass().getSimpleName(), accountNumber, accountHolder, balance);
    }

    public abstract boolean withdraw(double amount);

    //Getters
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolder() { return accountHolder; }
    public double getBalance() { return balance; }
}

