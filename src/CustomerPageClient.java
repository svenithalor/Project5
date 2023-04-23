import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class CustomerPageClient {
    //TODO
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 4242);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            int repeat = 1;
            CustomerPageClient C = new CustomerPageClient();  //creates a CustomerPage object to be used for processing
            do {
               //I created a method called displayMenu that would just consistently display
                //the menu and return the choice made by the user
                int choice = C.displayMainMenu(writer,reader);

                //checks if the user attempts to exit. If they do, then end the program
                if (choice == -1) {
                    repeat = 0;
                }

                switch (choice) {
                    case 1:
                        int choice1 = C.displayBikesMenu(writer, reader); // main menu option 1: display bikes
                        if (choice1 == -1) {
                            break;
                        }
                        writer.println(choice1);
                        writer.flush();
                        String bikeDescription = reader.readLine();
                        bikeDescription += "\n" + reader.readLine();
                        bikeDescription += "\n" + reader.readLine();
                        bikeDescription += "\nAdd bike to cart?";
                        int option = JOptionPane.showConfirmDialog(null, bikeDescription, "Boilermaker Bikes",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        System.out.println("1. Sort bikes by quantity available");
                        System.out.println("2. Sort bikes by price");
                        System.out.println("5. Search products");

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
                        //Takes them to the shopping cart...
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
                        LoginClient.userLogout();
                        break;
                    case 5: // option 5: delete account
                        System.out.println("Enter username to confirm account deletion or enter 1 to cancel");
                        String confirm = scanner.nextLine();
                        writer.println(confirm);
                        String deleted = reader.readLine();
                        if (confirm.equals("1")) {
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
            JOptionPane.showMessageDialog(null,"Thank you for visiting Boilermaker Bikes!",
                    "Boilermaker Bikes", JOptionPane.INFORMATION_MESSAGE);
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace(); //temporary messure just to make sure everything is working
        } catch (IOException ioe) {
            ioe.printStackTrace(); //temporary measure just to make sure everything is working
        }

    }

    /******
     * This method displays the customer page menu to the user and returns the menu item that they selected
     * @param writer to write the choice made by the buyer to the server
     * @param reader to close the BufferedReader in the event that the buyer exits out of the menu
     * @return the menu item selected by the user
     * @author Christina Joslin
     */
    public int displayMainMenu(PrintWriter writer, BufferedReader reader) throws IOException {
        //Creates a dropdown menu for the buyer to scroll through the menu options
        //Just an idea for how the dropdown can be implemented
        JPanel panel = new JPanel();
        JLabel label1 = new JLabel("Select an option: ");
        panel.add(label1);
        JComboBox dropdown = new JComboBox(new String[]{"1. View all available bikes","2. Review cart",
                "3. Get purchase history","4. Logout","5. Delete account"});
        panel.add(dropdown);
        int option = JOptionPane.showConfirmDialog(null, panel, "Boilermaker Bikes",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            //sends the chosen option to the server to be processed and then returns this index to the user
            writer.write("" + dropdown.getSelectedIndex() + 1);
            writer.println();
            writer.flush();
            return dropdown.getSelectedIndex() + 1;
        } else {
            return -1;
        }
    }

    public int displayBikesMenu(PrintWriter writer, BufferedReader reader) throws IOException {

        String bikeNames = reader.readLine(); // main menu option 1: display bikes
        String[] bikeNamesArray = bikeNames.substring(1, bikeNames.length() - 1).split(",");
        String[] buttons = {"Search","Sort by price", "Sort by quantity", "View bike", " Go back"};
        JPanel panel = new JPanel();
        JTextField searchField = new JTextField("Enter search", 10);
        JComboBox dropdown = new JComboBox(bikeNamesArray);

        panel.add(dropdown);
        panel.add(searchField);
        int option = JOptionPane.showOptionDialog(null, panel, "Boilermaker Bikes",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, null);
        System.out.println("option: " + option); // view bike = 3, sort quantity = 2, price = 1, search = 0
        switch(option) {
            case 0:
                String search = searchField.getText();
                return 10;
            case 1:
                return 10;
            case 2:
                return 10;
            case 3:
                return dropdown.getSelectedIndex();
            case 4:
                return -1;
            default:
                return 10;
        }
    }


}
