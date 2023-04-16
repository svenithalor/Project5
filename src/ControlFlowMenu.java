import java.util.*;
import java.io.*;
/***************
 *The ControlFlowMenu class allows the user to navigate to the buyer or seller pages on the Boilermaker Bikes site
 *
 * @author Christina Joslin, lab sec 4427
 * @version 4/7/2023
 */
public class ControlFlowMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login(); //initializes the login object
        boolean validInput = true;
        int userType = 0;
        //Welcome message
        do {
            System.out.println("Welcome to Boilermaker Bike Shop!");
            //Asks for the user type and stores it
            System.out.println("Are you a buyer or a seller?");
            System.out.println("1. Buyer\n2. Seller");
            userType = scanner.nextInt();  //keeps track of the user type
            scanner.nextLine();
            if (userType != 1 && userType != 2) {
                System.out.println("Invalid input!");
                validInput = false;
            }
        } while (!validInput);
        //takes the user to the login class
        int userIndex = login.userLogin(scanner, userType);
        // System.out.println("Bikes from login: " + bikes);
        if (userIndex != -1) {
            if(userType == 1) {
                Buyer thisBuyer = UserInfo.getBuyers().get(userIndex);
                CustomerPage cp = new CustomerPage(UserInfo.getBikes(), thisBuyer);
                cp.open(thisBuyer);
            } else if (userType == 2) {
                Seller thisSeller = UserInfo.getSellers().get(userIndex);
                SellerPage sp = new SellerPage(thisSeller.getUsername(), thisSeller.getInventory());
                sp.runSellerPage(thisSeller);
            }
        }
        System.out.println("Thanks for shopping at Boilermaker Bikes!");

        login.userLogout(2);
        login.userLogout(1);
    }

}
