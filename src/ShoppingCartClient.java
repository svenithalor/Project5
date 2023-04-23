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
 * @author Christina Joslin, lab sec 4427
 * @version 4/20/2023
 */
public class ShoppingCartClient extends JComponent implements Runnable {
    JButton addItemButton; //allows the user to add a bike to their shopping cart
    JButton deleteItemButton; //allows the user to delete a bike from their shopping cart
    JButton checkoutButton; //allows the user to checkout all of the items in their shopping cart
    JButton returnToHomeButton; //allows the user to return back to hte main menu
    JButton refreshButton; //allows the user to refresh their screen and see any updates made by other users
    Container content; //where the shopping cart items will be displayed

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ShoppingCartClient());


    }

    @Override
    public void run() {

        //Creates the JFrame
        JFrame frame = new JFrame("Boilermaker Bikes");
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


    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                Socket socket = new Socket("localhost", 4242);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                UserInfo.readUsers();

                /***********
                 * Add Item button: Insert description here...
                 */
                if (e.getSource() == addItemButton) {
                    String bikeId = ""; //stores the bikeId entered by the user
                    System.out.println("add");
                    writer.write("add");
                    writer.println();
                    writer.flush();
                    //checks if the user entered bike ID is valid
                    do {
                        bikeId = JOptionPane.showInputDialog(null, "Enter bike ID: ", "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);
                        //sends the bike id to the server
                        writer.write(bikeId);
                        writer.println();
                        writer.flush();
                        //confirms that the user input is correct
                        String valid = reader.readLine();
                        System.out.println(valid);
                        if (valid.equals("true")) {
                            break;
                        }
                        JOptionPane.showMessageDialog(null, "Bike ID Not found. Please try again.",
                                "Boilermaker Bikes", JOptionPane.ERROR_MESSAGE);

                    } while (true);


                }
                /******
                 * Delete Item Button: add description here...
                 */
                if (e.getSource() == deleteItemButton) {
                    //tells the server that the user wants to delete an item
                    writer.write("delete");
                    writer.println();
                    writer.flush();
                    //checks if the user entered bike ID is valid
                    do {
                        String bikeId = JOptionPane.showInputDialog(null, "Enter bike ID: ", "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);
                        //sends the bike id to the server
                        writer.write(bikeId);
                        writer.println();
                        writer.flush();
                        //confirms that the user input is correct
                        String valid = reader.readLine();
                        System.out.println(valid);
                        if (valid.equals("true")) {
                            break;
                        }
                        JOptionPane.showMessageDialog(null, "Invalid Input. Please try again.", "Boilermaker Bikes", JOptionPane.ERROR_MESSAGE);

                    } while (true);


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
                    writer.write("refresh");
                    writer.println();
                    writer.flush();

                    //tells the server that the user needs to refresh their screen
                    //do something
                }


            } catch (IOException ex) {
                ex.printStackTrace();
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

    public void addBike() {
        //ask for the bike ID they want to remove and we'll have to check if that ID is still available
        //ask if they want it insured or not (JOptionPane)
        //refresh the page so they can see the item they added

    }


}