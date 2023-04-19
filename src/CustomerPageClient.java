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
        scanner.nextLine();
        try {
            Socket socket = new Socket(host, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            System.out.println("Connection established!");
            int repeat = 1;
            do {
                System.out.println("Select an option: ");
                System.out.println("1. View all available bikes");
                System.out.println("2. Review cart");
                System.out.println("3. Get purchase history");
                System.out.println("4. Logout");
                System.out.println("5. Delete account");
                int choice = scanner.nextInt();
                scanner.nextLine();
                writer.println(choice);
                writer.flush();
                switch (choice) {
                    case 1: String bikeInfo = reader.readLine(); // main menu option 1: display bikes
                        while (bikeInfo != null) {
                            System.out.println(bikeInfo);
                            bikeInfo = reader.readLine();
                        }
                        System.out.println("1. Sort bikes by quantity available");
                        System.out.println("2. Sort bikes by price");
                        System.out.println("3. View bike");
                        System.out.println("4. Go back");
                        System.out.println("5. Search products");
                        int choice1 = scanner.nextInt();
                        scanner.nextLine();
                        writer.println(choice1);
                        writer.flush();
                        switch (choice1) { // switch of choices within view bikes
                            case 1, 2: // sorting bikes
                                String sortedBikeInfo = reader.readLine();
                                while (sortedBikeInfo != null) {
                                    System.out.println(sortedBikeInfo);
                                    sortedBikeInfo = reader.readLine();
                                }
                                break;
                            case 3: // view bike listing
                                System.out.println("Enter id of bike to view");
                                int id = scanner.nextInt();
                                scanner.nextLine();
                                writer.println(id);
                                System.out.println(reader.readLine());
                                System.out.println("1. Add to cart");
                                System.out.println("2. Go back");
                                int cart = Integer.parseInt(scanner.nextLine());
                                if (cart == 1) {
                                    // TODO: add to cart implementation
                                }
                                break;
                            case 4: // go back
                                break;
                            case 5: // search
                                System.out.println("Enter search"); // TODO: search box text field
                                String search = scanner.nextLine();
                                writer.println(search);
                                writer.flush();
                                String result = reader.readLine();
                                while (result != null) {
                                    System.out.println(result);
                                    result = reader.readLine();
                                }
                                break;
                        }
                        break;
                    case 2: // option 2: view cart
                        // TODO: view/edit cart and checkout
                        break;
                    case 3: // option 3: view purchase history
                        System.out.println("Enter name of file to export data to");
                        String fileName = scanner.nextLine();
                        writer.println(fileName);
                        writer.flush();
                        String success = reader.readLine();
                        if (success.equals("true")) {
                            System.out.println("Success!");
                        } else if (success.equals("false")) {
                            System.out.println("An error occurred, try again!");
                        }
                        break;
                    case 4: // option 4: logout
                        repeat = 0;
                        break; // TODO: implement logout
                    case 5: // option 5: delete account
                        System.out.println("Enter username to confirm account deletion or enter 1 to cancel");
                        String confirm = scanner.nextLine();
                        writer.println(confirm);
                        String deleted = reader.readLine();
                        if (confirm.equals("1")){
                            break;
                        } else if (deleted.equals("true")) {
                            System.out.println("Account deleted successfully!");
                            repeat = 0;
                        } else if (deleted.equals("false")) {
                            System.out.println("Try again!");
                        }
                        break;
                }
            } while (repeat == 1);
        } catch(UnknownHostException uhe) {

        } catch(IOException ioe) {

        }

    }
}
