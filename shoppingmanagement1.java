import java.util.ArrayList;
import java.util.Scanner;

// Entity classes
class Shop {
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

class Customer {
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

class Purchase {
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
        System.out.println(customerName + " purchased " + item + " from " + shopName + " for $" + amount);
    }
}

// Management system class
public class ManagementSystem {
    static ArrayList<Shop> shops = new ArrayList<>();
    static ArrayList<Customer> customers = new ArrayList<>();
    static ArrayList<Purchase> purchases = new ArrayList<>();

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
                    System.out.println("Exiting System...");
                    break;
                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 0);

        sc.close();
    }

    static void addShop(Scanner sc) {
        System.out.print("Enter shop name: ");
        String name = sc.nextLine();
        System.out.print("Enter shop category: ");
        String category = sc.nextLine();
        shops.add(new Shop(name, category));
        System.out.println("Shop added successfully.");
    }

    static void viewShops() {
        System.out.println("\n--- List of Shops ---");
        for (Shop s : shops) {
            s.displayShop();
        }
    }

    static void addCustomer(Scanner sc) {
        System.out.print("Enter customer name: ");
        String name = sc.nextLine();
        System.out.print("Enter contact number: ");
        String contact = sc.nextLine();
        customers.add(new Customer(name, contact));
        System.out.println("Customer added successfully.");
    }

    static void viewCustomers() {
        System.out.println("\n--- List of Customers ---");
        for (Customer c : customers) {
            c.displayCustomer();
        }
    }

    static void makePurchase(Scanner sc) {
        System.out.print("Enter customer name: ");
        String customerName = sc.nextLine();
        System.out.print("Enter shop name: ");
        String shopName = sc.nextLine();
        System.out.print("Enter item: ");
        String item = sc.nextLine();
        System.out.print("Enter amount: ");
        double amount = sc.nextDouble();
        purchases.add(new Purchase(customerName, shopName, item, amount));
        System.out.println("Purchase recorded successfully.");
    }

    static void viewPurchases() {
        System.out.println("\n--- List of Purchases ---");
        for (Purchase p : purchases) {
            p.displayPurchase();
        }
    }
}
