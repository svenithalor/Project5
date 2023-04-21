import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.*;

/***********
 * This class handles all the data processing for the login by receiving user input from the client and sending it
 * back to be displayed to the user. This class also searches the buyer and seller databases to check if the user can
 * log in to their existing account or needs to create a new account.
 *
 *@author Christina Joslin, lab sec 4427
 *@version 4/17/2023
 */
public class LoginServer {
    private ArrayList<Buyer> buyers; //keeps track of all buyers from UserInfo
    private ArrayList<Seller> sellers; //keeps track of all the sellers from UserInfo

    //Constructor
    public LoginServer() {
        //initializes the buyer and seller databases
        this.sellers = UserInfo.getSellers();
        this.buyers = UserInfo.getBuyers();
    }

    //Methods

    /*********
     * This method checks if a user's account already exists and has them either log in or create a new account
     * @param userType the type of user logging in (buyer of seller)
     * @param reader reads in the username and user type entered on the client side
     * @param writer writes back to the client if the username already exists
     * @return the index of the user in the database arraylist
     */
    public int userLogin(String userType, BufferedReader reader, PrintWriter writer) throws IOException {
        //reads the initial user information
        UserInfo.readUsers();
        boolean found = false; //whether or not the username already exists
        int userIndex = -1; //index of the new or existing user in the buyer or seller arraylist
        int attempt = 1; //keeps track of the number of attempts made by the user to login
        String ready = ""; //used to trigger the client entered username being read through the database

        /*********
         * Iterates through the entire database of buyers or sellers and checks if the username already exists.
         * If it DOES exist, then send true to the client and have them check their password next. If it DOES NOT exist,
         * then send false to the client which will prompt them to create a new account with a unique username and password
         */
        do {
            //reads the username entered by the user
            String userName = reader.readLine();
            if (attempt == 1) {
                ready = reader.readLine();
            }
            switch (userType) {
                case "buyer":
                    for (Buyer buyer : buyers) {
                        //System.out.println("Hello World");
                        //System.out.println(userName);
                        if (buyer.getUsername().equals(userName)) {
                            found = true;
                            userIndex = buyers.indexOf(buyer);
                            break;
                        }
                    }
                    break;
                case "seller":
                    for (Seller seller : sellers) {
                        //System.out.println("Hello World");
                        //System.out.println(userName);
                        if (seller.getUsername().equals(userName)) {
                            found = true;
                            userIndex = sellers.indexOf(seller);
                            break;
                        }
                    }
                    break;
            }

            if (found) {
                if (ready.equals("ready")) {
                    // System.out.println("Hello world! True");
                    writer.write("true");
                    writer.println();
                    writer.flush();
                    //now prompts the user to enter their password
                    String password = ""; //stores the password enterred by the user
                    do {
                        password = reader.readLine();
                    } while (!passwordChecker(userType, password, userName, reader, writer));


                    return userIndex;
                }
            } else if (!found) {
                if (ready.equals("ready")) {
                    //System.out.println("Hello world! False");
                    writer.write("false");
                    writer.println();
                    writer.flush();
                }
            }
            //from the client determines if the user is going to create a new account or not
            String userExited = reader.readLine();
            //System.out.printf("%s received from the client%n", userExited);

            if (userExited.equals("yes")) {
                writer.close();
                reader.close();
                return userIndex;

            } else if (userExited.equals("no")) {
                //set up a new account for the user
                boolean success = false; //keeps track of if the buyer or seller successfully created a new account
                String newUserName = ""; //keeps track of the new username entered to create an account
                do {
                    newUserName = reader.readLine();
                    //System.out.printf("New username %s received from the server", newUserName);
                    success = true;
                    //checks if that buyer or seller name already exists
                    switch (userType) {
                        case "buyer":
                            for (Buyer buyer : buyers) {
                                //System.out.println("Hello World");
                                //System.out.println(userName);
                                if (buyer.getUsername().equals(newUserName)) {
                                    success = false;
                                    break;
                                }
                            }
                            break;
                        case "seller":
                            for (Seller seller : sellers) {
                                //System.out.println("Hello World");
                                //System.out.println(userName);
                                if (seller.getUsername().equals(newUserName)) {
                                    success = false;
                                    break;
                                }
                            }
                            break;
                    }

                    if (success) {
                        if (ready.equals("ready")) {
                            // System.out.println("Hello world Again! True");
                            writer.write("true");
                            writer.println();
                            writer.flush();
                        }
                    } else if (!success) {
                        if (ready.equals("ready")) {
                            //System.out.println("Hello world Again! False");
                            writer.write("false");
                            writer.println();
                            writer.flush();
                        }
                    }
                } while (!success);


                switch (userType) {
                    case "buyer":
                        //Creates the new buyer's account and stores it in the buyer database
                        Buyer newBuyer = new Buyer(newUserName, "", null, null);
                        buyers.add(newBuyer);
                        UserInfo.setBuyers(buyers);
                        userIndex = buyers.indexOf(newBuyer);
                        break;
                    case "seller":
                        //Creates the new seller's account and stores it in the seller database
                        Seller newSeller = new Seller(userName, null, null);
                        sellers.add(newSeller);
                        UserInfo.setSellers(sellers);
                        userIndex = sellers.indexOf(newSeller);
                        break;
                }
            }
            attempt++; //increments the number of attempts made to login
        } while (true);
    }

