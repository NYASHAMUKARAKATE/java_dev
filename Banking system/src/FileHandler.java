import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String FILE_NAME = "bank_data.txt";

    public static void saveAccounts(Map<String, Account> accounts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Account acc : accounts.values()) {
                String type = acc instanceof SavingsAccount ? "SAVINGS" : "CHECKING";
                double specialValue = (acc instanceof SavingsAccount)
                        ? ((SavingsAccount)acc).getInterestRate() : ((CheckingAccount)acc).getOverdraftLimit();

                writer.println(type + "," + acc.getAccountNumber() + "," +
                        acc.getAccountHolder() + "," + acc.getBalance() + "," + specialValue);
            }
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    public static Map<String, Account> loadAccounts() {
        Map<String, Account> accounts = new HashMap<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return accounts;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String type = parts[0];
                    String accNum = parts[1];
                    String holder = parts[2];
                    double balance = Double.parseDouble(parts[3]);
                    double specialVal = Double.parseDouble(parts[4]);

                    if (type.equals("SAVINGS")) {
                        accounts.put(accNum, new SavingsAccount(accNum, holder, balance, specialVal));
                    } else if (type.equals("CHECKING")) {
                        accounts.put(accNum, new CheckingAccount(accNum, holder, balance, specialVal));
                    }
                }
            }
            System.out.println("Data loaded successfully!");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
        return accounts;
    }
}
