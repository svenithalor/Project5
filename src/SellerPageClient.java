import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class SellerPageClient {
    //TODO

    public static void main(String[] args) {
        SellerPageClient C = new SellerPageClient(); //creates an object to be used to navigate the menu
        do {
            //C.displayMainMenu(PrintWriter writer, BufferedReader reader);


        } while (true);
    }
    /******
     * This method displays the seller page menu to the user and returns the menu item that they selected
     * @param writer to write the choice made by the seller to the server
     * @param reader to close the BufferedReader in the event that the seller exits out of the menu
     * @return the menu item selected by the user
     * @author Christina Joslin
     */
    public int displayMainMenu(PrintWriter writer, BufferedReader reader) throws IOException {
        //Creates a dropdown menu for the buyer to scroll through the menu options
        //Just an idea for how the dropdown can be implemented
        JPanel panel = new JPanel();
        JLabel label1 = new JLabel("Select an option: ");
        panel.add(label1);
        JComboBox dropdown = new JComboBox(new String[]{"1. View current bikes","2. Add new bike",
                "3. Remove Bike","4. Search Bike","5. Delete Account", "6. View Customer Shopping Carts",
                "7. View analytics","8. Exit"});

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
            JOptionPane.showMessageDialog(null,"Thank you for visiting Boilermaker Bikes!",
                    "Boilermaker Bikes", JOptionPane.INFORMATION_MESSAGE);
            writer.close();
            reader.close();
            return -1;
        }
    }
}
