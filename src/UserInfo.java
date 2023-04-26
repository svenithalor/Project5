import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/*********
 * This class stores user information reads past user information and writes current user information
 * into buyer.csv and seller.csv
 * @author Christina Joslin, lab sec 4427
 * @version 4/14/2023
 */
public class UserInfo {
    /********
     * Stores all previous and new buyers
     */
    private static ArrayList<Buyer> buyers = new ArrayList<Buyer>();
    /*****
     * Stores all previous and new sellers
     */
    private static ArrayList<Seller> sellers = new ArrayList<Seller>();
    /*******
     * Stores all bikes that are for sale. A bike is for sale if it is located in a seller's inventory
     */
    private static ArrayList<Bike> bikes = new ArrayList<Bike>();


    //Methods

    /*********
     * Updates the buyers database
     * @param buyers of Boilermaker Bikes
     */
    synchronized public static void setBuyers(ArrayList<Buyer> buyers) {
        UserInfo.buyers = buyers;
    }

    /*********
     * Returns the buyers database
     * @return buyers of Boilermaker Bikes
     */
    public static ArrayList<Buyer> getBuyers() {
        return buyers;
    }

    /********
     * Updates the sellers database
     * @param sellers of Boilermaker Bikes
     */
    synchronized public static void setSellers(ArrayList<Seller> sellers) {
        UserInfo.sellers = sellers;
    }

    /***********
     * Returns the seller database
     * @return seller of Boilermaker Bikes
     */
    public static ArrayList<Seller> getSellers() {
        return sellers;
    }

    /*********
     * Returns all the available bikes for sale
     * @return the available bikes for sale
     */
    public static ArrayList<Bike> getBikes() {
        return bikes;
    }

    /*********
     * Updates the available bikes for sale
     * @param bikes available for sale
     */
    synchronized public static void setBikes(ArrayList<Bike> bikes) {
        UserInfo.bikes = bikes;
    }

