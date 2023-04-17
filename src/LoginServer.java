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
    public int userLogin(String userType, String userName, BufferedReader reader, PrintWriter writer) {
        //reads the initial user information
        //UserInfo.readUsers();
        boolean found = false; //whether or not the username already exists
        int userIndex = -1; //index of the new or existing user in the buyer or seller arraylist

        switch (userType) {
            case "buyer":
                /*********
                 * Iterates through the entire database of buyers and checks if the username already exists
                 */
                for (Buyer buyer : buyers) {
                    if (buyer.getUsername().equals(userName)) {
                        found = true;
                        userIndex = buyers.indexOf(buyer);
                        break;
                    }
                }
                /***********
                 * If the username is found, then exit this method, return the user's index on the arraylist of buyers,
                 * and write "true" to the client
                 * If the user is not found, then have them
                 * create a new account with their own unique username
                 */
                if (found) {
                    System.out.println("Successful login!");
                    writer.write("true");
                    writer.println();
                    writer.flush();
                    return userIndex;
                }

                break;
            case "seller":
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
                System.out.println(userType);
            } else if (userType.equals("1")) {
                userType = "seller";
                System.out.println(userType);
            }

            //reads the username entered by the user
            String username = reader.readLine();

            //creates a login server object and goes to the login method
            LoginServer login = new LoginServer();

            login.userLogin(userType, username, reader, writer);

            //if the username is not found in the database, then return false, if it is found, then return true


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
