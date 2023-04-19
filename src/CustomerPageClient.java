import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.swing.*;
public class CustomerPageClient {
    //TODO

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to boilermaker bikes!");
        System.out.println("Enter a host name");
        String host = scanner.nextLine();
        System.out.println("Enter a port number");
        int port = scanner.nextInt();
        try {
            Socket socket = new Socket(host, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            System.out.println("Connection established!");
            int repeat = 0;
            do {
                System.out.println("Select an option: ");
                System.out.println("1. View all available bikes");
                    System.out.println("2. Sort bikes by quantity available");
                    System.out.println("3. Sort bikes by price");
                    System.out.println("4. Add a bike to cart");
                    System.out.println("7. Search products");
                System.out.println("2. Review cart");
                    System.out.println("6. Edit cart");
                    System.out.println(". Checkout");
                System.out.println("3. Get purchase history");
                System.out.println("4. Logout");
                System.out.println("5. Delete account");
                int choice = scanner.nextInt();
                writer.println(choice);
                //TODO
            } while (repeat == 1);
        } catch(UnknownHostException uhe) {

        } catch(IOException ioe) {

        }

    }
}