    /********
     * This method reads past user information and stores each user into either the buyer arraylist or the
     * seller arraylist. If the user is a seller, then the bikes associated with their account will be put into the
     * bikes arraylist to be made available to the user .
     */
    synchronized public static void readUsers() {

        String line = ""; //empty line to reach through each file

        /******
         * Part One: Reads through the buyer information
         *
         * Format (each line represents a bike in the buyer's shopping cart or in their purchase history)
         *
         * username: [insert name1]
         * password: [insert 5 characters here]
         * [insert name1].shoppingcart color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured,id
         * [insert name1].shoppingcart color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured,id
         * [insert name1].purchasehistory color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured,id
         * [insert name1].purchasehistory color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured,id
         * **********
         * username: [insert name2]
         * password: [insert 5 characters here]
         * [insert name2].shoppingcart color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured,id
         * [insert name2].shoppingcart color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured,id
         * [insert name2].purchasehistory color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured,id
         * [insert name2].purchasehistory color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured,id
         * [insert name2].purchasehistory color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured,id
         * [insert name2].purchasehistory color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured,id
         * **********
         *
         *
         *******/
        String username = ""; //saves the username of each buyer
        String password = ""; //save the password of each buyer
        ArrayList<PurchasedBike> shoppingCart; //saves the previous shopping cart of each buyer
        ArrayList<PurchasedBike> purchaseHistory; //saves the previous purchasing history of each buyer
        ArrayList<Bike> inventory; //saves the inventory of each seller

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("buyer.csv"))) {
            shoppingCart = new ArrayList<>();
            purchaseHistory = new ArrayList();
            while ((line = bufferedReader.readLine()) != null) {
                // reads the username
                if (line.startsWith("username:")) {
                    String[] parts = line.split(" ");
                    username = parts[1];
                }
                //reads the password
                if (line.startsWith("password:")) {
                    String[] parts = line.split(" ");
                    password = parts[1];
                }
                //reads through the shopping cart
                if (line.startsWith(String.format("%s.shoppingcart", username))) {
                    String part = line.substring(line.indexOf(" ") + 1);
                    String[] bikeElements = part.split(",");
                    String color = bikeElements[0];
                    int wheelSize = Integer.parseInt(bikeElements[1]);
                    double price = Double.parseDouble(bikeElements[2]);
                    double finalPrice = Double.parseDouble(bikeElements[3]);
                    String modelName = bikeElements[4];
                    Boolean used = Boolean.parseBoolean(bikeElements[5]);
                    String description = bikeElements[6];
                    String sellerName = bikeElements[7];
                    int quantity = Integer.parseInt(bikeElements[8]);
                    boolean insured = Boolean.parseBoolean(bikeElements[9]);
                    int id = Integer.parseInt(bikeElements[10]);
                    shoppingCart.add(new PurchasedBike(color, wheelSize, price, finalPrice, modelName, used,
                            description, sellerName, quantity, insured, id));

                }
                //reads through the purchase history
                if (line.startsWith(String.format("%s.purchasehistory", username))) {
                    String part = line.substring(line.indexOf(" ") + 1);
                    String[] bikeElements = part.split(",");
                    String color = bikeElements[0];
                    int wheelSize = Integer.parseInt(bikeElements[1]);
                    double price = Double.parseDouble(bikeElements[2]);
                    double finalPrice = Double.parseDouble(bikeElements[3]);
                    String modelName = bikeElements[4];
                    Boolean used = Boolean.parseBoolean(bikeElements[5]);
                    String description = bikeElements[6];
                    String sellerName = bikeElements[7];
                    int quantity = Integer.parseInt(bikeElements[8]);
                    boolean insured = Boolean.parseBoolean(bikeElements[9]);
                    int id = Integer.parseInt(bikeElements[10]);
                    purchaseHistory.add(new PurchasedBike(color, wheelSize, price, finalPrice, modelName, used,
                            description, sellerName, quantity, insured, id));

                }
                if (line.startsWith("*")) {
                    //creates a buyer object to add to the database
                    Buyer buyer = new Buyer(username, password, shoppingCart, purchaseHistory);
                    buyers.add(buyer);
                    shoppingCart = new ArrayList<>(); //clears the shopping cart of the current user to read in a new user
                    purchaseHistory = new ArrayList<>(); //clears the purchasing history of the current user to read in a new user

                }

            }
            //System.out.println("Size of Buyers: " + buyers.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /********
         * Part 2: Reads through the seller information
         *
         * Format (each line represents a bike in a seller' inventory)
         *
         * username: [insert name1]
         * password: [insert 5 characters here]
         * [insert name1].inventory color,wheelSize,price,modelName,used,description,sellerName,quantity,id
         * [insert name1].inventory color,wheelSize,price,modelName,used,description,sellerName,quantity,id
         * [insert name1].inventory color,wheelSize,price,modelName,used,description,sellerName,quantity,id
         * **********
         * username: [insert name2]
         * password: [insert 5 characters here]
         * [insert name2].inventory color,wheelSize,price,modelName,used,description,sellerName,quantity,id
         * [insert name2].inventory color,wheelSize,price,modelName,used,description,sellerName,quantity,id
         * **********
         *
         * *****/
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("seller.csv"))) {
            inventory = new ArrayList<>(); //reads the inventory of the a user
            double revenue = 0.0; //reads the revenue of a user
            while ((line = bufferedReader.readLine()) != null) {
                // reads the username
                if (line.startsWith("username:")) {
                    String[] parts = line.split(" ");
                    username = parts[1];
                }
                //reads the password
                if (line.startsWith("password:")) {
                    String[] parts = line.split(" ");
                    password = parts[1];
                }
                //reads through the inventory
                if (line.startsWith(String.format("%s.inventory", username))) {
                    String part = line.substring(line.indexOf(" ") + 1);
                    String[] bikeElements = part.split(",");
                    String color = bikeElements[0];
                    int wheelSize = Integer.parseInt(bikeElements[1]);
                    double price = Double.parseDouble(bikeElements[2]);
                    String modelName = bikeElements[3];
                    Boolean used = Boolean.parseBoolean(bikeElements[4]);
                    String description = bikeElements[5];
                    String sellerName = bikeElements[6];
                    int quantity = Integer.parseInt(bikeElements[7]);
                    int id = Integer.parseInt(bikeElements[8]);
                    Bike b = new Bike(color, wheelSize, price, modelName, used, description, sellerName, quantity,
                            id);
                    inventory.add(b);  //adds a bike to the inventory
                    bikes.add(b); //adds a bike to list of bikes for sale
                    // System.out.println("Bike added to bikes!");
                    // System.out.println(b.toString());
                }
                if (line.startsWith("*")) {
                    //creates the seller object to add to the database
                    Seller seller = new Seller(username, password, inventory);
                    sellers.add(seller);
                    inventory = new ArrayList<>(); //clears the inventory of the current user to read in another user
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /*********
     * This method writes existing user information into both buyer.csv and seller.csv
     */
    synchronized public static void writeUsers() {
        //Writes each buyer back into buyer.csv using the same format
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("buyer.csv", false));
            for (Buyer buyer : buyers) {
                writer.write(buyer.toString() + "***********" + "\n");
                writer.flush();
            }
            writer.close();

            //Writes each seller back into seller.csv using the same format
            BufferedWriter bwriter = new BufferedWriter(new FileWriter("seller.csv", false));
            for (Seller seller : sellers) {
                bwriter.write(seller.toString() + "**********" + "\n");
                bwriter.flush();
            }
            bwriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**********
     * This method allows the user to search for the index of a bike they are looking for on the listing page
     * @param bikeId the 4 digit unique bike id entered by the user
     * @return the bike with the matching id entered
     */
    public static Bike searchBike(int bikeId) {
        for (Bike b : bikes) {
            if (bikeId == b.getId()) {
                return b;
            }
        }
        return null;
    }

    /*******
     * This method allows for the user to get the index of a buyer they are looking for in the buyers database
     * @return the index of the buyer they are looking for in the buyer database. If the buyer is not found, then
     * return an index of -1
     */
    public static int getBuyerIndex(Buyer buyer) {
        int index = -1;
        for (Buyer b : buyers) {
            if (b.equals(buyer)) {
                index = buyers.indexOf(b);
                break;
            }
        }
        return index;
    }

    /*******
     * This method allows for the user to get the index of a seller they are looking for in the sellers database
     * @return the index of the seller they are looking for in the sellers database. If the seller is not found, then
     * return an index of -1
     */
    public static int getSellerIndex(Seller seller) {
        int index = -1;
        for (Seller s : sellers) {
            if (s.equals(seller)) {
                index = sellers.indexOf(s);
                break;
            }
        }
        return index;
    }

    /**********
     * Overloaded method that returns index of master seller list given the Seller name String
     * @param sellerName
     * @return the index of the seller they are looking for in the sellers database. If the seller is not found, then
     * return an index of -1
     */
    public static int getSellerIndex(String sellerName) {
        int index = -1;
        for (Seller s : sellers) {
            if (s.getUsername().equals(sellerName)) {
                index = sellers.indexOf(s);
                break;
            }
        }
        return index;
    }
}
