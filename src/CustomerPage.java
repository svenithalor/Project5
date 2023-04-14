import javax.xml.crypto.KeySelector;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

/*********
 * CustomerPage
 * @author Sveni Thalor
 * @version 4/7/2026
 */
public class CustomerPage {

    private ArrayList<Bike> bikes;
    private ArrayList<PurchasedBike> shoppingCart;
    private ArrayList<PurchasedBike> purchasedBikes;
    private Buyer buyer;

    //Constructs the customer page and initializes an arraylist of bike
    public CustomerPage(ArrayList<Bike> bikes, Buyer buyer) {
        this.bikes = UserInfo.getBikes();
        // System.out.println("Bikes in customer page: " + bikes);
        this.buyer = buyer;
        this.shoppingCart = buyer.getShoppingCart();
        this.purchasedBikes = buyer.getPurchaseHistory();
    }

    /*****
     * This method outlines the main customer user experience. It navigates through a series of options and the user
     * can keep selecting options until they decide to exit. At the end of this method, the buyer object is updated with
     * changes and the method returns the updated arraylist of bikes.
     *
     * @return Updated arraylist of available bikes
     */
    public ArrayList<Bike> open(Buyer buyer) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Boilermaker Bikes!");
        boolean repeat = true;
        do {
            System.out.println("Select an option: ");
            System.out.println("1. View all available bikes");
            System.out.println("2. Sort bikes by quantity available");
            System.out.println("3. Sort bikes by price");
            System.out.println("4. Add a bike to cart");
            System.out.println("5. Review cart");
            System.out.println("6. Edit cart");
            System.out.println("7. Search products");
            System.out.println("8. Checkout");
            System.out.println("9. Get purchase history");
            System.out.println("10. Logout");
            System.out.println("11. Delete account");
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 1: viewAvailableBikes();
                    break;
                case 2: sortByQuantity();
                    break;
                case 3: sortByPrice();
                    break;
                case 4:
                    System.out.println("Enter the ID number of the bike you would like to add.");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Enter quantity you would like to add");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Would you like to add insurance? Enter 1 for yes and 2 for no");
                    int insurance = scanner.nextInt();
                    scanner.nextLine();
                    addToCart(id, quantity, insurance);
                    break;
                case 5: viewCart();
                    break;
                case 6:
                    System.out.println("Enter ID of bike you would like to remove");
                    int idRemove = scanner.nextInt();
                    scanner.nextLine();
                    removeBike(idRemove);
                    break;
                case 7:
                    System.out.println("Enter your search");
                    String query = scanner.nextLine();
                    search(query);
                    break;
                case 8: checkout();
                    break;
                case 9:
                    System.out.println("Enter name of file to export data to");
                    String fileName = scanner.nextLine();
                    getPurchaseHistory(fileName);
                    break;
                case 10: repeat = false;
                    break;
                case 11:
                    System.out.println("Enter username to confirm account deletion or enter 1 to cancel");
                    String confirm = scanner.nextLine();
                    if (!confirm.equals("1")) {
                        deleteAccount(confirm);
                        repeat = false;
                    }
                    break;
                default:
                    System.out.println("Invalid input!");
                    break;
            }
        } while (repeat);
        buyer.setShoppingCart(shoppingCart);
        buyer.setPurchaseHistory(purchasedBikes);
        int index = UserInfo.getBuyers().indexOf(buyer);
        UserInfo.getBuyers().set(index, buyer);
        return bikes;
    }

    /*****
     * This method searches each bike's description and name for a match based on the given search term. It stores the
     * matches in an arraylist and then prints them out.
     *
     * @param searchTerm : user-provided search term to look for
     */

    public void search(String searchTerm) {
        ArrayList<Bike> matches = new ArrayList<Bike>();
        String term = searchTerm.toLowerCase();
        for (Bike bike : bikes) {
            if (bike.getModelName().contains(term)) {
                matches.add(bike);
            } else if (bike.getSellerName().contains(term)) {
                matches.add(bike);
            } else if (bike.getDescription().contains(term)) {
                matches.add(bike);
            }
        }
        System.out.println("Matches:");
        for (Bike bike : matches) {
            System.out.println(bike.toString());
        }
    }

    /*****
     * This method sorts the arraylist of available bikes from lowest to highest price. It prints out the sorted array.
     *
     */
    public void sortByPrice() {
        System.out.println("Hello world");
        ArrayList<Bike> sorted = new ArrayList(bikes.size());
        for (int i = 0; i < bikes.size(); i++) {
            sorted.add(bikes.get(i));
        }
        for (int i = 0; i < bikes.size() - 1; i++) {
            Bike thisBike = sorted.get(i);
            Bike lowestBike = thisBike;
            int lowestIndex = i;
            double lowestPrice = lowestBike.getPrice();
            for (int j = i + 1; j < bikes.size(); j++) {
                Bike otherBike = sorted.get(j);
                double price = otherBike.getPrice();
                if (price < lowestPrice) {
                    lowestBike = otherBike;
                    lowestPrice = price;
                    lowestIndex = j;
                }
            }
            sorted.set(i, lowestBike);
            sorted.set(lowestIndex, thisBike);
        }

        for (Bike bike : sorted) {
            System.out.println(bike.toString());
        }
    }

    /*****
     * This method sorts the arraylist of available bikes from lowest to highest available quantity. It prints out the
     * sorted array.
     */
    public void sortByQuantity() {
        ArrayList<Bike> sorted = new ArrayList(bikes.size());
        for (int i = 0; i < bikes.size(); i++) {
            sorted.add(bikes.get(i));
        }
        for (int i = 0; i < bikes.size() - 1; i++) {
            Bike thisBike = sorted.get(i);
            Bike lowestBike = thisBike;
            int lowestIndex = i;
            double lowestQuantity = lowestBike.getQuantity();
            for (int j = i + 1; j < bikes.size(); j++) {
                Bike otherBike = sorted.get(j);
                double quantity = otherBike.getQuantity();
                if (quantity < lowestQuantity) {
                    lowestBike = otherBike;
                    lowestQuantity = quantity;
                    lowestIndex = j;
                }
            }
            sorted.set(i, lowestBike);
            sorted.set(lowestIndex, thisBike);
        }

        for (Bike bike : sorted) {
            System.out.println(bike.toString());
        }
    }

    /*****
     * This method prints out all the available bikes.
     */
    public void viewAvailableBikes() {
        System.out.println("Available bikes:");
        for (Bike bike : bikes) {
            if (bike.getQuantity() > 0) {
                System.out.println(bike.toString());
            }
        }
    }

    /*****
     * This method searches the available bikes for the one with the given id number. It creates a new PurchasedBike
     * and adds it to the ShoppingCart arraylist if a sufficient quantity of the desired Bike is available.
     *
     * @param id id of the cart being added
     * @param insurance whether the user wants to buy insurance
     * @param quantity how many of the bike the user wants to buy
     */

    public void addToCart(int id, int quantity, int insurance) {
        boolean alreadyInCart = false;
        for (PurchasedBike pb : shoppingCart) {
            if (pb.getId() == id) {
                pb.setQuantity(pb.getQuantity() + quantity);
                alreadyInCart = true;
            }
        }
        if (! alreadyInCart) {
            Bike bikeToAdd = null;
            for (Bike bike : bikes) {
                if (bike.getId() == id) {
                    bikeToAdd = bike;
                }
            }
            if (bikeToAdd == null || bikeToAdd.getQuantity() < quantity) {
                System.out.println("Error: unavailable");
            } else {
                double purchasePrice = quantity * bikeToAdd.getPrice();
                boolean insured = false;
                if (insurance == 1) {
                    insured = true;
                    purchasePrice += 50.0;
                }
                bikeToAdd.setQuantity(quantity);
                PurchasedBike newPurchase = new PurchasedBike(bikeToAdd, purchasePrice, insured);
                shoppingCart.add(newPurchase);
                System.out.println("Added to cart!");
            }
        }
    }

    /*****
     * This method prints out all the bikes in the shopping cart
     *
     */
    public void viewCart() {
        System.out.println("Bikes in cart:");
        for (PurchasedBike bike : shoppingCart) {
            System.out.println(bike.toString());
        }
    }
    /*****
     * This method iterates through the shopping cart and adds all of them to purchased bikes. It also updates the list
     * of available bikes by searching for the corresponding bike based on id and reducing the quantity by the amount
     * being purchased.
     */
    public void checkout() {
        for (Bike b : bikes) {
            for (PurchasedBike pb : shoppingCart) {
                if (pb.getId() == b.getId()) {
                    b.setQuantity(b.getQuantity() - pb.getQuantity());
                }
                purchasedBikes.add(pb);
            }
        }
        
        // adjusting the quantities of seller inventories based on what the buyer bought
        for (Seller se : UserInfo.getSellers()) {
            for (Bike bi : se.getInventory()) {
                for (PurchasedBike pb : shoppingCart) {
                    if (pb.getId() == bi.getId()) {
                        bi.setQuantity(bi.getQuantity() - pb.getQuantity());
                    }
                }
            }
        }
        // dumping everything from shopping cart to purchased bikes
        int len = shoppingCart.size();
        for (int i = 0; i < len; i++) {
            purchasedBikes.add(shoppingCart.get(0));
            shoppingCart.remove(0);
        }

        
        
        

        System.out.println("Checked out successfully.");
    }

    /*****
     * This method iterates through the shoppingCart and removes the bike with the given ID from the shopping cart.
     *
     * @param id the id of the bike to be removed
     */

    public void removeBike(int id) {
        PurchasedBike bikeToRemove = null;
        for (PurchasedBike bike: shoppingCart) {
            if(bike.getId() == id) {
                shoppingCart.remove(bike);
                bikeToRemove = bike;
                System.out.println("Bike removed!");
                break;
            }
        }
        if (bikeToRemove == null) {
            System.out.println("No bike found!");
        } else {
            Bike bike = new Bike(bikeToRemove);
            bikes.add(bike);
        }
    }

    /*****
     * This method iterates through the PurchasedBikes and exports info for each one into a file with the given name
     *
     * @param fileName User-specified name of the file to add to
     */

    public void getPurchaseHistory(String fileName) {
        try {
            File file = new File(fileName);
            PrintWriter pw = new PrintWriter(file);
            for (PurchasedBike b : purchasedBikes) {
                pw.println(b.toString());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*****
     * This method removes the user from the list of buyers stored in Login
     *
     * @param user username of account to delete
     */
    public void deleteAccount(String user) {
        ArrayList<Buyer> buyers = UserInfo.getBuyers();
        for (Buyer buyer : buyers) {
            if(buyer.getUsername().equalsIgnoreCase(user)) {
                buyers.remove(buyer);
                UserInfo.setBuyers(buyers);
                System.out.println("Account deleted.");
            }
        }
    }

    /*****
     * This method updates available bikes
     *
     * @param bikes updated arraylist of available bikes
     */

    public void updateListings(ArrayList<Bike> bikes) {
        this.bikes = bikes;
    }
}
