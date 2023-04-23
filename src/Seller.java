import java.util.*;

/*****************
 * The Seller class initializes the attributes and methods specific to a buyer in the Boilermaker Bikes platform
 * @author Christina Joslin, lab sec 4427
 * @version 4/7/2023
 */
public class Seller {
    private ArrayList<Bike> inventory = new ArrayList<>();  //stores the inventory of a specific seller
    private String username; //stores the username of this seller
    private String password; //stores the password of this seller

    //Constructor
    public Seller(String username, String password, ArrayList<Bike> inventory) {
        this.username = username;
        this.password = password;
        //if the initial seller inventory is null, then set the arraylist to empty
        if (inventory == null) {
            this.inventory = new ArrayList<>(0);
        } else {
            this.inventory = inventory;
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

    /*******
     * Return the user's password
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /********
     * Updates the user's password
     * @param password of this user
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /***********
     * Returns the inventory of this Seller
     * @return the inventory of this Seller
     */
    public ArrayList<Bike> getInventory() {
        return inventory;
    }

    /*******
     * updates the inventory of this Seller
     * @param inventory of this Seller
     */
    public void setInventory(ArrayList<Bike> inventory) {
        this.inventory = inventory;
    }

    @Override
    /********
     * This method is used to import/export seller information in log in and log out the seller
     */
    public String toString() {
        StringBuilder message = new StringBuilder(String.format("username: %s%n", getUsername()));
        message.append(String.format("password: %s%n", getPassword()));
        for (Bike bike : inventory) {
            message.append(String.format("%s.inventory ", getUsername()));
            message.append(bike.toString()).append("\n");
        }
        return message.toString();
    }
}

