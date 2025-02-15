import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class User {
    String name;
    float amt;
    String upi;

    User() {
        this.name = "";
        this.amt = 0.0f;
        this.upi = "";
    }

    User(String name, float amt, String upi) {
        this.name = name;
        this.amt = amt;
        this.upi = upi;
    }

    public void Getter1() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter user name: ");
        this.name = sc.nextLine();
        System.out.print("Enter user's UPI address: ");
        this.upi = sc.nextLine();
    }
}

class Expense extends User {
    String description;
    float amount;
    List<User> paidByUsers;
    List<Float> paidAmounts;
    boolean splitEqually;
    List<User> splitAmongUsers;
    List<Float> splitAmounts;

    Expense() {
        this.description = "";
        this.amount = 0.0f;
        this.paidByUsers = new ArrayList<>();
        this.paidAmounts = new ArrayList<>();
        this.splitEqually = true;
        this.splitAmongUsers = new ArrayList<>();
        this.splitAmounts = new ArrayList<>();
    }
}

public class SplitSmartApp {
    static List<Expense> expenses = new ArrayList<>();
    static String sharingTitle = "";

    static void startNewSharing() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Title : ");
        sharingTitle = sc.nextLine();

        System.out.print("Enter the number of users: ");
        int n = sc.nextInt();
        sc.nextLine(); 

        User[] users = new User[n];
        System.out.println("Users:");
        for (int i = 0; i < n; i++) {
            users[i] = new User();
            System.out.println("Enter details for user " + (i + 1) + ":");
            users[i].Getter1();
            System.out.println();
        }