    /********
     * This method checks if the password entered by a user is valid or invalid and corresponds with the same valid
     * username enterred by the user
     *
     * @param password entered by the user
     * @param reader reads the password enterrd by th client to the user
     * @param writer writes back to the client whether or not the password was valid
     * @return whether the password and username combo already exists or not
     */
    public boolean passwordChecker(String userType, String password, String userName, BufferedReader reader, PrintWriter writer) {
        boolean found = false; //saves whether or not the user's password was found
        //String password = reader.readLine();
        switch (userType) {
            case "buyer":
                for (Buyer buyer : buyers) {
                    if (buyer.getPassword().equals(password) && buyer.getUsername().equals(userName)) {
                        found = true;
                        break;
                    }
                }
                break;
            case "seller":
                for (Seller seller : sellers) {
                    if (seller.getPassword().equals(password) && seller.getUsername().equals(userName)) {
                        found = true;
                        break;
                    }
                }
                break;
        }
        return found;
    }

    public static void main(String[] args) {
        //sets up the server connection
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            Socket socket = serverSocket.accept(); //waits until the client connects
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            //reads the userType entered by the user and interprets it
            String userType = reader.readLine();
            if (userType == null) {
                writer.close();
                reader.close();
            } else if (userType.equals("0")) {
                userType = "buyer";
                //System.out.printf("Received from the Client: %s%n", userType);
            } else if (userType.equals("1")) {
                userType = "seller";
                //System.out.printf("Received from the Client: %s%n", userType);
            }
            //creates a login server object and goes to the login method
            LoginServer login = new LoginServer();

            //stores the user index and sends the user either to their corresponding buyer or seller page

            int userIndex = login.userLogin(userType, reader, writer);
            //System.out.println(userIndex);

            //as long as the user index is valid, take the user to the buyer or seller menu
            if (userIndex != -1) {
                if (userType.equals("buyer")) {
                    Buyer thisBuyer = UserInfo.getBuyers().get(userIndex);
                    CustomerPage cp = new CustomerPage(UserInfo.getBikes(), thisBuyer);
                    cp.open(thisBuyer);
                } else if (userType.equals("seller")) {
                    Seller thisSeller = UserInfo.getSellers().get(userIndex);
                    SellerPage sp = new SellerPage(thisSeller.getUsername(), thisSeller.getInventory());
                    sp.runSellerPage(thisSeller);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
