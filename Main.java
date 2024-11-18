import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    // Product class to represent items in the inventory
    static class Product {
        private String name;
        private double price;
        private int quantity;

        public Product(String name, double price, int quantity) {
            this.name = name;
            this.price = price;
            this.quantity = quantity;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void reduceQuantity(int quantity) {
            this.quantity -= quantity;
        }

        @Override
        public String toString() {
            return name + " - " + price + " (Stock: " + quantity + ")";
        }
    }

    // CartItem class to represent products added to the cart
    static class CartItem {
        private Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getTotalPrice() {
            return product.getPrice() * quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    // Cart class to manage items in the cart
    static class Cart {
        private List<CartItem> items;

        public Cart() {
            items = new ArrayList<>();
        }

        public void addItem(Product product, int quantity) {
            if (product.getQuantity() < quantity) {
                System.out.println("Not enough stock for " + product.getName());
            } else {
                CartItem cartItem = new CartItem(product, quantity);
                items.add(cartItem);
                product.reduceQuantity(quantity);
            }
        }

        public void displayCart() {
            double total = 0;
            System.out.println("\nItems in your cart:");
            for (CartItem item : items) {
                System.out.println(item.getProduct().getName() + " x " + item.getQuantity() + " = " + item.getTotalPrice());
                total += item.getTotalPrice();
            }
            System.out.println("Total: " + total);
        }

        public List<CartItem> getItems() {
            return items;
        }
    }

    // Transaction class to manage transactions and calculate total bill
    static class Transaction {
        private Cart cart;
        private double totalAmount;

        public Transaction(Cart cart) {
            this.cart = cart;
            calculateTotal();
        }

        private void calculateTotal() {
            totalAmount = 0;
            for (CartItem item : cart.getItems()) {
                totalAmount += item.getTotalPrice();
            }
        }

        public void displayBill() {
            cart.displayCart();
            System.out.println("Total Amount: " + totalAmount);
        }

        public boolean processPayment(double paymentAmount) {
            if (paymentAmount >= totalAmount) {
                System.out.println("Payment successful. Change: " + (paymentAmount - totalAmount));
                return true;
            } else {
                System.out.println("Insufficient funds.");
                return false;
            }
        }
    }

    // Main class to run the application
    public static void main(String[] args) {
        // Create some products in the inventory
        List<Product> inventory = new ArrayList<>();
        inventory.add(new Product("Tomato", 1.2, 100));
        inventory.add(new Product("Onion", 0.8, 50));
        inventory.add(new Product("Potato", 0.5, 200));

        Scanner scanner = new Scanner(System.in);
        Cart cart = new Cart();

        System.out.println("Welcome to the Vegetable and Grocery Market");

        boolean running = true;
        while (running) {
            System.out.println("\n1. View Products");
            System.out.println("2. Add to Cart");
            System.out.println("3. View Cart");
            System.out.println("4. Checkout");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    // View the list of products in the inventory
                    System.out.println("\nAvailable Products:");
                    for (Product product : inventory) {
                        System.out.println(product);
                    }
                    break;
                case 2:
                    // Add a product to the cart
                    System.out.print("Enter product name to add: ");
                    String productName = scanner.next();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();

                    // Search for the product by name
                    Product product = null;
                    for (Product p : inventory) {
                        if (p.getName().equalsIgnoreCase(productName)) {
                            product = p;
                            break;
                        }
                    }

                    if (product != null) {
                        cart.addItem(product, quantity);
                    } else {
                        System.out.println("Product not found.");
                    }
                    break;
                case 3:
                    // View the items in the cart
                    cart.displayCart();
                    break;
                case 4:
                    // Checkout and process payment
                    Transaction transaction = new Transaction(cart);
                    transaction.displayBill();
                    System.out.print("Enter payment amount: ");
                    double paymentAmount = scanner.nextDouble();
                    if (transaction.processPayment(paymentAmount)) {
                        // End the session after successful payment
                        running = false;
                    }
                    break;
                case 5:
                    // Exit the application
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        scanner.close();
        System.out.println("Thank you for shopping with us!");
    }
}