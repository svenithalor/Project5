import java.util.*;
import java.io.*;

/*****************
 * The Buyer class initializes the attributes and methods specific to a buyer in the Boilermaker Bikes platform
 * @author Christina Joslin, lab sec 4427
 * @version 4/7/2023
 */
public class Buyer {

    private ArrayList<PurchasedBike> shoppingCart; //shopping cart where the user can store their
    // purchase bikes
    private ArrayList<PurchasedBike> purchaseHistory; //list of bikes purchased by the user
    private String username; //stores the username of this buyer
    private String password; //stores the password of this buyer

    public Buyer(String username,String password, ArrayList<PurchasedBike> shoppingCart,
                 ArrayList<PurchasedBike> purchaseHistory) {
        this.username = username;
        this.password= password;
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

    /********
     * Gets the user's password
     * @return the user's password
     */
    public String getPassword() {
        return password;
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


    @Override
    /********
     * This method is used to import/export buyer information in Login.java in the following format
     * username: [insert name]
     * password: [insert 5 characters]
     *[insert name].shoppingcart color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,
     * insured
     *[insert name].purchasehistory color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,
     * insured
     *********/
    public String toString() {
        StringBuilder message = new StringBuilder(String.format("username: %s%n", username));
        message.append(String.format("password: %s%n",password));
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
