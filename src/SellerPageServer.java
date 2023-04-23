import java.io.*;
import java.net.*;
import javax.swing.*;
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
                String strOption1 = reader.readLine(); // will read the input from 

            } while (option != 8);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
