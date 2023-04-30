import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.*;
import java.net.*;
import java.text.CompactNumberFormat;
import java.util.concurrent.CountDownLatch;


public class SellerPageClient {
    // fields
    private String name;
    private ArrayList<Bike> inventory;
    private int port;

    public SellerPageClient(String name, ArrayList<Bike> inventory,int port) {
        this.name = name;
        this.inventory = inventory;
        this.port = port;
    }
    public static Bike parseBike(String newBike) {
        
        String[] sNB = newBike.split(",");
        int wS = Integer.parseInt(sNB[1]);
        double p = Double.parseDouble(sNB[2]);
        boolean u = Boolean.parseBoolean(sNB[4]);
        int q = Integer.parseInt(sNB[7]);
        int tempID = Integer.parseInt(sNB[8]);

        Bike bike = new Bike(sNB[0], wS, p, sNB[3], u, sNB[5], sNB[6], q, tempID);

        return bike;
    }

    public static PurchasedBike parsePurchasedBike(String part) {
        //System.out.println(part);
        String[] bikeElements = part.split(",");
        String color = bikeElements[0];
        int wheelSize = Integer.parseInt(bikeElements[1]);
        double price = Double.parseDouble(bikeElements[2]);
        double finalPrice = Double.parseDouble(bikeElements[3]);
        String modelName = bikeElements[4];
        Boolean used = Boolean.parseBoolean(bikeElements[5]);
        String description = bikeElements[6];
        String sellerName = bikeElements[7];
        int quantity = Integer.parseInt(bikeElements[8]);
        boolean insured = Boolean.parseBoolean(bikeElements[9]);
        int id = Integer.parseInt(bikeElements[10]);
        PurchasedBike pb = new PurchasedBike(color, wheelSize, price, finalPrice, modelName, used, description, 
            sellerName, quantity, insured, id);

        return pb;
    }

