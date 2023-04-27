import javax.swing.*;

/***************
 *The ControlFlowMenu class allows the user to navigate to the login and the buyer or seller pages on the
 *Boilermaker Bikes site.
 *
 * @author Christina Joslin, lab sec 4427
 * @version 4/25/2023
 */
public class ControlFlowMenu {
    private static int userIndex; //the current location of the user in the buyer or seller arraylist database
    private static String userType; //whether the user is a buyer or a seller
    private static Buyer thisBuyer; //the current buyer navigating Boilermaker Bikes
    private static Seller thisSeller; //the current seller navigating Boilermaker Bikes
    private static String[] options = {"OK"}; //the ok button displayed when the user receives an error message

    public static void main(String[] args) {

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
            String[] options = {"OK"};
            JOptionPane.showOptionDialog(null, "Connection interrupted.  Exiting Boilermaker Bikes.",
                    "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);
            return;

        }
        /*******
         * As long as the user index is valid, make buyer client/server threads or seller client/server threads
         */

        if (userIndex != -1) {
            if (userType.equals("buyer")) {
                thisBuyer = UserInfo.getBuyers().get(userIndex);
                Thread buyerClient = new Thread() {
                    public void run() {
                        CustomerPageClient.runClient(thisBuyer);
                    }
                };

                Thread buyerServer = new Thread() {
                    public void run() {
                        CustomerPageServer.run(thisBuyer);
                    }
                };

                buyerServer.start();

                buyerClient.start();

                try {
                    buyerClient.join();
                    buyerServer.join();

                } catch (Exception e) {
                    String[] options = {"OK"};
                    JOptionPane.showOptionDialog(null, "Connection interrupted.  Exiting Boilermaker Bikes.",
                            "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, options, options[0]);

                }

            } else if (userType.equals("seller")) {
                thisSeller = UserInfo.getSellers().get(userIndex);

                Thread sellerClient = new Thread() {
                    public void run() {
                        SellerPageClient C = new SellerPageClient(thisSeller.getUsername(),thisSeller.getInventory());
                        C.runSellerPageClient(thisSeller.getUsername(),thisSeller.getInventory());
                    }
                };

                Thread sellerServer = new Thread() {
                    public void run() {
                        SellerPageServer S = new SellerPageServer(thisSeller.getUsername(),thisSeller.getInventory());
                        S.run();
                    }
                };

                sellerClient.start();
                sellerServer.start();

                try {
                    sellerClient.join();
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
