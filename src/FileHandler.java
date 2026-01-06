import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String FILE_NAME = "bank_data.txt";

    public static void saveAccounts(Map<String, Account> accounts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Account acc : accounts.values()) {
                String type = acc instanceof SavingsAccount ? "SAVINGS" : "CHECKING";
                double specialValue = (acc instanceof SavingsAccount)
                        ? 0.0 : ((CheckingAccount)acc).getOverdraftLimit();

                writer.println(type + "," + acc.getAccountNumber() + "," +
                        acc.getAccountHolder() + "," + acc.getBalance() + "," + specialValue);
            }
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

}
