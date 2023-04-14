import java.util.*;
/*****************
 * The Seller class initializes the attributes and methods specific to a buyer in the Boilermaker Bikes platform
 * @author Christina Joslin, lab sec 4427
 * @version 4/7/2023
 */
public class Seller {
    private ArrayList<Bike> inventory = new ArrayList<>();  //stores the inventory of a specific seller
    private String username; //stores the username of this seller
    private double revenue; //stores the revenue of this seller

    //Constructor
    public Seller(String username, ArrayList<Bike> inventory,double revenue) {
        this.username = username;
        this.revenue = revenue;
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

    /*******
     * returns the revenue of this Seller
     * @return revenue of this Seller
     */
    public double getRevenue() {
        return revenue;
    }

    /**********
     * updates the revenue of this Seller
     * @param revenue of this Seller
     */
    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    @Override
    /********
     * This method is used to import/export seller information in log in and log out the seller
     */
    public String toString() {
        StringBuilder message = new StringBuilder(String.format("username: %s%n",getUsername()));
        for (Bike bike : inventory) {
            message.append(String.format("%s.inventory ",getUsername()));
            message.append(bike.toString()).append("\n");
        }
        message.append(String.format("%s.revenue %.2f%n",getUsername(),getRevenue()));
        return message.toString();
    }
}

