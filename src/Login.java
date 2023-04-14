import java.util.*;
/*********
 * The Login class allows the user to log in and log out of the Boilermaker Bikes site
 * @author Christina Joslin, lab sec 4427
 * @version 4/9/2023
 */
public class Login {
    private ArrayList<Buyer> buyers = new ArrayList<>(); //keeps track of all buyers from UserInfo
    private ArrayList<Seller> sellers = new ArrayList<Seller>(); //keeps track of all the sellers from UserInfo
    private ArrayList<Bike> bikes = new ArrayList<Bike>(); //keeps track of all bikes for sale from Userinfo

    //Constructor
    public Login() {
        this.sellers = UserInfo.getSellers();
        this.buyers = UserInfo.getBuyers();
        this.bikes = UserInfo.getBikes();
    }


    //Methods


    /********
     *This method logs the user into the Boiler Bikes website and or has them create a new account
     * @param scanner the username to be enterred by the user
     * @param userType the type of user logging in (a buyer or seller)
     */
    public int userLogin(Scanner scanner, int userType) {
        UserInfo.readUsers();
        int userIndex = -1;
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
                            Seller newSeller = new Seller(sellerName, null, 0.0);
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
        UserInfo.writeUsers();
    }

}