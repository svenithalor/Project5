import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class SellerPageClient {
    // fields
    private String name;
    private ArrayList<Bike> inventory;


    public static Bike parseBike(String newBike) {
        
        String[] sNB = newBike.split(",");
        int wS = Integer.valueOf(sNB[1]);
        double p = Double.valueOf(sNB[2]);
        boolean u = Boolean.parseBoolean(sNB[4]);
        int q = Integer.valueOf(sNB[7]);
        int tempID = Integer.valueOf(sNB[8]);

        Bike bike = new Bike(sNB[0], wS, p, sNB[3], u, sNB[5], sNB[6], q, tempID);
    }

    /******
     * Method to send arrayList from client to server or vice versa.
     */
    public static String sendArrayList(ArrayList<Bike> bike) {
        StringBuilder sb = new StringBuilder();
        for (Bike b : bikes) {
            sb.append(b.toString() + "]");
        }

        if (sb.length() != 0) {
            sb.deleteCharAt(sb.length() - 1);
        }

        String vtr = sb.toString();
        return vtr;
    }

    /********
     * Method to recieve arrayList from other side
     */
    public ArrayList<Bike> recieveArrayList(String s) {
        String[] bikes = s.split(']');
        ArrayList<Bike> vtr = new ArrayList<>();

        for (String bike : bikes) {
            vtr.add(parseBike(bike));
        }

        return vtr;
    }

    // The above 3 methods are the same as the SellerPageServer.
    // Both classes will need them because they are sending and recieving data back and forth.
    public SellerPageClient(String name, ArrayList<Bike> inventory) {
        this.name = name;
        this.inventory = inventory;
    }

    
    
    public void runSellerPageClient(Seller seller) {
        try {
            SellerPageClient C = new SellerPageClient(seller.getUsername(), seller.getInventory()); //creates an object to be used to navigate the menu
            Socket socket = new Socket("localhost", 4242);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            writer.write(seller.getUsername());
            writer.println();
            writer.flush(); // sends the seller name
            do {
                int o = C.displayMainMenu(writer, reader); // writer and reader have already been created


            } while (o != 8);

            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            // JOption pane for something went wrong
        }
        
        
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
