import java.util.HashMap;
import java.util.Map;

public class Bank {
    // storing accounts in a Map: Key = Account Number, Value = Account Object
    private Map<String, Account> accounts;

    public Bank() {
        this.accounts = new HashMap<>();
    }

    //an account to the bank
    public void openAccount(Account account) {
        accounts.put(account.getAccountNumber(), account);
        System.out.println("Account opened successfully for: " + account.getAccountHolder());
    }


    public Account findAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }


    public boolean transfer(String fromNum, String toNum, double amount) {
        Account fromAccount = findAccount(fromNum);
        Account toAccount = findAccount(toNum);

        if (fromAccount == null || toAccount == null) {
            System.out.println("Error: One or both accounts not found.");
            return false;
        }

        if (fromAccount.withdraw(amount)) {
            toAccount.deposit(amount);
            System.out.println("Transfer successful!");
            return true;
        } else {
            System.out.println("Transfer failed: Insufficient funds.");
            return false;
        }
    }

    public void displayAllAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts in the system.");
        } else {
            for (Account acc : accounts.values()) {
                System.out.println(acc);
            }
        }
    }
}