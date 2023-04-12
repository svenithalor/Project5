import java.io.*;
import java.util.*;

/*********
 * The Login class restores the previous information regarding past buyers and sellers when each session starts and
 * allows the user to save their information when they are logging out. This information is accessed via either the
 * buyer.txt file or the seller.txt file.
 * @author Christina Joslin, lab sec 4427
 * @version 4/9/2023
 */
public class Login {
    private static ArrayList<Buyer> buyers = new ArrayList<Buyer>(); //keeps track of all the buyers on the bicycle
    // website
    private static ArrayList<Seller> sellers = new ArrayList<Seller>(); //keeps track of all the sellers on the bicycle
    // website
    private static ArrayList<Bike> bikes = new ArrayList<Bike>();

    /*********
     * This method iterates through a file containing all buyer or seller information and stores it into the
     * buyer and seller arraylists.
     ******/
    public static void initialSetup() {
        String line = "";
        String username = ""; //saves the username of each buyer
        ArrayList<PurchasedBike> shoppingCart; //saves the previous shopping cart of each buyer
        ArrayList<PurchasedBike> purchaseHistory; //saves the previous purchasing history of each buyer
        ArrayList<Bike> inventory; //saves the inventory of each seller

        //Readers through the buyer.txt file
        /********
         * Format
         *
         * username: [insert name]
         *[insert name].shoppingcart color,wheelSize,price,finalPrice,modelName,used,description,sellerName,
         * quantity,insured,id
         *[insert name].purchasehistory color,wheelSize,price,finalPrice,modelName,used,description,sellerName,
         * quantity,insured,id
         * *****/
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("buyer.csv"))) {
            shoppingCart = new ArrayList<>();
            purchaseHistory = new ArrayList();
            while ((line = bufferedReader.readLine()) != null) {
                // reads the username
                if (line.startsWith("username:")) {
                    String parts[] = line.split(" ");
                    username = parts[1];
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
                if (line.startsWith("=")) {
                    //creates a buyer object to add to the database
                    Buyer buyer = new Buyer(username, shoppingCart, purchaseHistory);
                    buyers.add(buyer);
                    shoppingCart = new ArrayList<>(); //clears the shopping cart of the current user
                    purchaseHistory = new ArrayList<>(); //clears the purchasing history of the current user

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //Reads through the seller.txt file
        /********
         * Format
         *
         * username: [insert name]
         *[insert name].inventory color,wheelSize,price,modelName,used,description,sellerName,quantity,id
         * *****/
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("seller.csv"))) {
            inventory = new ArrayList<>(); //saves the inventory of the current user
            while ((line = bufferedReader.readLine()) != null) {
                // reads the username
                if (line.startsWith("username:")) {
                    String parts[] = line.split(" ");
                    username = parts[1];
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
                    bikes.add(b);
                    // System.out.println("Bike added to bikes!");
                    // System.out.println(b.toString());
                }
                if (line.startsWith("=")) {
                    //creates the seller object to add to the database
                    Seller seller = new Seller(username, inventory);
                    sellers.add(seller);
                    inventory = new ArrayList<>(); //clears the inventory of the current user
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    /********
     *This method logs the user into the Boiler Bikes website and or has them create a new account
     * @param scanner the username to be enterred by the user
     * @param userType the type of user logging in (a buyer or seller)
     */
    public int userLogin(Scanner scanner, int userType) {
        int userIndex = -1;
        initialSetup();
        switch (userType) {
            //Buyer login
            case 1:
                do {
                    System.out.println("Please enter your username: ");
                    boolean found = false;
                    String buyerName = scanner.nextLine();
                    /*********
                     * Iterates through the entire database of buyers and checks if the username already exists
                     */
                    for (Buyer buyer : buyers) {
                        if (buyer.getUsername().equals(buyerName)) {
                            found = true;
                            userIndex = buyers.indexOf(buyer);
                            break;
                        }
                    }
                    /***********
                     * If the username is found, then exit this method. If the user is not found, then have them
                     * create a new account with their own unique username
                     */
                    if (found) {
                        System.out.println("Successful login!");
                        return userIndex;
                    } else if (!found) {
                        System.out.println("User not found. Would you like to create a new account? (Enter 'yes' or " +
                                "'no')");
                        String answer = scanner.nextLine();
                        if (answer.equals("yes")) {
                            boolean success = false; //keeps track of if the buyer successfully created a new account
                            do {
                                System.out.println("Please enter a username: ");
                                buyerName = scanner.nextLine();
                                success = true;
                                //checks if that buyer name already exists
                                for (Buyer buyer : buyers) {
                                    if (buyer.getUsername().equals(buyerName)) {
                                        System.out.println("Error, this username is already taken. Try again.");
                                        success = false;
                                        break;
                                    }
                                }
                            } while (!success);

                            //Creates the new buyer's account and stores it in the buyer database
                            Buyer newBuyer = new Buyer(buyerName, null, null);
                            buyers.add(newBuyer);
                            System.out.println("Account successfully created!");
                            userIndex = buyers.indexOf(newBuyer);
                        } else if (answer.equals("no")) {
                            System.out.println("Login cancelled.");
                            System.out.println("Goodbye!");
                            userIndex = -1;
                            break;
                        }
                    }
                } while (true);
            case 2:
                //Seller login
                do {
                    System.out.println("Please enter your username: ");
                    boolean found = false;
                    String sellerName = scanner.nextLine();
                    /*********
                     * Iterates through the entire database of sellers and checks if the username already exists
                     */
                    for (Seller seller : sellers) {
                        if (seller.getUsername().equals(sellerName)) {
                            found = true;
                            userIndex = sellers.indexOf(seller);
                            break;
                        }
                    }
                    /***********
                     * If the username is found, then exit this method. If the user is not found, then have them
                     * create a new account with their own unique username
                     */
                    if (found) {
                        System.out.println("Successful login!");
                        return userIndex;
                    } else if (!found) {
                        System.out.println("User not found. Would you like to create a new account? (Enter 'yes' or " +
                                "'no')");
                        String answer = scanner.nextLine();
                        if (answer.equals("yes")) {

                            boolean success = false; //keeps track of if the seller successfully created a new account
                            do {
                                System.out.println("Please enter a username: ");
                                sellerName = scanner.nextLine();
                                success = true;
                                //checks if that seller name already exists
                                for (Seller seller : sellers) {
                                    if (seller.getUsername().equals(sellerName)) {
                                        System.out.println("Error, this username is already taken. Try again.");
                                        success = false;
                                        break;
                                    }
                                }
                            } while (!success);

                            //Creates the new seller's account and stores it in the seller database
                            Seller newSeller = new Seller(sellerName, null);
                            sellers.add(newSeller);
                            System.out.println("Account successfully created!");
                            userIndex = sellers.indexOf(newSeller);

                        } else if (answer.equals("no")) {
                            System.out.println("Login cancelled.");
                            System.out.println("Goodbye!");
                            userIndex = -1;
                            break;
                        }
                    }
                } while (true);
        }
        return userIndex;
    }

    /************
     * This method logs the user out of the application and saves their information to a file
     * @param userType the user that is logging our whether a buyer or seller
     */
    public void userLogout(int userType) {
        //Writes each buyer or seller object back into a file
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("buyer.csv", false));
            for (Buyer buyer : buyers) {
                writer.write(buyer.toString() + "=" + "\n");
                writer.flush();
            }
            writer.close();

            BufferedWriter bwriter = new BufferedWriter(new FileWriter("seller.csv", false));
            for (Seller seller : sellers) {
                bwriter.write(seller.toString() + "=" + "\n");
                bwriter.flush();
            }
            bwriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void setBuyers(ArrayList<Buyer> buyers) {
        Login.buyers = buyers;
    }

    public static ArrayList<Buyer> getBuyers() {
        return buyers;
    }

    public static void setSellers(ArrayList<Seller> sellers) {
        Login.sellers = sellers;
    }

    public static ArrayList<Seller> getSellers() {
        return sellers;
    }

    public static ArrayList<Bike> getBikes() {
        return bikes;
    }
}
