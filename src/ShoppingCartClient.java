import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*************
 * This method displays and updates the contents of a buyer's shopping cart. It also allows the buyer to add
 * items to their shopping cart and checkout all the items in their shopping cart.
 *
 * @author Christina Joslin and Duoli Chen, lab sec 4427
 * @version 4/24/2023
 */
public class ShoppingCartClient extends JComponent implements Runnable {
    JButton addItemButton; //allows the user to add a bike to their shopping cart
    JButton deleteItemButton; //allows the user to delete a bike from their shopping cart
    JButton checkoutButton; //allows the user to checkout all of the items in their shopping cart
    JButton returnToHomeButton; //allows the user to return back to hte main menu
    JButton refreshButton; //allows the user to refresh their screen and see any updates made by other users
    Container content; //where the shopping cart items will be displayed
    JFrame frame; //the content page for the shopping cart
    static JTable table;
    Socket socket;
    BufferedReader reader;
    PrintWriter writer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ShoppingCartClient());

    }

    @Override
    public void run() {

        //Creates the JFrame
        frame = new JFrame("Boilermaker Bikes");
        content = frame.getContentPane();
        content.setLayout(new BorderLayout());

        //Configures the JFrame by setting up the size, location, close operation, and visibility
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        UserInfo.readUsers(); //NOTE* temporary value to read in buyers and sellers


        ShoppingCartClient.displayBikes(UserInfo.getBuyers().get(0), frame); //NOTE* temporary input
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

        try {
            socket = new Socket("localhost", 4242);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    ActionListener actionListener = new ActionListener() {
        //creates a ShoppingCartClient object to navigate the buttons
        @Override
        public void actionPerformed(ActionEvent e) {

            //creates an shopping cart client object to navigate to each method
            ShoppingCartClient c = new ShoppingCartClient();

            if (e.getSource() == addItemButton) {
                writer.write("add");
                writer.println();
                writer.flush();
                c.addBike(writer, reader);
            }
            if (e.getSource() == deleteItemButton) {
                writer.write("delete");
                writer.println();
                writer.flush();
            }
            if (e.getSource() == checkoutButton) {
                System.out.println("checkout");
                writer.write("checkout");
                writer.println();
                writer.flush();
                //tells the server that the user wants to check out
                //do something
                //remove elements from the shopping cart and putting it in the purchase history
                //also removing those elements from the bikes
            }
            if (e.getSource() == returnToHomeButton) {
                writer.write("backHome");
                writer.println();
                writer.flush();
                //tells the server that the user wants to return home

                //do something
            }
            if (e.getSource() == refreshButton) {
                //refreshes the page for the user
                writer.write("refresh");
                writer.println();
                writer.flush();
                frame.setVisible(false);
                frame.setVisible(true);
            }

        }

    };

    /*******
     * This method displays the bikes in a certain buyer's shopping cart using a JTable
     * @param b the buyer who wants to view their shopping cart
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
     * Need to fill this in
     * @param writer
     * @param reader
     */
    public void checkOutBikes(PrintWriter writer, BufferedReader reader) {
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
            int choice = JOptionPane.showConfirmDialog(null,"Error. One or more bikes are " +
                    "unavailable.","Boilermaker Bikes",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(null,"Successful Checkout!","Boilermaker Bikes",JOptionPane.INFORMATION_MESSAGE);
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
            String bikeId = ""; //keeps track of the 4 digit bike id entered by the user
            boolean validId = false; //confirms that the user has enterred a validBikeId

            /********
             * Checks if the bike ID is valid
             */
            do {
                bikeId = JOptionPane.showInputDialog(null, "Enter bike ID: ",
                        "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);
                //sends the bike ID to the server and confirms that is a valid input
                writer.write(bikeId);
                writer.println();
                writer.flush();
                try {
                    validId = Boolean.parseBoolean(reader.readLine());
                } catch (IOException e) {
                    System.out.println("Error under bike ID in AddBike"); //TEMP value
                    break;
                }

                if (validId) {
                    break;
                }
                int choice = JOptionPane.showConfirmDialog(null, "Invalid Bike ID. Please try again.", "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                //allows the user to exit at any point using exit button

                if (choice == JOptionPane.CLOSED_OPTION) {
                    break;
                }

            } while (!validId);

            /************
             * Checks if the bikeID is already in the shopping cart. If it is already there, then just have the buyer add
             * on to the existing quantity
             */
            boolean inCart;
            try {
                inCart = Boolean.parseBoolean(reader.readLine());

            } catch (Exception e) {
                System.out.println("error when trying to find out if already in shopping cart");
                return;
            }

            if (inCart) {
                //checks if the bike quantity is valid
                String quantity = "";
                boolean validQuantity = false;
                do {
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

    private void deleteCart() {
        String bikeId = JOptionPane.showInputDialog(null, "Enter bike ID: ",
                "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);

        String tempStr = "delete," + bikeId;
        writer.write(tempStr);
        writer.println();
        writer.flush();

        String valid = null;
        try {
            valid = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (valid.equals("false")) {
            JOptionPane.showMessageDialog(null, "Invalid Input. Please try again.",
                    "Boilermaker Bikes", JOptionPane.ERROR_MESSAGE);
        }
    }


}
