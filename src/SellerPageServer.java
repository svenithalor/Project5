import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.tree.ExpandVetoException;

import java.util.ArrayList;
import java.lang.StringBuilder;
public class SellerPageServer {

    // fields
    private String name;
    private ArrayList<Bike> inventory;

    public SellerPageServer(String name, ArrayList<Bike> bikes) {
        this.name = name;
        this.inventory = bikes;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Bike> getInventory() {
        return inventory;
    }

    public void setInventory(ArrayList<Bike> i) {
        this.inventory = i;
    }

    /*******
     * Creates a Bike object when passed in the toString() format
     */
    public static Bike parseBike(String newBike) {
        
        String[] sNB = newBike.split(",");
        int wS = Integer.valueOf(sNB[1]);
        double p = Double.valueOf(sNB[2]);
        boolean u = Boolean.parseBoolean(sNB[4]);
        int q = Integer.valueOf(sNB[7]);
        int tempID = Integer.valueOf(sNB[8]);

        Bike bike = new Bike(sNB[0], wS, p, sNB[3], u, sNB[5], sNB[6], q, tempID);

        return bike;
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
    public static ArrayList<Bike> recieveArrayList(String s) {
        String[] bikes = s.split("]");
        ArrayList<Bike> vtr = new ArrayList<>();

        for (String bike : bikes) {
            vtr.add(parseBike(bike));
        }

        return vtr;
    }

    public ArrayList<Bike> searchBike(String term) {
        ArrayList<Bike> matches = new ArrayList<>();

        String t = term.toLowerCase();
        for (Bike b : inventory) {
            if (b.getColor().contains(t) || b.getModelName().contains(t)
                || b.getDescription().contains(t) || Integer.toString(b.getId()).contains(t)) {
                    matches.add(b);
                }
        }
        return matches;
    }

    public boolean deleteAccount() {
        String user = this.getName();
        ArrayList<Seller> sellers = UserInfo.getSellers();
        
        try {
            for (Seller seller : sellers) {
                if(seller.getUsername().equalsIgnoreCase(user)) {
                    sellers.remove(seller);
                    //System.out.println("Account deleted.");
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
        
    }
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(4242); // change this port later

            Socket socket = ss.accept();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream()); 

            String sellerName = reader.readLine(); // reads seller name first

            String sellerInventory = reader.readLine(); // reads the inventory immediately after

            ArrayList<Bike> inv = recieveArrayList(sellerInventory);

            

            
            SellerPageServer sp = new SellerPageServer(sellerName, inv);

            String strOption = null;
            int option = -1;
            do {
                strOption = reader.readLine(); // will read the input from client
                option = Integer.parseInt(strOption);
                // Options 1-3 are taken care of within client
                if (option == 4) {
                    String term = reader.readLine();
                    ArrayList<Bike> matches = sp.searchBike(term);
                    String vtr = sendArrayList(matches);

                    writer.write(vtr);
                    writer.println();
                    writer.flush();

                } else if (option == 5) {
                    
                    boolean deleted = sp.deleteAccount();
                    
                    String vtr = Boolean.toString(deleted);

                    writer.write(vtr);
                    writer.println();
                    writer.flush();

                }
                

            } while (option != 8);

            writer.close();
            reader.close();
            ss.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
