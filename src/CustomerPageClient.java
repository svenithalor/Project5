import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

/*******
 * This class..
 * @author Sveni Thalor and Christina Joslin
 *
 */
public class CustomerPageClient extends JComponent implements Runnable {
    private String searchTerm;
    private static BufferedReader reader;
    private static PrintWriter writer;

    //Shopping Cart Fields
    private JButton addItemButton; //allows the user to add a bike to their shopping cart
    private JButton deleteItemButton; //allows the user to delete a bike from their shopping cart
    private JButton checkoutButton; //allows the user to checkout all of the items in their shopping cart
    private JButton returnToHomeButton; //allows the user to return back to hte main menu
    private JButton refreshButton; //allows the user to refresh their screen and see any updates made by other users
    private Container content; //where the shopping cart items will be displayed
    private JFrame frame; //the content page for the shopping cart
    static JTable table; //displays the contents of the shopping cart


    public static void runClient(Buyer buyer) {
        try {
            //System.out.println("Run client");
            Socket socket = new Socket("localhost", 1234);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());

            int repeat = 1;
            CustomerPageClient C = new CustomerPageClient();  //creates a CustomerPage object to be used for processing
            do {
                String bikeNames = reader.readLine();
                int choice = C.displayMainMenu(writer, reader);
                writer.println(choice);
                writer.flush();

                switch (choice) {
                    case -1:
                        repeat = 0;
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
                                    } else { //takes the buyer to the shopping cart to add a bike
                                        //TODO

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
                            }
                        } while (repeat1 == 1);
                        break;
                    case 2: // option 2: view cart
                        SwingUtilities.invokeLater(new CustomerPageClient());
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
            JOptionPane.getRootFrame().dispose();
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

    /*******
     * The following methods are used to display the shopping cart and allow the user to add,remove, or checkout items
     * @author Christina Joslin
     *
     */


    /*******
     * This method runs the complex GUI for the shopping cart page and the buttons add bike,remove bike,checkout,
     * return to home, and refresh
     * @author Christina Joslin
     */
    @Override
    public void run() {
        //Creates the JFrame
        frame = new JFrame("Boilermaker Bikes");
        content = frame.getContentPane();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        content.setLayout(new BorderLayout());

        //Configures the JFrame by setting up the size, location, close operation, and visibility
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);


        displayBikes(CustomerPageServer.thisBuyer, frame); //NOTE* temporary input
        //Creates the list of items in the buyer's shopping cart
        JLabel l = new JLabel("Shopping Cart");
        content.add(l, BorderLayout.NORTH);

        //Creates the buttons for the shopping cart page and adds them to the frame
        JPanel panelBottom = new JPanel();

        addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(actionListener);

        deleteItemButton = new JButton("Delete Item");
        deleteItemButton.addActionListener(actionListener);

        checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(actionListener);

        returnToHomeButton = new JButton("Return to Home");
        returnToHomeButton.addActionListener(actionListener);

        refreshButton = new JButton("Refresh Screen");
        refreshButton.addActionListener(actionListener);

        //adds the buttons to the bottom of the frame
        panelBottom.add(addItemButton);
        panelBottom.add(deleteItemButton);
        panelBottom.add(checkoutButton);
        panelBottom.add(returnToHomeButton);
        panelBottom.add(refreshButton);
        content.add(panelBottom, BorderLayout.SOUTH);


    }

    ActionListener actionListener = new ActionListener() {
        //creates a ShoppingCartClient object to navigate the buttons
        @Override
        public void actionPerformed(ActionEvent e) {

            //creates a shopping cart client object to navigate to each method
            CustomerPageClient c = new CustomerPageClient();

            if (e.getSource() == addItemButton) { //TODO need to fix this will work on tomorrow
                writer.write("add");
                writer.println();
                writer.flush();
                c.addBike(writer, reader);
            }
            if (e.getSource() == deleteItemButton) { //TODO not complete yet
                writer.write("delete");
                writer.println();
                writer.flush();
            }
            if (e.getSource() == checkoutButton) { //TODO need to fix this will work on tomorrow
                writer.write("checkout");
                writer.println();
                writer.flush();
                System.out.println("checkout");
                c.checkOutBikes(reader);
                //tells the server that the user wants to check out
                //do something
                //remove elements from the shopping cart and putting it in the purchase history
                //also removing those elements from the bikes
            }
            if (e.getSource() == returnToHomeButton) {
                writer.write("backHome");
                writer.println();
                writer.flush();
                frame.setVisible(false);
                return;

            }
            if (e.getSource() == refreshButton) { //TODO need to fix this
                writer.write("refresh");
                writer.println();
                writer.flush();
                frame.repaint();  //at the moment repaint() is not working
            }

        }

    };

