import java.util.Scanner;

/**********
 * This class serves as the test class for the Login class (please see the buyer.txt file and the seller.txt file for
 * the official format of this file)
 * @author Christina Joslin, lab sec 4427
 * @version 4/10/2023
 */
public class TestClassLogin {
    /**********
     * Test Class for Login
     * Notes:
     * (1) Each buyer/seller is separated by an "="
     * (2) Each shopping cart/purchasing history/inventory item written as username.shoppingcart
     * attribute1,attribute2,,etc.
     * (3) For buyers/sellers with an empty purchasing history/inventory/shopping cart only the username is written
     *
     * Testing Login.Java for buyer.txt (Run the Main Method)
     *
     * Test Case 1:
     * type '1' => type c9sug => type yes => type c9sug => Output: in the buyer.txt file should be as follows...
     *
     * Test Case 2:
     * type '1' => type cJoslin => should say that you have successfully logged and display the SAME output as Test Case 1
     *
     * Output for buyer.txt:
     * username: cJoslin
     * cJoslin.shoppingcart blue,20,209.88,224.99,BikeShop,true,It is a cool bike that you will love,loversAreCool,1,true,1078
     * cJoslin.shoppingcart pink,100,280.99,540.66,AndyBike,false,The best bike in the world,RasperryPi,89,false,1097
     * cJoslin.shoppingcart green,20,209.88,224.99,CycleFlow,true,The best bike ever,loversAreCool,1,true,1078
     * cJoslin.purchasehistory red,10,208.56,225.46,BikeShop,true,These are some iconic bikes!,itsSummerTime,19,false,2985
     * cJoslin.purchasehistory green,99,270.99,305.99,BikeRus,false,This is the fastest bike in the world!,strongMe,280,true,1089
     *
     * username: haileyS
     * haileyS.shoppingcart blue,20,209.88,224.99,BikeShop,true,It is a cool bike that you will love,loversAreCool,1,true,1078
     * haileyS.shoppingcart pink,100,280.99,540.66,AndyBike,false,The best bike in the world,RasperryPi,89,false,1097
     * haileyS.shoppingcart purple,20,209.88,224.99,CycleFlow,false,Bikes are the best,loversAreCool,1,true,1078
     * haileyS.purchasehistory red,10,208.56,225.46,BikeShop,true,These are some iconic bikes!,itsSummerTime,19,false,2985
     * haileyS.purchasehistory green,99,270.99,305.99,BikeRus,false,This is the fastest bike in the world!,strongMe,280,true,1056
     * haileyS.purchasehistory orange,100,208.56,225.99,FunTimesTwo,true,These are the best bikes!,SummerIsFun,19,false,2990
     *
     * username: c9sug
     *
     *-------------------------------------------------------------
     *
     * Testing Login.java for seller.txt (Run the Main method)
     *
     * Test Case 1:
     * type '2' => type vanessaB => type yes => type vanessaB => Output: in the seller.txt file should be as follows...
     *
     * Test Case 2:
     * type '2' => type CindyM => should say that you have successfully logged and display the SAME output as Test Case 1
     * Output for seller.txt
     *
     * username: bobby
     * c9sug.inventory "blue",20,205.99,"Prius",false,"This is a really cool bike",AwesomePants,10,1045
     * c9sug.inventory "yellow",20,200.99,"Honda",true,"I am excited to be here",AwesomePantsTwo,1,9087
     * c9sug.inventory "red",100,100.99,CableRU,false,"This bike is one of the best in the country!",GoodShoes,2,8906
     * c9sug.inventory "yellow",20,200.99,StrengthForLife,true,"Bike of a lifetime for a journey of a lifetime",SmileAndHaveFun,1,6702
     * =
     * username: CindyM
     * CindyM.inventory "blue",20,205.99,"Prius",false,"This is a really cool bike",AwesomePants,10,1045
     * CindyM.inventory "red",100,100.99,CableRU,false,"This bike is one of the best in the country!",GoodShoes,2,8906
     * CindyM.inventory "yellow",20,200.99,StrengthForLife,true,"Bike of a lifetime for a journey of a lifetime",SmileAndHaveFun,1,6702
     * =
     * username: vanessaB
     * =
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        //Welcome message
        System.out.println("Welcome to Boilermaker Bike Shop!");
        //Asks for the user type and stores it
        System.out.println("Are you a buyer or a seller?");
        System.out.println("1. Buyer\n2. Seller");
        int userType = scanner.nextInt();  //keeps track of the user type
        scanner.nextLine();
        Login login = new Login();
        login.userLogin(scanner, userType);
        login.userLogout(userType);
        System.out.println("I am ready to start!");


    }
}
