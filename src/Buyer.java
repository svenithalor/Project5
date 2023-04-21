import java.util.*;
import java.io.*;

/*****************
 * The Buyer class initializes the attributes and methods specific to a buyer in the Boilermaker Bikes platform
 * @author Christina Joslin, lab sec 4427
 * @version 4/7/2023
 */
public class Buyer {

    private ArrayList<PurchasedBike> shoppingCart = new ArrayList<>(); //shopping cart where the user can store their
    // purchase bikes
    private ArrayList<PurchasedBike> purchaseHistory = new ArrayList<>(); //list of bikes purchased by the user
    private String username; //stores the username of this buyer

    public Buyer(String username, ArrayList<PurchasedBike> shoppingCart, ArrayList<PurchasedBike> purchaseHistory) {
        this.username = username;
        //if the initial shopping cart is null, then set the arraylist to empty
        if (shoppingCart == null) {
            this.shoppingCart = new ArrayList<>();
        } else {
            this.shoppingCart = shoppingCart;
        }
        //if the purchasing history is null, then set the arraylist to empty

        if (purchaseHistory == null) {
            this.purchaseHistory = new ArrayList<>();
        } else {
            this.purchaseHistory = purchaseHistory;
        }

    }

    //Methods

    /********
     * Gets the user's username
     * @return the user's username
     */
    public String getUsername() {
        return username;
    }

    /*************
     * Returns the shopping cart of this buyer
     * @return the Shopping Cart of this buyer
     */
    public ArrayList<PurchasedBike> getShoppingCart() {
        return shoppingCart;
    }

    public ArrayList<PurchasedBike> getPurchaseHistory() {
        return purchaseHistory;
    }

    /***********
     * Updates the shopping cart of this buyer
     * @param shoppingCart of this buyer
     */
    public void setShoppingCart(ArrayList<PurchasedBike> shoppingCart) {
        //if the shopping cart is set to null, then ensure that the shopping cart is empty
        if (shoppingCart == null) {
            this.shoppingCart = new ArrayList<PurchasedBike>(0);
        } else {
            this.shoppingCart = shoppingCart;
        }

    }

    /********
     * Updates the list of bikes purchased by this buyer
     * @param purchaseHistory of this buyer
     */
    public void setPurchaseHistory(ArrayList<PurchasedBike> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    /***************
     * Displays the bike id, model name, final price, and quantity purchased of the bike in the shopping cart
     * @return 2D object array to be displayed in a JTable in the shopping cart
     */
    public Object[][] shoppingCartInfo() {
        Object[][] items; //a 2 dimension array used to display the items in the user's shopping cart
        ArrayList<Object[]> temp = new ArrayList<>(); //temporarily stores an individual array container the information for each purchased bike

        //iterates through each bike in the user's shopping cart, creates an array out of hte information
        // and temporarily stores it in an arraylist

        for (PurchasedBike bike : getShoppingCart()) {
            Object[] it = new Object[]{"" + bike.getId(), "" + bike.getModelName(), "$" + bike.getFinalPrice(), "" + bike.getQuantity()};
            temp.add(it);

        }
        //converts the arraylist information into the 2D object array used for the JTable
        items = temp.toArray(new Object[0][0]);

        return items;
    }

    @Override
    /********
     * This method is used to import/export buyer information in Login.java in the following format
     * username: [insert name]
     *[insert name].shoppingcart color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,
     * insured
     *[insert name].purchasehistory color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,
     * insured
     *********/
    public String toString() {
        StringBuilder message = new StringBuilder(String.format("username: %s%n", username));
        //identifies the bikes in the shopping cart
        for (PurchasedBike bike : shoppingCart) {
            message.append(String.format("%s.shoppingcart ", username));
            message.append(bike.toString()).append("\n");
        }
        //identifies the bikes in the purchasing history
        for (PurchasedBike bike : purchaseHistory) {
            message.append(String.format("%s.purchasehistory ", username));
            message.append(bike.toString()).append("\n");
        }
        return message.toString();
    }
}
