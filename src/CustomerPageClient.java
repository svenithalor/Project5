import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;

public class CustomerPageClient {
    private String searchTerm;
    public static void run(Buyer buyer) {
        try {
            Socket socket = new Socket("localhost", 1234);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            int repeat = 1;
            CustomerPageClient C = new CustomerPageClient();  //creates a CustomerPage object to be used for processing
            do {
                String bikeNames = reader.readLine();
                int choice = C.displayMainMenu(writer,reader);
                writer.println(choice);
                writer.flush();

                switch (choice) {
                    case -1: repeat = 0;
                        break;
                    case 1: // main menu option 1: display bikes
                        int repeat1 = 1;
                        do {
                        int choice1 = C.displayBikesMenu(writer, reader, bikeNames);

                        writer.println(choice1);
                        writer.flush();

                        switch (choice1) { // switch of choices within view bikes
                            default: // when index of a selected bike is returned
                                String bikeDescription = reader.readLine();
                                bikeDescription += "\n" + reader.readLine();
                                bikeDescription += "\n" + reader.readLine();
                                String[] buttons = {"Go back", "Add to cart"};
                                int option = JOptionPane.showOptionDialog(null, bikeDescription, "Boilermaker Bikes",
                                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, null);
                                if (option == 0) {
                                    break;
                                } else {
                                    // TODO: add to cart

                                    break;
                                }
                            case -2, -3: // sorting bikes
                                String sortedBikeInfo = reader.readLine();
                                C.displayBikesMenu(writer, reader, sortedBikeInfo); //TODO: processing because of different indexes in matches arraylist
                                break;
                            case -1: // go back
                                repeat1 = 0;
                                break;
                            case -4: // search
                                writer.println(C.searchTerm);
                                writer.flush();
                                String result = reader.readLine();
                                if (!result.equals("-1")) {
                                    C.displayBikesMenu(writer, reader, result); //TODO: processing because of different indexes in matches arraylist
                                } else {
                                    JOptionPane.showMessageDialog(null, "No matches found!");
                                }
                                break;
                        } } while (repeat1 == 1);
                        break;
                    case 2: // option 2: view cart
                        // TODO: view/edit cart and checkout
                        //Takes them to the shopping cart...
                        break;
                    case 3: // option 3: view purchase history
                        String fileName = JOptionPane.showInputDialog("Enter name of file to export data to");
                        writer.println(fileName);
                        writer.flush();
                        String success = reader.readLine();
                        if (success.equals("true")) {
                            JOptionPane.showMessageDialog(null, "Success!");
                        } else if (success.equals("false")) {
                            JOptionPane.showMessageDialog(null, "An error occurred, try again!");
                        }
                        break;
                    case 4: // option 4: logout
                        repeat = 0;
                        LoginClient.userLogout();
                        break;
                    case 5: // option 5: delete account
                        String confirm = JOptionPane.showInputDialog("Enter username to confirm account deletion");
                        if (confirm != null) {
                            writer.println(confirm);
                            writer.flush();
                            writer.println(buyer.getUsername());
                            writer.flush();
                            int buyerIndex = UserInfo.getBuyers().indexOf(buyer);
                            writer.println(buyerIndex);
                            String deleted = reader.readLine();
                            if (deleted.equals("true")) {
                                JOptionPane.showMessageDialog(null, "Account deleted successfully!");
                                repeat = 0;
                            } else if (deleted.equals("false")) {
                                JOptionPane.showMessageDialog(null, "Try again!");
                            }
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
            return dropdown.getSelectedIndex() + 1;
        } else {
            return -1;
        }
    }

    public int displayBikesMenu(PrintWriter writer, BufferedReader reader, String bikeNames) throws IOException {

        // main menu option 1: display bikes
        String[] bikeNamesArray = bikeNames.substring(1, bikeNames.length() - 1).split(",");
        String[] buttons = {"Search", "Sort by price", "Sort by quantity", "View bike", " Go back"};
        JPanel panel = new JPanel();
        JTextField searchField = new JTextField("Enter search", 10);
        JComboBox dropdown = new JComboBox(bikeNamesArray);

        panel.add(dropdown);
        panel.add(searchField);
        int option = JOptionPane.showOptionDialog(null, panel, "Boilermaker Bikes",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, null);

        switch (option) {
            case 0: // search
                this.searchTerm = searchField.getText();
                return -4;
            case 1: // sort by price
                return -2;
            case 2: // sort by quantity
                return -3;
            case 3: // view bike
                return dropdown.getSelectedIndex();
            case 4: // go back
                return -1;
            default:
                return -1;
        }
    }


}
