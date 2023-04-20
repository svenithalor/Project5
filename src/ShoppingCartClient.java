import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ShoppingCartClient extends JComponent implements Runnable {
    JButton addItemButton; //allows the user to add a bike to their shopping cart
    JButton deleteItemButton; //allows the user to delete a bike from their shopping cart
    JButton checkoutButton; //allows the user to checkout all of the items in their shopping cart
    JButton returnToHomeButton; //allows the user to return back to hte main menu
    JList items; //allows the user to see the items in their cart
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

        //creates the
        JList item
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

        //adds the buttons to the bottom of the frame
        panelBottom.add(addItemButton);
        panelBottom.add(deleteItemButton);
        panelBottom.add(checkoutButton);
        panelBottom.add(returnToHomeButton);
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

        }

    };


}
