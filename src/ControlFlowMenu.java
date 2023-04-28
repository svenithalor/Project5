import javax.swing.*;
import java.util.concurrent.*;

/***************
 *The ControlFlowMenu class allows the user to navigate to the login and the buyer or seller pages on the
 *Boilermaker Bikes site.
 *
 * @author Christina Joslin, lab sec 4427
 * @version 4/27/2023
 */
public class ControlFlowMenu {
    private static int userIndex; //the current location of the user in the buyer or seller arraylist database
    private static String userType; //whether the user is a buyer or a seller
    private static Buyer thisBuyer; //the current buyer navigating Boilermaker Bikes
    private static Seller thisSeller; //the current seller navigating Boilermaker Bikes
    private static String[] options = {"OK"}; //the ok button displayed when the user receives an error message
    private static ExecutorService pool = Executors.newFixedThreadPool(4);  //uses the executor service to create a limited number of threads for each client and server


    public static void main(String[] args) {
        ControlFlowMenu user = new ControlFlowMenu(); //creates an object of the control flow menu to create new threads
        //Thread newUser = new Thread(user); do
        //pool.execute(user); do more research on this

        //We need to put in the final element of concurrency where we run each buyer and seller as a separate thread
        //need to close the socket, reader,writer, etc.
        //need to fix the exit points (cancel,close,yes,no,ok)
        //need to connect the login and start writing to a file


        //opens up the LoginClient thread
        Thread loginClient = new Thread() {
            public void run() {
                LoginClient.run();
            }
        };
        //opens up the LoginServer Thread
        Thread loginServer = new Thread() {
            public void run() {
                String userInfo = LoginServer.run();

                //converts the information from the user login into their current index in the database and the user type (buyer or seller)
                String[] parts = userInfo.split(",");
                userIndex = Integer.parseInt(parts[0]);
                userType = parts[1];
            }
        };
        loginServer.start();
        loginClient.start();

        try {
            loginServer.join();
            loginClient.join();

        } catch (Exception e) {
            return;

        }
        /*******
         * As long as the user index is valid, make buyer client/server threads or seller client/server threads
         */

        if (userIndex != -1) {
            if (userType.equals("buyer")) {
                thisBuyer = UserInfo.getBuyers().get(userIndex);
                //opens up the buyer server
                Thread buyerServer = new Thread() {
                    public void run() {
                        CustomerPageServer.run(thisBuyer);
                    }
                };
                buyerServer.start();

                try {
                    buyerServer.join();

                } catch (Exception e) {
                    String[] options = {"OK"};
                    JOptionPane.showOptionDialog(null, "Connection interrupted.  Exiting Boilermaker Bikes.",
                            "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                }

            } else if (userType.equals("seller")) {
                thisSeller = UserInfo.getSellers().get(userIndex);
                //opens up the seller page server
                Thread sellerServer = new Thread() {
                    public void run() {
                        SellerPageServer S = new SellerPageServer(thisSeller.getUsername(), thisSeller.getInventory());
                        S.run();
                    }
                };
                sellerServer.start();

                try {
                    sellerServer.join();

                } catch (Exception e) {
                    String[] options = {"OK"};
                    JOptionPane.showOptionDialog(null, "Connection interrupted.  Exiting Boilermaker Bikes.",
                            "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                }

            }
        } else {
            JOptionPane.showOptionDialog(null, "Connection interrupted. Exiting Boilermaker Bikes.",
                    "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        }


    }

}

/*******
 *   String[] options = {"OK"};
 *             JOptionPane.showOptionDialog(null, "Connection interrupted.  Exiting Boilermaker Bikes.",
 *                     "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
 */

