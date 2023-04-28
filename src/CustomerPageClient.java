import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.*;
import java.io.File;

/**************
 * The CustomerPageClient class allows the buyer to navigate through the buyer menu options via a user-friendly GUI
 * application that includes displaying the central buyer menu in addition to providing users access to the shopping
 * cart and listing page features.
 *
 * @author Christina Joslin and Sveni Thalor, lab sec 4427
 * @version 4/27/2023
 *
 */
public class CustomerPageClient {
    private String searchTerm;
    private static BufferedReader reader;
    private static PrintWriter writer;


    public static void runClient(Buyer buyer) {
        try {
            Socket socket = new Socket("localhost", 1233);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            int repeat = 1;

            do {
                CustomerPageClient C = new CustomerPageClient();  //creates a CustomerPage object to be used for processing
                String bikeNames = reader.readLine();
                String[] bikeNamesArray = bikeNames.substring(1, bikeNames.length() - 1).split(",");
                int choice = C.displayMainMenu(writer, reader);
                writer.println(choice);
                writer.flush();

                switch (choice) {
                    case -1:
                        repeat = 0;
                        break;
                    case 1: // main menu option 1: display bikes
                        int repeat1 = 1;
                        int choice2 = -5;
                        do {
                            int choice1;
                            if (choice2 != -5) {
                                choice1 = choice2;
                                choice2 = -5;
                            } else {
                                choice1 = C.displayBikesMenu(bikeNames);
                            }
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
                                        writer.write("false");
                                        writer.println();
                                        writer.flush();
                                        break;
                                    } else { //takes the buyer to the shopping cart to add a bike
                                        writer.write("true");
                                        writer.println();
                                        writer.flush();
                                        CustomerPageClient c = new CustomerPageClient();
                                        c.addBike(writer,reader,"fromDescription");
                                        break;
                                    }
                                case -2, -3: // sorting bikes
                                    String sortedBikeInfo = reader.readLine();
                                    choice2 = C.displayBikesMenu(sortedBikeInfo);
                                    if (choice2 != -2 && choice2 != -3 && choice2 != -1 && choice2 != -4) {
                                        String[] sortedBikesArray = sortedBikeInfo.substring(1, sortedBikeInfo.length() - 1).split(",");
                                        String sortedChoice = sortedBikesArray[choice2].strip();
                                        for (int i = 0; i < bikeNamesArray.length; i++) {
                                            if (sortedChoice.equals(bikeNamesArray[i].strip())) {
                                                choice2 = i;
                                                break;
                                            }
                                        }
                                    }
                                    break;
                                case -1: // go back
                                    repeat1 = 0;
                                    break;
                                case -4: // search
                                    writer.println(C.searchTerm);
                                    writer.flush();
                                    String result = reader.readLine();
                                    if (!result.equals("-1")) {
                                        choice2 = C.displayBikesMenu(result);
                                        if (choice2 != -2 && choice2 != -3 && choice2 != -1 && choice2 != -4) {
                                            String[] matchesArray = result.substring(1, result.length() - 1).split(",");
                                            String matchChoice = matchesArray[choice2].strip();
                                            for (int i = 0; i < bikeNamesArray.length; i++) {
                                                if (matchChoice.equals(bikeNamesArray[i].strip())) {
                                                    choice2 = i;
                                                    break;
                                                }
                                            }
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(null, "No matches found!");
                                    }
                                    break;
                            }
                        } while (repeat1 == 1);
                        break;
                    case 2: //option 2: view cart
                        String message = "";
                        do {
                            message = C.displayShoppingCartMenu();
                            if (message.equals("exit")) {
                                return;
                            }
                        } while (!message.equals("backHome"));

                        break;
                    case 3: // option 3: view purchase history
                        JFileChooser j = new JFileChooser();
                        j.setDialogTitle("Choose a file to save purchase history to");
                        int save = j.showSaveDialog(null);
                        File file = null;
                        if (save == JFileChooser.APPROVE_OPTION) {
                            file = j.getSelectedFile();
                        } else {
                            JOptionPane.showMessageDialog(null, "An error occurred, try again!");
                            break;
                        }
                        String path = file.getPath();
                        writer.println(path);
                        writer.flush();
                        writer.println(buyer.getUsername());
                        writer.flush();
                        writer.println(buyer.getUsername());
                        writer.flush();
                        String success = reader.readLine();
                        if (success.equals("true")) {
                            JOptionPane.showMessageDialog(null, "Success!");
                        } else if (success.equals("false")) {
                            JOptionPane.showMessageDialog(null, "An error occurred, try again!");
                        }
                        break;
                    case 4: // option 4: logout  //TODO need to put in the GUI for this
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
            JOptionPane.showMessageDialog(null, "Thank you for visiting Boilermaker Bikes!",
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
        JComboBox dropdown = new JComboBox(new String[]{"1. View all available bikes", "2. Review cart",
                "3. Get purchase history", "4. Logout", "5. Delete account"});
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

    public int displayBikesMenu(String bikeNames) {

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


    /*******
     * The following methods are used to display the shopping cart and allow the user to add,remove, or checkout items
     * @author Christina Joslin
     *
     */

    /*******
     * The UPDATED shopping cart menu using Simple GUI; need to implement this
     * @return
     */
    public String displayShoppingCartMenu() {
        ArrayList<PurchasedBike> shoppingCartTemp = CustomerPageServer.thisBuyer.getShoppingCart();

        String[] bikeNames = new String[shoppingCartTemp.size()];
        int i = 0;
        for (PurchasedBike pb : shoppingCartTemp) {
            bikeNames[i] = pb.shoppingCartToString();
            i++;
        }
        // main menu option 1: display bikes
        String[] buttons = {"Add Item", "Delete Item", "Checkout", "Back To Home"};
        JPanel panel = new JPanel();
        JList dropdown = new JList(bikeNames);
        String message = "";

        panel.add(dropdown);
        int option = JOptionPane.showOptionDialog(null, panel, "Boilermaker Bikes",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, buttons, null);

        CustomerPageClient c = new CustomerPageClient();

        switch (option) {
            case 0: // Add Item
                writer.write("add");
                writer.println();
                writer.flush();
                c.addBike(writer, reader,"fromCart");
                message = "add";
                break;
            case 1: // Delete Item
                writer.write("delete");
                writer.println();
                writer.flush();
                c.removeBike(writer,reader);
                message = "delete";
                break;

            case 2: // Checkout
                writer.write("checkout");
                writer.println();
                writer.flush();
                c.checkOutBikes(reader);
                message = "checkout";
                break;

            case 3: // Back To Home
                writer.write("backHome");
                writer.println();
                writer.flush();
                message = "backHome";
                break;
            default:
                message = "exit";
                break;
        }
        return message;
    }

    /********
     * Allows the buyer to checkout all of the bikes in their current shopping cart and move them to their purchase
     * history by first checking if the bikes in their cart are still available. If the bikes are not available, then
     * the checkout is not performed and an error message is shown. If a checkout is successfully performed then a
     * successful checkout message will be shown.
     * @param reader readers the input from the server regarding whether the checkout successfully took place
     * @author Christina Joslin
     */
    public void checkOutBikes(BufferedReader reader) {
        boolean stillAvailable; //keeps track of whether or not the items can be checked out or not
        do {
            try {
                stillAvailable = Boolean.parseBoolean(reader.readLine());
            } catch (Exception e) {
                //System.out.println("Error under checkoutBikes");
                return;
            }
            if (stillAvailable) {
                break;
            }
            int choice = JOptionPane.showConfirmDialog(null, "Error. One or more bikes in your cart are " +
                    "unavailable.", "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            if (choice == JOptionPane.CLOSED_OPTION || choice == JOptionPane.CANCEL_OPTION || choice == JOptionPane.OK_OPTION) {
                return;
            }
        } while (!stillAvailable);
        boolean success; //keeps track of if the user has successfully
        try {
            success = Boolean.parseBoolean(reader.readLine());
            System.out.println("Successful checkout? " + success);
            if (success) {
                JOptionPane.showMessageDialog(null, "Successful Checkout!",
                        "Boilermaker Bikes", JOptionPane.INFORMATION_MESSAGE);

            }
        } catch (Exception e) {
            System.out.println("Error message under successfully completing the shopping cart.");
        }


    }

    /*******
     * This method allows the user to add a bike to their shopping cart via the add bike button
     * @param writer writes the user input to the server
     * @param reader reads either true or false from the server indicating it the process was a success or not
     * @param buttonType if the user is adding a bike from the shoppingCart, then a dropdown menu is first displayed to
     * select a specific bike. If the user is adding a bike from its description then it will simply ask for the quantity to be added
     * @author Christina Joslin
     */
    public void addBike(PrintWriter writer, BufferedReader reader,String buttonType) {
        do {
            int bikeId = -1; //keeps track of the 4 digit bike id entered by the user
            if (buttonType.equals("fromCart")) {
                String[] listingPageOptions = new String[UserInfo.getBikes().size()];
                int i = 0;

                /********
                 * Iterates through the available bikes in the shopping cart database and displays them to the user
                 */
                for (Bike b : UserInfo.getBikes()) {
                    listingPageOptions[i] = b.toNiceString();
                    i++;
                }
                String bikeMessage = (String) JOptionPane.showInputDialog(null, "Choose Bike to Add", "Boilermaker Bikes",
                        JOptionPane.PLAIN_MESSAGE, null, listingPageOptions, listingPageOptions[0]);
                System.out.println(bikeMessage);
                //if the user does not choose an option then set the bike message to null
                if (bikeMessage == null || bikeMessage.isEmpty()) {
                    System.out.println("Exit Button");
                    return;
                }

                /********
                 * Retrieves the corresponding bikeID
                 */
                for (Bike b : UserInfo.getBikes()) {
                    if (b.toNiceString().equals(bikeMessage)) {
                        bikeId = b.getId();
                        writer.write(bikeId + "");
                        writer.println();
                        writer.flush();
                        break;
                    }
                }
            } else if (buttonType.equals("fromDescription")) {
                //receives the corresponding bike id of the bike description chosen by the buyer
                try {
                    bikeId = Integer.parseInt(reader.readLine());

                    //sends the bikeId to the server for processing
                    writer.write(bikeId + "");
                    writer.println();
                    writer.flush();

                } catch (Exception e) {
                    System.out.println(bikeId + " cannot be parsed");
                    return;
                }

            }

            /************
             * Checks if the bikeID is already in the shopping cart. If it is already there, then just have the buyer add
             * on to the existing quantity
             */
            boolean inCart;
            String input = "";
            try {
                input = reader.readLine();
                //System.out.println("Client input received");
                inCart = Boolean.parseBoolean(input);
                System.out.println("inCart is " + inCart);
                //System.out.println("Client side it is" + inCart + "");

            } catch (Exception e) {
                System.out.println("error when trying to find out if already in shopping cart");
                return;
            }

            if (inCart) {
                //checks if the bike quantity is valid
                String quantity = "";
                boolean validQuantity;
                do {
                    quantity = JOptionPane.showInputDialog(null, "This bike is already in your " +
                                    "shopping cart. Enter bike quantity to add: ",
                            "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);
                    System.out.println("Entered quantity: " + quantity);
                    writer.write(quantity);
                    writer.println();
                    writer.flush();
                    quantity = ""; //clears the quantity for the next iteration

                    try {
                        validQuantity = Boolean.parseBoolean(reader.readLine());
                        System.out.println("Valid Quantity Client? " + validQuantity);
                        if (validQuantity) {
                            break;
                        }

                    } catch (Exception e) {
                        System.out.println("Error invalid quantity in AddBike");
                        return;
                    }

                    int error = JOptionPane.showConfirmDialog(null, "Error. Invalid quantity " +
                                    "please try again.", "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE);
                    if (error == JOptionPane.CLOSED_OPTION || error == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                } while (true);


            } else if (!inCart) {
                //checks if the bike quantity is valid
                String quantity = "";
                boolean validQuantity;

                do {
                    quantity = JOptionPane.showInputDialog(null, "Enter bike quantity: ",
                            "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);
                    System.out.println("quantity entered " + quantity);
                    writer.write(quantity);
                    writer.println();
                    writer.flush();
                    try {
                        validQuantity = Boolean.parseBoolean(reader.readLine());
                        System.out.println("Valid Quantity? " + validQuantity);
                        if (validQuantity) {
                            break;
                        }

                    } catch (Exception e) {
                        System.out.println("Error under bike quantity in AddBike");
                        break;
                    }

                    int choice = JOptionPane.showConfirmDialog(null, "Invalid bike quantity. Please try again.", "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    //allows the user to exit at any point using exit button

                    if (choice == JOptionPane.CLOSED_OPTION) {
                        break;
                    }

                } while (true);
                //asks the user if they would like bike insurance added to their total
                int x = JOptionPane.showConfirmDialog(null, "Would you like one-time $50 bike in a tree " +
                                "insurance?",
                        "Boilermaker Bikes",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (x == JOptionPane.YES_OPTION) {
                    writer.write("true");
                    writer.println();
                    writer.flush();
                } else if (x == JOptionPane.NO_OPTION) {
                    writer.write("false");
                    writer.println();
                    writer.flush();
                } else {
                    return;
                }
            }
            boolean success;
            try {
                success = Boolean.parseBoolean(reader.readLine());
            } catch (Exception e) {
                System.out.println("Error under adding a bike in AddBike");
                return;
            }
            if (success) {
                String[] options = {"OK"};
                JOptionPane.showOptionDialog(null, "Bike successfully added!",
                        "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                break;
            }

        } while (true);

    }

    /*********
     * This method removes bikes from the shopping cart
     * @param writer sends the specific bike ID to be removed to the user
     * @param reader indicates whether the bike has been successfully removed from the shopping cart
     */
    //TODO need to allow the user to choose what quantity they want removed
    public void removeBike(PrintWriter writer,BufferedReader reader) {
        int bikeId = -1; //keeps track of the 4 digit bike id entered by the user

        //creates a dropdown menu of items that the user can remove from their shopping cart
        String[] shoppingCartOptions = new String[CustomerPageServer.thisBuyer.getShoppingCart().size()];
        int i = 0;

        /********
         * Iterates through the available bikes in the shopping cart database and displays them to the user
         */
        for (PurchasedBike b : CustomerPageServer.thisBuyer.getShoppingCart()) {
            shoppingCartOptions[i] = b.shoppingCartToString();
            i++;
        }
        String bikeMessage = (String) JOptionPane.showInputDialog(null, "Choose an Item to Remove", "Boilermaker Bikes",
                JOptionPane.PLAIN_MESSAGE, null, shoppingCartOptions, shoppingCartOptions[0]);
        System.out.println("Bike message" + bikeMessage);
        //if the user does not choose an option then set the bike message to null
        if (bikeMessage == null || bikeMessage.isEmpty()) {
            System.out.println("Exit Button");
            return;
        }
        /********
         * Retrieves the corresponding bikeID
         */
        for (PurchasedBike b : CustomerPageServer.thisBuyer.getShoppingCart()) {
            if (b.shoppingCartToString().equals(bikeMessage)) {
                bikeId = b.getId();
                System.out.println("found bikeID " + bikeId);
                writer.write(bikeId + "");
                writer.println();
                writer.flush();
                break;
            }
        }
        /******
         * Prints a success message when the bike is removed from the cart
         */
        try {
            boolean success = Boolean.parseBoolean(reader.readLine());
            if (success) {
                int x = JOptionPane.showConfirmDialog(null,"Successfully deleted!",
                        "Boilermaker Bikes",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE);

            }

        } catch (Exception e) {
            System.out.println("Error under success in removeBike");
        }

    }

}