        while (true) {
            System.out.print("1. Add new Expense.\n2. Give final output.\nEnter input: ");
            int w = sc.nextInt();
            sc.nextLine(); 
            System.out.println();

            if (w == 1) {
                // Adding new expense
                Expense expense = new Expense();

                System.out.print("Description: ");
                expense.description = sc.nextLine();

                System.out.print("Enter amount: ");
                expense.amount = sc.nextFloat();
                sc.nextLine();

                
                System.out.print("Paid by Single/Multiple (S/M): ");
                char c = sc.nextLine().toUpperCase().charAt(0);

                if (c=='M') {
                    System.out.print("Paid by how many users: ");
                    int n1 = sc.nextInt();
                    sc.nextLine();
                    for (int i = 0; i < n; i++) {
                        System.out.print((i + 1) + ". " + users[i].name +"\n");
                    }
                    System.out.println("Enter the indices of the users who paid the amount:");
                    
                    int[] indexArr = new int[n1];
                    for (int i = 0; i < n1; i++) {
                        indexArr[i] = sc.nextInt() - 1;
                    }
                    sc.nextLine();

                    for (int i = 0; i < n1; i++) {
                        System.out.print("Enter the amount paid by " + users[indexArr[i]].name + ": ");
                        float amt1 = sc.nextFloat();
                        sc.nextLine();
                        expense.paidByUsers.add(users[indexArr[i]]);
                        expense.paidAmounts.add(amt1);
                    }
                } 
                else {
                    System.out.println("Select the user who paid the amount:");
                    for (int i = 0; i < n; i++) {
                        System.out.println((i + 1) + ". " + users[i].name + "");
                    }
                    System.out.print("Enter the index of the user who paid the amount: ");
                    int j = sc.nextInt() - 1;
                    sc.nextLine();
                    expense.paidByUsers.add(users[j]);
                    expense.paidAmounts.add(expense.amount);
                }

                System.out.print("Split equally (Y/N): ");
                char c2 = sc.nextLine().toUpperCase().charAt(0);
                if (c2 == 'N') {
                    expense.splitEqually = false;
                } else {
                    expense.splitEqually = true;
                }

                if (!expense.splitEqually) {
                    System.out.println("Enter amount for each user:");
                    for (int i = 0; i < n; i++) {
                        System.out.print((i + 1) + ". " + users[i].name +  " : ");
                        float amt2 = sc.nextFloat();
                        sc.nextLine();
                        expense.splitAmongUsers.add(users[i]);
                        expense.splitAmounts.add(amt2);
                    }
                } else {
                    System.out.print("Split amongst how many users: ");
                    int n2 = sc.nextInt();
                    sc.nextLine();

                    if (n2 == n) {
                        float splitAmt = expense.amount / n;
                        for (int i = 0; i < n; i++) {
                            expense.splitAmongUsers.add(users[i]);
                            expense.splitAmounts.add(splitAmt);
                        }
                    } else {
                        System.out.println("Enter the indices of the users amongst whom the amount must be split:");
                        for (int i = 0; i < n; i++) {
                            System.out.println((i + 1) + ". " + users[i].name + "");
                        }
                        int[] indexArr = new int[n2];
                        for (int i = 0; i < n2; i++) {
                            indexArr[i] = sc.nextInt() - 1;
                        }
                        sc.nextLine();

                        float splitAmt = expense.amount / n2;
                        for (int i = 0; i < n2; i++) {
                            expense.splitAmongUsers.add(users[indexArr[i]]);
                            expense.splitAmounts.add(splitAmt);
                        }
                    }
                }

                expenses.add(expense);
                System.out.println("Expense added successfully!\n");

            } else if (w == 2) {
                // Generate final output
                generateFinalOutput(users);
                break;
            } else {
                System.out.println("Error in entering input!\n");
            }
        }
    }

    static void generateFinalOutput(User[] users) {
        String fileName = sharingTitle.replaceAll("\\s+", "") + ".txt";
        try (FileWriter fileWriter = new FileWriter(fileName)) { 

            
            fileWriter.write(sharingTitle + "\n\n");

            
            fileWriter.write("Users:\n");
            for (User user : users) {
                fileWriter.write("• " + user.name + " (" + user.upi + ")\n");
            }

            fileWriter.write("\nExpense History:\n");

            
            int expenseCount = 1;
            for (Expense expense : expenses) {
                fileWriter.write(expenseCount + ". Description: " + expense.description + "\n");
                fileWriter.write("   Amount: " + expense.amount + "\n");
                
                // Paid by
                fileWriter.write("   Paid by: ");
                if (expense.paidByUsers.size() == 1) {
                    User payer = expense.paidByUsers.get(0);
                    fileWriter.write(payer.name + "\n");
                } else {
                    for (int i = 0; i < expense.paidByUsers.size(); i++) {
                        User payer = expense.paidByUsers.get(i);
                        fileWriter.write(payer.name + "("+ expense.paidAmounts.get(i) + ")");
                        if (i != expense.paidByUsers.size() - 1) {
                            fileWriter.write(", ");
                        } else {
                            fileWriter.write("\n");
                        }
                    }
                }

                // Split details
                fileWriter.write("   Split:\n");
                for (int i = 0; i < expense.splitAmongUsers.size(); i++) {
                    User splitUser = expense.splitAmongUsers.get(i);
                    float splitAmt = expense.splitAmounts.get(i);
                    fileWriter.write("      - " + splitUser.name + "-- " + splitAmt + "/-\n");
                }

                fileWriter.write("---------------------------------\n");
                expenseCount++;
            }

            fileWriter.write("\nSettlement Summary:\n");
            // Calculate net amounts for each user
            float[] netAmounts = new float[users.length];
            for (Expense expense : expenses) {
                // Add amounts paid
                for (int i = 0; i < expense.paidByUsers.size(); i++) {
                    User payer = expense.paidByUsers.get(i);
                    float amtPaid = expense.paidAmounts.get(i);
                    for (int j = 0; j < users.length; j++) {
                        if (users[j].name.equals(payer.name)) {
                            netAmounts[j] += amtPaid;
                            break;
                        }
                    }
                }
                // Subtract amounts owed
                for (int i = 0; i < expense.splitAmongUsers.size(); i++) {
                    User splitUser = expense.splitAmongUsers.get(i);
                    float amtSplit = expense.splitAmounts.get(i);
                    for (int j = 0; j < users.length; j++) {
                        if (users[j].name.equals(splitUser.name)) {
                            netAmounts[j] -= amtSplit;
                            break;
                        }
                    }
                }
            }

            List<UserBalance> creditors = new ArrayList<>();
            List<UserBalance> debtors = new ArrayList<>();

            for (int i = 0; i < users.length; i++) {
                if (netAmounts[i] > 0) {
                    creditors.add(new UserBalance(users[i], netAmounts[i]));
                } else if (netAmounts[i] < 0) {
                    debtors.add(new UserBalance(users[i], -netAmounts[i]));
                }
            }

            int y = 0, z = 0;
            while (y < debtors.size() && z < creditors.size()) {
                UserBalance debtor = debtors.get(y);
                UserBalance creditor = creditors.get(z);

                float minAmount = Math.min(debtor.amount, creditor.amount);
                fileWriter.write(debtor.user.name + " owes " +
                                 creditor.user.name + " -- " + minAmount + "/-\n");

                debtors.get(y).amount -= minAmount;
                creditors.get(z).amount -= minAmount;

                if (debtors.get(y).amount == 0) {
                    y++;
                }
                if (creditors.get(z).amount == 0) {
                    z++;
                }
            }

            System.out.println("Final output has been written to " + fileName + "\n");

        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    // Class to store user and their net balance
    static class UserBalance {
        User user;
        float amount;

        UserBalance(User user, float amount) {
            this.user = user;
            this.amount = amount;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("------------------------------------------------");
            System.out.println("Main menu:");
            System.out.print("1. Start New Sharing.\n2. End.\nEnter input: ");
            int q = sc.nextInt();
            sc.nextLine();
            if (q == 2) break;
            else if (q == 1) {
                System.out.println("------------------------------------------------");
                startNewSharing();
                System.out.println();
            } else {
                System.out.println("Error in entering input!\n");
            }
        }
    }
}