import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.*;

/***********
 * This class handles all the data processing for the login by receiving user input from the client and sending it
 * back to be displayed to the user.
 *@author Christina Joslin, lab sec 4427
 *@version 4/14/2023
 */
public class LoginServer {
    private ArrayList<Buyer> buyers; //keeps track of all buyers from UserInfo
    private ArrayList<Seller> sellers; //keeps track of all the sellers from UserInfo
    private ArrayList<Bike> bikes; //keeps track of all bikes for sale from Userinfo

    //Constructor
    public LoginServer() {
        //initializes the values that are currently in the database
        this.sellers = UserInfo.getSellers();
        this.buyers = UserInfo.getBuyers();
        this.bikes = UserInfo.getBikes();
    }

    //Methods

    /*********
     * This method checks if a user's account already exists and has them either log in or create a new account
     * @param userType the type of user logging in (buyer of seller)
     * @param userName the username of this user
     * @param reader reads in the username and user type entered on the client side
     * @param writer writes back to the client if the username already exists
     * @return the index of the user in the database arraylist
     */
    public int userLogin(String userType, String userName, BufferedReader reader, PrintWriter writer) throws IOException {
        //reads the initial user information
        UserInfo.readUsers();
        boolean found = false; //whether or not the username already exists
        int userIndex = -1; //index of the new or existing user in the buyer or seller arraylist

        String ready = reader.readLine();
        System.out.println("Ready received from the client");

        switch (userType) {
            case "buyer":
                /*********
                 * Iterates through the entire database of buyers and checks if the username already exists.
                 * If it does exist, then send true to the client and exit this method. If it does not exist,
                 * then send false to the client which will prompt them to create a new account wiht their own unique username
                 */
                for (Buyer buyer : buyers) {
                    System.out.println("Hello World");
                    System.out.println(userName);
                    if (buyer.getUsername().equals(userName)) {
                        found = true;
                        userIndex = buyers.indexOf(buyer);
                        break;
                    }
                }
                if (found) {
                    if (ready.equals("ready")) {
                        System.out.println("Hello world! True");
                        writer.write("true");
                        writer.println();
                        writer.flush();
                        return userIndex;
                    }
                } else if (!found) {
                    if (ready.equals("ready")) {
                        System.out.println("Hello world! False");
                        writer.write("false");
                        writer.println();
                        writer.flush();
                    }
                }

                //from the client determines if the user is going to create a new account or not
                String userExited = reader.readLine();
                System.out.printf("%s received from the client%n", userExited);

                if (userExited.equals("yes")) {
                    writer.close();
                    reader.close();

                } else if (userExited.equals("no")) {
                    //set up a new account for the user
                    boolean success = false; //keeps track of if the buyer successfully created a new account
                    String newUserName = ""; //keeps track of the new username entered to create an account
                    do {
                        newUserName = reader.readLine();
                        System.out.printf("New username %s received from the server", newUserName);
                        success = true;
                        //checks if that buyer name already exists
                        for (Buyer buyer : buyers) {
                            System.out.println("Hello World");
                            System.out.println(userName);
                            if (buyer.getUsername().equals(userName)) {
                                success = true;
                                userIndex = buyers.indexOf(buyer);
                                break;
                            }
                        }
                        if (success) {
                            if (ready.equals("ready")) {
                                System.out.println("Hello world Again! True");
                                writer.write("true");
                                writer.println();
                                writer.flush();
                                return userIndex;
                            }
                        } else if (!success) {
                            if (ready.equals("ready")) {
                                System.out.println("Hello world Again! False");
                                writer.write("false");
                                writer.println();
                                writer.flush();
                            }
                        }


                    } while (!success);
                    //Creates the new buyer's account and stores it in the buyer database
                    Buyer newBuyer = new Buyer(newUserName, null, null);
                    buyers.add(newBuyer);
                }
                break;

            case "seller":

                //TODO

                /**********
                 * Iterates through the entire database of sellers and checks if the username already exists
                 ***********/
                for (Seller seller : sellers) {
                    if (seller.getUsername().equals(userName)) {
                        found = true;
                        userIndex = sellers.indexOf(seller);
                        break;
                    }
                }


                break;
        }

        return userIndex;
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
            if (userType.equals("0")) {
                userType = "buyer";
                System.out.printf("Received from the Client: %s%n", userType);
            } else if (userType.equals("1")) {
                userType = "seller";
                System.out.printf("Received from the Client: %s%n", userType);
            }

            //reads the username entered by the user
            String username = reader.readLine();

            //creates a login server object and goes to the login method
            LoginServer login = new LoginServer();

            login.userLogin(userType, username, reader, writer);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
