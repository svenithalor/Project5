import java.util.Scanner;

/**********
 * This class serves as the test class for the Login class (please see the buyer.txt file and the seller.txt file for
 * the official format of this file)
 * @author Christina Joslin, lab sec 4427
 * @version 4/10/2023
 */
public class TestClassLogin {
    /**********
     * Test Class for UserInfo
     *
     *
     * ******/
    public static void main(String[] args) {
        UserInfo database = new UserInfo();
        database.readUsers();
        database.writeUsers();
        for (Bike b:database.getBikes()) {
            System.out.println(b.toString());
        }


    }
}
