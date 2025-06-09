import java.io.*;
import java.util.*;

// Entity Classes
class Shop implements Serializable {
    String shopName;
    String shopCategory;

    public Shop(String shopName, String shopCategory) {
        this.shopName = shopName;
        this.shopCategory = shopCategory;
    }

    public void displayShop() {
        System.out.println("Shop Name: " + shopName + ", Category: " + shopCategory);
    }
}

class Customer implements Serializable {
    String name;
    String contact;

    public Customer(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public void displayCustomer() {
        System.out.println("Customer Name: " + name + ", Contact: " + contact);
    }
}

class Purchase implements Serializable {
    String customerName;
    String shopName;
    String item;
    double amount;

    public Purchase(String customerName, String shopName, String item, double amount) {
        this.customerName = customerName;
        this.shopName = shopName;
        this.item = item;
        this.amount = amount;
    }

    public void displayPurchase() {
        System.out.println(customerName + " purchased " + item + " from " + shopName + " for Rs. " + amount);
    }
}

public class ShoppingMallManagement {
    static final String SHOP_FILE = "shops.dat";
    static final String CUSTOMER_FILE = "customers.dat";
    static final String PURCHASE_FILE = "purchases.dat";

    static ArrayList<Shop> shops = loadData(SHOP_FILE);
    static ArrayList<Customer> customers = loadData(CUSTOMER_FILE);
    static ArrayList<Purchase> purchases = loadData(PURCHASE_FILE);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n--- Shopping Mall Management System ---");
            System.out.println("1. Add Shop");
            System.out.println("2. View Shops");
            System.out.println("3. Add Customer");
            System.out.println("4. View Customers");
            System.out.println("5. Make Purchase");
            System.out.println("6. View Purchases");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");
            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                sc.next();
            }
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    addShop(sc);
                    break;
                case 2:
                    viewShops();
                    break;
                case 3:
                    addCustomer(sc);
                    break;
                case 4:
                    viewCustomers();
                    break;
                case 5:
                    makePurchase(sc);
                    break;
                case 6:
                    viewPurchases();
                    break;
                case 0:
                    System.out.println("Exiting system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid Choice! Please try again.");
            }
        } while (choice != 0);
        sc.close();
    }

    static void addShop(Scanner sc) {
        System.out.print("Enter shop name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Shop name cannot be empty.");
            return;
        }
        System.out.print("Enter shop category: ");
        String category = sc.nextLine().trim();
        if (category.isEmpty()) {
            System.out.println("Shop category cannot be empty.");
            return;
        }
        shops.add(new Shop(name, category));
        saveData(SHOP_FILE, shops);
        System.out.println("Shop added successfully.");
    }

    static void viewShops() {
        System.out.println("\n--- List of Shops ---");
        if (shops.isEmpty()) {
            System.out.println("No shops available.");
            return;
        }
        for (Shop s : shops) {
            s.displayShop();
        }
    }

    static void addCustomer(Scanner sc) {
        System.out.print("Enter customer name: ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Customer name cannot be empty.");
            return;
        }
        System.out.print("Enter contact number: ");
        String contact = sc.nextLine().trim();
        if (!contact.matches("\\d{10}")) {
            System.out.println("Invalid contact number. Must be 10 digits.");
            return;
        }
        customers.add(new Customer(name, contact));
        saveData(CUSTOMER_FILE, customers);
        System.out.println("Customer added successfully.");
    }

    static void viewCustomers() {
        System.out.println("\n--- List of Customers ---");
        if (customers.isEmpty()) {
            System.out.println("No customers available.");
            return;
        }
        for (Customer c : customers) {
            c.displayCustomer();
        }
    }

    static void makePurchase(Scanner sc) {
        System.out.print("Enter customer name: ");
        String customerName = sc.nextLine().trim();
        System.out.print("Enter shop name: ");
        String shopName = sc.nextLine().trim();
        System.out.print("Enter item: ");
        String item = sc.nextLine().trim();
        System.out.print("Enter amount: ");
        while (!sc.hasNextDouble()) {
            System.out.print("Invalid amount. Enter numeric value: ");
            sc.next();
        }
        double amount = sc.nextDouble();
        sc.nextLine();

        if (customerName.isEmpty() || shopName.isEmpty() || item.isEmpty() || amount <= 0) {
            System.out.println("Invalid purchase details.");
            return;
        }

        purchases.add(new Purchase(customerName, shopName, item, amount));
        saveData(PURCHASE_FILE, purchases);
        System.out.println("Purchase recorded successfully.");
    }

    static void viewPurchases() {
        System.out.println("\n--- List of Purchases ---");
        if (purchases.isEmpty()) {
            System.out.println("No purchases recorded.");
            return;
        }
        for (Purchase p : purchases) {
            p.displayPurchase();
        }
    }

    // Generic Load and Save Methods
    @SuppressWarnings("unchecked")
    static <T> ArrayList<T> loadData(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
                return new ArrayList<>();
            } catch (IOException e) {
                System.out.println("Failed to create file: " + filename);
                return new ArrayList<>();
            }
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (ArrayList<T>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    static <T> void saveData(String filename, ArrayList<T> list) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(list);
        } catch (IOException e) {
            System.out.println("Failed to save data to " + filename);
        }
    }
}