    /*******
     * This method displays the bikes in a certain buyer's shopping cart using a JTable
     * @param b the buyer who wants to view their shopping cart
     * @param content the container of bikes the user wants to display
     * @author Christina Joslin
     */
    public static void displayBikes(Buyer b, Container content) {

        String[] columnNames = {"Bike ID", "Model Name", "Price", "Quantity"};
        JTable table = new JTable(b.shoppingCartInfo(), columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        //prevents the user from being able to edit the table of values
        table.setDefaultEditor(Object.class, null);

        //makes the table names visible
        content.add(scrollPane);

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
                System.out.println("Error under checkoutBikes");
                return;
            }
            if (stillAvailable) {
                break;
            }
            int choice = JOptionPane.showConfirmDialog(null, "Error. One or more bikes are " +
                    "unavailable.", "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            if (choice == JOptionPane.CLOSED_OPTION || choice == JOptionPane.CANCEL_OPTION) {
                return;
            }
        } while (!stillAvailable);
        boolean success = false; //keeps track of if the user has successfully
        try {
            success = Boolean.parseBoolean(reader.readLine());
        } catch (Exception e) {
            System.out.println("Error message under successfully completing the shopping cart.");
        }
        if (success) {
            JOptionPane.showMessageDialog(null, "Successful Checkout!", "Boilermaker Bikes", JOptionPane.INFORMATION_MESSAGE);
        }
        for (PurchasedBike pb:CustomerPageServer.thisBuyer.getShoppingCart()) {
            System.out.println(pb.toNiceString());
        }

    }

    /*******
     * This method allows the user to add a bike to their shopping cart via the add bike button
     * @param writer writes the user input to the server
     * @param reader reads either true or false from the server indicating it the process was a success or not
     * @author Christina Joslin
     */
    public void addBike(PrintWriter writer, BufferedReader reader) {
        do {
            int bikeId = -1; //keeps track of the 4 digit bike id entered by the user
            boolean validId = false; //confirms that the user has enterred a validBikeId
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

            //if the user does not choose an option then set the bike message to null
            if (bikeMessage.isEmpty() || bikeMessage == null) {
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


            /************
             * Checks if the bikeID is already in the shopping cart. If it is already there, then just have the buyer add
             * on to the existing quantity
             */
            boolean inCart;
            String input = "";
            try {
                if (reader.ready()) {
                    input = reader.readLine();
                    JOptionPane.getRootFrame().dispose();
                }
                //System.out.println("Client input received");
                inCart = Boolean.parseBoolean(input);
                //System.out.println("Client side it is" + inCart + "");

            } catch (Exception e) {
                System.out.println("error when trying to find out if already in shopping cart");
                return;
            }
            if (inCart == true || inCart == false) {
                JOptionPane.getRootFrame().dispose();
            }

            if (inCart) {
                //checks if the bike quantity is valid
                String quantity = "";
                boolean validQuantity = false;
                do {
                    System.out.println("Hello world");
                    JOptionPane.getRootFrame().dispose();
                    quantity = JOptionPane.showInputDialog(null, "This bike is already in your " +
                                    "shopping cart. Enter bike quantity to add: ",
                            "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);

                    writer.write(quantity);
                    writer.println();
                    writer.flush();
                    try {
                        validQuantity = Boolean.parseBoolean(reader.readLine());
                        //System.out.println(validQuantity);
                    } catch (Exception e) {
                        System.out.println("Error invalid quantity in AddBike");
                        return;
                    }

                    if (validQuantity) {
                        break;
                    }
                    int error = JOptionPane.showConfirmDialog(null, "Error. Invalid quantity " +
                                    "please try again.", "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.ERROR_MESSAGE);
                    if (error == JOptionPane.CLOSED_OPTION || error == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                } while (!validQuantity);


            } else if (!inCart) {
                //checks if the bike quantity is valid
                String quantity = "";
                boolean validQuantity = false;

                do {
                    quantity = JOptionPane.showInputDialog(null, "Enter bike quantity: ",
                            "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);
                    writer.write(quantity);
                    writer.println();
                    writer.flush();
                    try {
                        validQuantity = Boolean.parseBoolean(reader.readLine());
                    } catch (Exception e) {
                        System.out.println("Error under bike quantity in AddBike");
                        break;
                    }
                    if (validQuantity) {
                        break;
                    }
                    int choice = JOptionPane.showConfirmDialog(null, "Invalid bike quantity. Please try again.", "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    //allows the user to exit at any point using exit button

                    if (choice == JOptionPane.CLOSED_OPTION) {
                        break;
                    }

                } while (!validQuantity);

                //asks the user if they would like bike insurance added to their total
                int x = JOptionPane.showConfirmDialog(null, "Enter bike insurance ",
                        "Boilermaker Bikes",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

                if (x == JOptionPane.YES_OPTION) {
                    writer.write("yes");
                    writer.println();
                    writer.flush();
                } else if (x == JOptionPane.NO_OPTION) {
                    writer.write("no");
                    writer.println();
                    writer.flush();
                } else {
                    return;
                }
            }
            boolean success = false;
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


}
