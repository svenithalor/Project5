import javax.swing.*;
import java.net.Socket;
import java.util.*;
import java.io.*;
/***************
 *The ControlFlowMenu class allows the user to navigate to the buyer or seller pages on the Boilermaker Bikes site
 *
 * THIS CLASS needs ot be reorganized...please take a look at LoginClient and LoginServer
 * @author Christina Joslin, lab sec 4427
 * @version 4/7/2023
 */
public class ControlFlowMenu {

    public static void main(String[] args) {
        //Construction Zone
        Scanner scanner = new Scanner(System.in);
        Login login = new Login(); //initializes the login object
        boolean validInput = true;
        int userType = 0;

        //takes the user to the login class
        int userIndex = login.userLogin(scanner, userType);
        // System.out.println("Bikes from login: " + bikes);
        if (userIndex != -1) {
            if (userType == 1) {
                Buyer thisBuyer = UserInfo.getBuyers().get(userIndex);
                CustomerPage cp = new CustomerPage(UserInfo.getBikes(), thisBuyer);
                cp.open(thisBuyer);
            } else if (userType == 2) {
                Seller thisSeller = UserInfo.getSellers().get(userIndex);
                SellerPage sp = new SellerPage(thisSeller.getUsername(), thisSeller.getInventory());
                sp.runSellerPage(thisSeller);
            }
        }
        UserInfo.userLogout();
        UserInfo.userLogout();
    }

}
