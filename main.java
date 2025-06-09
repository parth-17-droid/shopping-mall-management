import java.sql.*;
import java.util.Scanner;

public class ShoppingMallManagement {
    // Database connection parameters
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mall_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password"; // Change to your MySQL password
    
    private static Connection connection;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Establish database connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            int choice;
            do {
                System.out.println("\n--- Shopping Mall Management System ---");
                System.out.println("1. Add Shop");
                System.out.println("2. View Shops");
                System.out.println("3. Add Customer");
                System.out.println("4. View Customers");
                System.out.println("5. Make Purchase");
                System.out.println("6. View Purchases");
                System.out.println("7. View Purchases by Customer");
                System.out.println("8. View Purchases by Shop");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");
                choice = getIntInput();
                
                switch (choice) {
                    case 1: addShop(); break;
                    case 2: viewShops(); break;
                    case 3: addCustomer(); break;
                    case 4: viewCustomers(); break;
                    case 5: makePurchase(); break;
                    case 6: viewPurchases(); break;
                    case 7: viewPurchasesByCustomer(); break;
                    case 8: viewPurchasesByShop(); break;
                    case 0: System.out.println("Exiting system. Goodbye!"); break;
                    default: System.out.println("Invalid Choice! Please try again.");
                }
            } while (choice != 0);
            
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
        } finally {
            try {
                if (connection != null) connection.close();
                scanner.close();
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    private static void addShop() {
        System.out.print("Enter shop name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Shop name cannot be empty.");
            return;
        }
        
        System.out.print("Enter shop category: ");
        String category = scanner.nextLine().trim();
        if (category.isEmpty()) {
            System.out.println("Shop category cannot be empty.");
            return;
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO shops (shop_name, shop_category) VALUES (?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, category);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Shop added successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding shop: " + e.getMessage());
        }
    }

    private static void viewShops() {
        System.out.println("\n--- List of Shops ---");
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM shops ORDER BY shop_name")) {
            
            if (!rs.isBeforeFirst()) {
                System.out.println("No shops available.");
                return;
            }
            
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Category: %s%n",
                        rs.getInt("id"),
                        rs.getString("shop_name"),
                        rs.getString("shop_category"));
            }
        } catch (SQLException e) {
            System.err.println("Error viewing shops: " + e.getMessage());
        }
    }

    private static void addCustomer() {
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Customer name cannot be empty.");
            return;
        }
        
        System.out.print("Enter contact number: ");
        String contact = scanner.nextLine().trim();
        if (!contact.matches("\\d{10}")) {
            System.out.println("Invalid contact number. Must be 10 digits.");
            return;
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO customers (name, contact) VALUES (?, ?)")) {
            stmt.setString(1, name);
            stmt.setString(2, contact);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Customer added successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding customer: " + e.getMessage());
        }
    }

    private static void viewCustomers() {
        System.out.println("\n--- List of Customers ---");
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM customers ORDER BY name")) {
            
            if (!rs.isBeforeFirst()) {
                System.out.println("No customers available.");
                return;
            }
            
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Contact: %s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("contact"));
            }
        } catch (SQLException e) {
            System.err.println("Error viewing customers: " + e.getMessage());
        }
    }

    private static void makePurchase() {
        viewCustomers();
        System.out.print("\nEnter customer ID: ");
        int customerId = getIntInput();
        
        viewShops();
        System.out.print("\nEnter shop ID: ");
        int shopId = getIntInput();
        
        System.out.print("Enter item name: ");
        String item = scanner.nextLine().trim();
        if (item.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return;
        }
        
        System.out.print("Enter amount: ");
        double amount = getDoubleInput();
        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return;
        }
        
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO purchases (customer_id, shop_id, item, amount) VALUES (?, ?, ?, ?)")) {
            stmt.setInt(1, customerId);
            stmt.setInt(2, shopId);
            stmt.setString(3, item);
            stmt.setDouble(4, amount);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Purchase recorded successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Error recording purchase: " + e.getMessage());
        }
    }

    private static void viewPurchases() {
        System.out.println("\n--- All Purchases ---");
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT p.id, c.name AS customer, s.shop_name AS shop, p.item, p.amount, p.purchase_date " +
                     "FROM purchases p " +
                     "JOIN customers c ON p.customer_id = c.id " +
                     "JOIN shops s ON p.shop_id = s.id " +
                     "ORDER BY p.purchase_date DESC")) {
            
            if (!rs.isBeforeFirst()) {
                System.out.println("No purchases recorded.");
                return;
            }
            
            while (rs.next()) {
                System.out.printf("ID: %d, Customer: %s, Shop: %s, Item: %s, Amount: %.2f, Date: %s%n",
                        rs.getInt("id"),
                        rs.getString("customer"),
                        rs.getString("shop"),
                        rs.getString("item"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("purchase_date"));
            }
        } catch (SQLException e) {
            System.err.println("Error viewing purchases: " + e.getMessage());
        }
    }

    private static void viewPurchasesByCustomer() {
        viewCustomers();
        System.out.print("\nEnter customer ID: ");
        int customerId = getIntInput();
        
        System.out.println("\n--- Purchases by Customer ---");
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT p.id, s.shop_name AS shop, p.item, p.amount, p.purchase_date " +
                "FROM purchases p " +
                "JOIN shops s ON p.shop_id = s.id " +
                "WHERE p.customer_id = ? " +
                "ORDER BY p.purchase_date DESC")) {
            
            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();
            
            if (!rs.isBeforeFirst()) {
                System.out.println("No purchases found for this customer.");
                return;
            }
            
            while (rs.next()) {
                System.out.printf("ID: %d, Shop: %s, Item: %s, Amount: %.2f, Date: %s%n",
                        rs.getInt("id"),
                        rs.getString("shop"),
                        rs.getString("item"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("purchase_date"));
            }
        } catch (SQLException e) {
            System.err.println("Error viewing customer purchases: " + e.getMessage());
        }
    }

    private static void viewPurchasesByShop() {
        viewShops();
        System.out.print("\nEnter shop ID: ");
        int shopId = getIntInput();
        
        System.out.println("\n--- Purchases by Shop ---");
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT p.id, c.name AS customer, p.item, p.amount, p.purchase_date " +
                "FROM purchases p " +
                "JOIN customers c ON p.customer_id = c.id " +
                "WHERE p.shop_id = ? " +
                "ORDER BY p.purchase_date DESC")) {
            
            stmt.setInt(1, shopId);
            ResultSet rs = stmt.executeQuery();
            
            if (!rs.isBeforeFirst()) {
                System.out.println("No purchases found for this shop.");
                return;
            }
            
            while (rs.next()) {
                System.out.printf("ID: %d, Customer: %s, Item: %s, Amount: %.2f, Date: %s%n",
                        rs.getInt("id"),
                        rs.getString("customer"),
                        rs.getString("item"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("purchase_date"));
            }
        } catch (SQLException e) {
            System.err.println("Error viewing shop purchases: " + e.getMessage());
        }
    }

    // Helper methods for input validation
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return input;
    }

    private static double getDoubleInput() {
        while (!scanner.hasNextDouble()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        double input = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        return input;
    }
}
