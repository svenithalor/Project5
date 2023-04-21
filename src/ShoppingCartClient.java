import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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
        frame.setSize(700,500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        //creates the shopping cart
        UserInfo.readUsers(); //temporarily value to read in buyers and sellers

        ShoppingCartClient.displayBikes(UserInfo.getBuyers().get(0),frame);
        //Creates the list of items in the buyer's shopping cart
        JLabel l = new JLabel("Shopping Cart");
        content.add(l,BorderLayout.NORTH);
        //items = new JList(ShoppingCartClient.displayBikes(UserInfo.getBuyers().get(0)));
        //content.add(items);

        //Creates the buttons for the shopping cart page and adds them to the frame
        JPanel panelBottom = new JPanel();

        JButton addItemButton = new JButton("Add Item");
        addItemButton.addActionListener(actionListener);

        JButton deleteItemButton = new JButton("Delete Item");
        deleteItemButton.addActionListener(actionListener);

        JButton checkoutButton = new JButton("Checkout");
        checkoutButton.addActionListener(actionListener);

        JButton returnToHomeButton = new JButton("Return to Home");
        returnToHomeButton.addActionListener(actionListener);

        JButton refreshButton = new JButton("Refresh Screen");
        refreshButton.addActionListener(actionListener);

        //adds the buttons to the bottom of the frame
        panelBottom.add(addItemButton);
        panelBottom.add(deleteItemButton);
        panelBottom.add(checkoutButton);
        panelBottom.add(returnToHomeButton);
        panelBottom.add(refreshButton);
        content.add(panelBottom,BorderLayout.SOUTH);


    }

    ActionListener actionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == addItemButton) {
                //do something

            }
            if (e.getSource() == deleteItemButton) {

                //do something
            }
            if (e.getSource() == checkoutButton) {
                //do something
            }
            if (e.getSource() == returnToHomeButton) {
                //do something
            }
            if (e.getSource() == refreshButton) {

                //do something (concurrency element
            }

        }

    };

    /*******
     * This method displays the bikes in a certain buyer's shopping cart using a JTable
     * @param b the buyer who wants to view their shopping cart
     */
    public static void displayBikes (Buyer b,Container content) {
        String[] columnNames = {"Bike ID","Model Name","Price","Quantity"};
        JTable table = new JTable(b.shoppingCartInfo(),columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        //prevents the user from being able to edit the table of values
        table.setDefaultEditor(Object.class,null);

        //makes the table names visible
        content.add(scrollPane);

    }


}