    /******
     * Method to send arrayList from client to server or vice versa.
     */
    public static String sendArrayList(ArrayList<Bike> bike) {
        StringBuilder sb = new StringBuilder();
        for (Bike b : bike) {
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
        String[] bikes = s.split("]");
        ArrayList<Bike> vtr = new ArrayList<>();

        for (String bike : bikes) {
            vtr.add(parseBike(bike));
        }

        return vtr;
    }

    public static ArrayList<PurchasedBike> recievePurchasedBikes(String s) {
        //System.out.println(s);
        String[] bikes = s.split("]");
        ArrayList<PurchasedBike> vtr = new ArrayList<>();

        for (String bike : bikes) {
            vtr.add(parsePurchasedBike(bike));
        }

        return vtr;
    }

    // The above 3 methods are the same as the SellerPageServer.
    // Both classes will need them because they are sending and recieving data back and forth.
   
    // This method is only for testing seller page on its own
    public static void main(String[] args) {
        
        String bike1 = "blue,26,299.99,Firmstrong Urban,false,single-speed cruiser bike for easy relaxed riding,Bob,10,1078";
        String bike2 = "black,24,329.99,Hiland Road Bike Shimano,true,designed for people who like to ride or use it for daily commuting,Bob,20,1081";
        String bike3 = "red,25,329.99,Royce Union RMY,false,the durable lightweight aluminum frame is easy to handle and will never rust,BikesAreCool,2,1080";
        

        ArrayList<Bike> inv = new ArrayList<>();
        inv.add(parseBike(bike1));
        inv.add(parseBike(bike2));
        inv.add(parseBike(bike3));
        // inv.add(parseBike(bike4));
        // inv.add(parseBike(bike5));

        // ALL OF THE ABOVE STUFF IS TEMPORARY FOR TESTING PURPOSES

       // SellerPageClient spc = new SellerPageClient("test", inv);
       // spc.runSellerPageClient("test", inv);
    }
    
    public void runSellerPageClient(String rName, ArrayList<Bike> rInventory) {
        try {
            SellerPageClient C = new SellerPageClient(rName, rInventory,port); //creates an object to be used to navigate the menu
            Socket socket = new Socket("localhost", port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            writer.write(name);
            writer.println();
            writer.flush(); // sends the seller name

            String sendInventory = sendArrayList(inventory);

            writer.write(sendInventory);
            writer.println();
            writer.flush();

            int o = -1;
            do {
                o = C.displayMainMenu(writer, reader); // writer and reader have already been created
                
                // server processing is done here

                //String type = reader.readLine(); // gets back type of output

            } while (o != 8 && o != 5 && o != -1);

            String finalInventory = sendArrayList(inventory);

            //writer.write(finalInventory);
            //writer.println();
            //writer.flush();

            reader.close();
            writer.close();
            socket.close();
        } catch (Exception e) {
            // JOption pane for something went wrong
            e.printStackTrace();
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
        UserInfo.readUsers();
        JPanel panel = new JPanel();
        JLabel label1 = new JLabel("Select an option: ");
        panel.add(label1);
        JComboBox dropdown = new JComboBox(new String[]{"1. View current bikes","2. Add new bike",
                "3. Remove Bike","4. Search Bike","5. Delete Account", "6. View Customer Shopping Carts",
                "7. View analytics","8. Logout", "9. Export Inventory"});

        panel.add(dropdown);
        
        int confirmOption = JOptionPane.showConfirmDialog(null, panel, "Boilermaker Bikes",
                JOptionPane.OK_OPTION, JOptionPane.PLAIN_MESSAGE);
        int option = dropdown.getSelectedIndex() + 1;
        writer.write(Integer.toString(confirmOption));
        writer.println();
        writer.flush();
        if (confirmOption == JOptionPane.OK_OPTION) {
            // System.out.println(option);
            writer.write(Integer.toString(option));
            writer.println();
            writer.flush();

            if (option == 1) {
                basicViewOnly(inventory);
                
            } else if (option == 2) {
                
                
                int response = JOptionPane.showConfirmDialog(null, "Are you updating the stock of an existing bike?", 
                    "Boilermaker Bikes", JOptionPane.YES_NO_OPTION);
                if (response == 0) {
                    CountDownLatch latch = new CountDownLatch(1);
                    BikeQuantityIDGUI bqig = new BikeQuantityIDGUI(latch);
                    SwingUtilities.invokeLater(bqig);
                    try {
                        latch.await();
                    } catch (InterruptedException ie) {
                        JOptionPane.showMessageDialog(null, "Error in fetching bike data.", "Boilermaker Bikes",
                            JOptionPane.ERROR_MESSAGE);
                        
                    }
                    boolean success = bqig.returnSuccess();
                    if (success) {
                        int[] idq = bqig.returnResult();
                        int tempID = idq[0];
                        int tempQuant = idq[1];
                        if (this.checkValidID(tempID)) {
                            Bike tempBike = this.getBikeWithID(tempID);
                            tempBike.setQuantity(tempBike.getQuantity() + tempQuant);
                            //updates the UserInfo in its entirety
                            ArrayList<Seller> tempSellers = UserInfo.getSellers();
                            for (Seller s: tempSellers) {
                                if (s.getUsername().equals(name)) {
                                    ArrayList<Bike> tempInventory = s.getInventory();
                                    for (Bike b: tempInventory) {
                                        if (b.getId() == tempID) {
                                            b.setQuantity(b.getQuantity() + tempQuant);
                                        }
                                    }
                                    s.setInventory(tempInventory);
                                }
                            }
                            UserInfo.setSellers(tempSellers);
                            UserInfo.writeUsers();
                            UserInfo.readUsers();

                        } else {
                            JOptionPane.showMessageDialog(null, "ID not found", "Boilermaker Bikes",
                            JOptionPane.ERROR_MESSAGE);

                        }
                    }



                } else {
                    CountDownLatch latch = new CountDownLatch(1);
                    BikeDetailsGUI bdg = new BikeDetailsGUI(latch);
                    SwingUtilities.invokeLater(bdg);
                    try {
                        latch.await();
                    } catch (InterruptedException ie) {
                        JOptionPane.showMessageDialog(null, "Error in fetching bike data.", "Boilermaker Bikes",
                            JOptionPane.ERROR_MESSAGE);
                    }
                    boolean success = bdg.returnSuccess();
                    if (success) {
                        Bike tempBike = bdg.getBike();
                        inventory.add(tempBike);
                        ArrayList<Seller> tempSellers = UserInfo.getSellers();
                        for (Seller s: tempSellers) {
                            if (s.getUsername().equals(name)) {
                                s.setInventory(inventory);
                            }
                        }
                        UserInfo.setSellers(tempSellers);
                        UserInfo.writeUsers();
                        UserInfo.readUsers();

                    }
                    
                }
            } else if (option == 3) {
                
                CountDownLatch latch = new CountDownLatch(1);

                BikeQuantityIDGUI bqig = new BikeQuantityIDGUI(latch);
                SwingUtilities.invokeLater(bqig);
                try {
                    latch.await();
                } catch (InterruptedException ie) {
                    JOptionPane.showMessageDialog(null, "Error in fetching bike data.", "Boilermaker Bikes",
                        JOptionPane.ERROR_MESSAGE);
                    
                }
                boolean success = bqig.returnSuccess();
                if (success) {
                    int[] idq = bqig.returnResult();
                    int tempID = idq[0];
                    int tempQuant = idq[1];
                    if (this.checkValidID(tempID)) {
                        Bike tempBike = this.getBikeWithID(tempID);
                        tempBike.setQuantity(tempBike.getQuantity() - tempQuant);

                        if ((tempBike.getQuantity() - tempQuant) > 0) {
                            //updates the UserInfo in its entirety
                            ArrayList<Seller> tempSellers = UserInfo.getSellers();
                            for (Seller s: tempSellers) {
                                if (s.getUsername().equals(name)) {
                                    ArrayList<Bike> tempInventory = s.getInventory();
                                    for (Bike b: tempInventory) {
                                        if (b.getId() == tempID) {
                                            b.setQuantity(b.getQuantity() - tempQuant);
                                        }
                                    }
                                    s.setInventory(tempInventory);
                                }
                            }
                            UserInfo.setSellers(tempSellers);
                            UserInfo.writeUsers();
                            UserInfo.readUsers();

                        } else {
                            //removes inventory that is less than 0
                            ArrayList<Seller> tempSellers = UserInfo.getSellers();
                            for (Seller s: tempSellers) {
                                if (s.getUsername().equals(name)) {
                                    ArrayList<Bike> tempInventory = s.getInventory();
                                    for (Bike b: tempInventory) {
                                        if (b.getId() == tempID) {
                                            tempInventory.remove(b);
                                            break;
                                        }
                                    }
                                    s.setInventory(tempInventory);
                                }
                            }
                            UserInfo.setSellers(tempSellers);
                            UserInfo.writeUsers();
                            UserInfo.readUsers();
                        }


                    } else {
                        JOptionPane.showMessageDialog(null, "ID not found", "Boilermaker Bikes",
                        JOptionPane.ERROR_MESSAGE);

                    }
                }
                

            } else if (option == 4) {
                String term = JOptionPane.showInputDialog(null, "Enter a search term",
                "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);

                if (!(term == null || (term != null && term.equals("")))) {
                    writer.write(term);
                    writer.println();
                    writer.flush();
                   // System.out.println("option 4 test");

                    String matchesString = reader.readLine();
                    String succ = reader.readLine();
                    boolean success = Boolean.parseBoolean(succ);
                    ArrayList<Bike> matches = new ArrayList<>(inventory);
                    if (success) {
                        try {
                            matches = recieveArrayList(matchesString);
                            basicViewOnly(matches);
                        } catch (Exception e) {
                            JOptionPane.showMessageDialog(null, "No matches found!", "Boilermaker Bikes",
                            JOptionPane.ERROR_MESSAGE);
                        }
                        
                            
                        
                    }
                } else {
                    writer.write("null");
                    writer.println();
                    writer.flush();
                }
                
                
                
            } else if (option == 5) {
               String deletedString = reader.readLine();
               boolean deleted = Boolean.parseBoolean(deletedString);

               if (deleted) {
                JOptionPane.showMessageDialog(null, "Account Successfully deleted.", "Boilermaker Bikes", 
                    JOptionPane.INFORMATION_MESSAGE);
               } else {
                JOptionPane.showMessageDialog(null, "An Error Occurred.", "Boilermaker Bikes",
                JOptionPane.ERROR_MESSAGE);
               }
                

            } else if (option == 6) {
                String cCarts = reader.readLine();

                if (cCarts.length() == 0) {
                    JOptionPane.showMessageDialog(null, "No Bikes Found.", "Boilermaker Bikes",
                        JOptionPane.ERROR_MESSAGE);
                } else {
                    ArrayList<PurchasedBike> customerCarts = recievePurchasedBikes(cCarts);

               
                    basicViewPurchasedBikes(customerCarts);
                }
                



            } else if (option == 7) {
                boolean success = true;
                String analyticsString = reader.readLine();
                ArrayList<PurchasedBike> analytics = new ArrayList<>();
                try {
                    analytics = recievePurchasedBikes(analyticsString);
                } catch (ArrayIndexOutOfBoundsException fjlsdkjf) {
                    JOptionPane.showMessageDialog(null, "No customers are interested in your bikes", "CS180 HW11",
                    JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "An error occured. No Action was taken.", "CS180 HW11",
                    JOptionPane.ERROR_MESSAGE);
                    success = false;
                }
                
                if (success) {
                    viewAnalytics(analytics);
                }
            } else if (option == 8) {
                String vtr = sendArrayList(inventory);

                writer.write(vtr);
                writer.println();
                writer.flush();

                String strSuccess = reader.readLine();
                boolean success = Boolean.parseBoolean(strSuccess);

                if (success) {
                    JOptionPane.showMessageDialog(null, "Successfully saved data. Exiting now...", "Boilermaker Bikes", 
                        JOptionPane.INFORMATION_MESSAGE);
                    UserInfo.writeUsers();
                } else {
                    JOptionPane.showMessageDialog(null, "An error occured. No action was taken.", "Boilermaker Bikes",
                    JOptionPane.ERROR_MESSAGE);
                }

                writer.close();
                reader.close();
            } else {
                JFileChooser j = new JFileChooser();
                j.setDialogTitle("Choose a file to export inventory to.");
                int save = j.showSaveDialog(null);
                File file = null;
                if (save == JFileChooser.APPROVE_OPTION) {
                    file = j.getSelectedFile();
                    String path = file.getPath();
                    writer.println(path);
                    writer.flush();
                    writer.println(name);
                    writer.flush();
                    String success = reader.readLine();
                    if (success.equals("true")) {
                        JOptionPane.showMessageDialog(null, "Success!");
                    } else if (success.equals("false")) {
                        JOptionPane.showMessageDialog(null, "An error occurred, try again!");
                    }
                } else {
                    writer.println("exit");
                    writer.flush();
                    
                }
                
            }
            //sends the chosen option to the server to be processed and then returns this index to the user
           // writer.write("" + Integer.toString(option));
            //writer.println();
            //writer.flush();
            
            return option;
        } else {
            JOptionPane.showMessageDialog(null,"Thank you for visiting Boilermaker Bikes!",
                    "Boilermaker Bikes", JOptionPane.INFORMATION_MESSAGE);

            
            String vtr = sendArrayList(inventory);

            writer.write(vtr);
            writer.println();
            writer.flush();

            String strSuccess = reader.readLine();
            boolean success = Boolean.parseBoolean(strSuccess);

            if (success) {
                JOptionPane.showMessageDialog(null, "Successfully saved data. Exiting now...", "Boilermaker Bikes", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "An error occured. No action was taken.", "Boilermaker Bikes",
                JOptionPane.ERROR_MESSAGE);
            }

            writer.write(vtr);
            writer.println();
            writer.flush();

            writer.close();
            reader.close();
            return -1;
        }

       

    }

    public static void basicViewOnly(ArrayList<Bike> bikes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bikes: \n");
        
        // ArrayList<JLabel> displayBikes = new ArrayList<>();
        for (Bike b : bikes) {
            sb.append(b.toNiceString() + "\n");

        }
        // sb.deleteCharAt(sb.length() - 1);

        String vtr = sb.toString();

        JOptionPane.showMessageDialog(null, vtr, "Boilermaker Bikes", 
            JOptionPane.INFORMATION_MESSAGE);
        

    }

    public static void basicViewPurchasedBikes(ArrayList<PurchasedBike> bikes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bikes: \n");
        
        // ArrayList<JLabel> displayBikes = new ArrayList<>();
        for (PurchasedBike b : bikes) {
            sb.append(b.toNiceString() + "\n");

        }
        // sb.deleteCharAt(sb.length() - 1);

        String vtr = sb.toString();

        JOptionPane.showMessageDialog(null, vtr, "Boilermaker Bikes", 
            JOptionPane.INFORMATION_MESSAGE);
        

    }

    public static void viewAnalytics(ArrayList<PurchasedBike> bikes) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bikes: \n");
        
        int total = 0;
        // ArrayList<JLabel> displayBikes = new ArrayList<>();
        for (PurchasedBike b : bikes) {
            sb.append(String.format(b.toNiceString() + " | Revenue: $%.2f\n", (b.getPrice() * b.getQuantity())));
            total += b.getQuantity();
        }
        // sb.deleteCharAt(sb.length() - 1);

        sb.append(String.format("Total bikes purchased: %d\n", total));
        String vtr = sb.toString();

        JOptionPane.showMessageDialog(null, vtr, "Boilermaker Bikes", 
            JOptionPane.INFORMATION_MESSAGE);
        

    }

    public boolean checkValidID(int id) {
        boolean valid = false;
        for (Bike b : inventory) {
            if (b.getId() == id) {
                valid = true;
                return valid;
            }
        }
        return valid;
    }

    public Bike getBikeWithID(int id) {
        for (Bike b : inventory) {
            if (b.getId() == id) {
                return b;
            }
        }
        Bike vtr = null;
        return vtr;
    }
}
