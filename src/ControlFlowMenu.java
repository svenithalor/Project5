import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.*;
import java.net.*;
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
    private static final int[] localPorts = {4242,2323,4590,9876,1023,1002,1526,1345,1987,2903,2145,3145}; //the local ports available for each thread to navigate to

    /******
     * This method checks if one of the listed ports are still available. If it is then pass it to the next server-client network
     * io connection.
     * @return the next available port for a thread to use
     * @author Christina Joslin
     */
    synchronized public static int availablePort() {
        int availablePort = -1; //stores the next available port

        for (int p: localPorts) {
            try {
                ServerSocket socket = new ServerSocket(p);
                socket.close();
                availablePort = p;
                break;
            } catch (IOException e) {
            }
        }
        return availablePort;
    }

    public static void main(String[] args) {
        //checks which port is currently available
        int currentPort = availablePort();

        //opens up the LoginClient thread
        Thread loginClient = new Thread() {
            public void run() {
                LoginClient.run(currentPort);
            }
        };
        //opens up the LoginServer Thread
        Thread loginServer = new Thread() {
            public void run() {
                String userInfo = LoginServer.run(currentPort);

                //converts the information from the user login into their current index in the database and the user type (buyer or seller)
                String[] parts = userInfo.split(",");
                try {
                    userIndex = Integer.parseInt(parts[0]);
                } catch (Exception e) {
                    return;
                }
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
            if (userType == null) {
                return;
            }
            if (userType.equals("buyer")) {
                thisBuyer = UserInfo.getBuyers().get(userIndex);
                //searches for a port that the buyer can use
                int buyerPort = availablePort();

                //opens up the buyer server
                Thread buyerServer = new Thread() {
                    public void run() {
                        CustomerPageServer.run(thisBuyer,buyerPort);
                    }
                };
                buyerServer.start();

                try {
                    buyerServer.join();

                } catch (Exception e) {
                    String[] options = {"OK"};
                    JOptionPane.showOptionDialog(null, "Login cancelled. Exiting Boilermaker Bikes.",
                            "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                }

            } else if (userType.equals("seller")) {
                //searches for a port that the seller can use
                int sellerPort = availablePort();

                thisSeller = UserInfo.getSellers().get(userIndex);
                //opens up the seller page server
                Thread sellerServer = new Thread() {
                    public void run() {
                        SellerPageServer S = new SellerPageServer(thisSeller.getUsername(), thisSeller.getInventory(),sellerPort);
                        S.run();
                    }
                };
                sellerServer.start();

                try {
                    sellerServer.join();

                } catch (Exception e) {
                    String[] options = {"OK"};
                    JOptionPane.showOptionDialog(null, "Exiting Boilermaker Bikes.",
                            "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                }

            }
        } else {
            JOptionPane.showOptionDialog(null, "Exiting Boilermaker Bikes.",
                    "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
        }


    }

}


