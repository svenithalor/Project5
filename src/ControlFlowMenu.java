import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
/***************
 *The ControlFlowMenu class allows the user to navigate to the buyer or seller pages on the Boilermaker Bikes site
 *
 * THIS CLASS needs ot be reorganized...please take a look at LoginClient and LoginServer
 * @author Christina Joslin, lab sec 4427
 * @version 4/7/2023
 */
public class ControlFlowMenu {

    public static void main(String[] args) {
        //opens up the LoginClient thread
        Thread loginClient = new Thread() {
            public void run() {
                LoginClient.main(null);
            }
        };
        loginClient.start();
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
                    socket.close();
                    Buyer thisBuyer = UserInfo.getBuyers().get(userIndex);
                    //creates a customer page object

                    //starts the threads for the customer and client page
                    Thread cpClient = new Thread() {
                        public void run() {
                            CustomerPageClient.main(null);
                            System.out.println("thread client is running");
                        }
                    };
                    Thread cpServer = new Thread() {
                        public void run() {
                            CustomerPageServer.main(null);
                            System.out.println("thread server is running");
                        }
                    };
                    cpClient.start();
                    cpServer.start();


                } else if (userType.equals("seller")) {
                    Seller thisSeller = UserInfo.getSellers().get(userIndex);
                    socket.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        //Implementations that direct users to their corresponding buyer/seller page now in Login.Server
        //LoginClient.userLogout();
        //LoginClient.userLogout();
    }

}
