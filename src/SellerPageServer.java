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
    private int port;

    public SellerPageServer(String name, ArrayList<Bike> bikes, int port) {
        this.name = name;
        this.inventory = bikes;
        this.port = port;
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

    public static PurchasedBike parsePurchasedBike(String part) {
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

    public static String sendPurchasedBikes(ArrayList<PurchasedBike> bike) {
        StringBuilder sb = new StringBuilder();
        for (PurchasedBike b : bike) {
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
            if (b.getColor().toLowerCase().contains(t.toLowerCase()) || b.getModelName().toLowerCase().contains(t.toLowerCase())
                || b.getDescription().toLowerCase().contains(t.toLowerCase()) || Integer.toString(b.getId()).toLowerCase().contains(t.toLowerCase())) {
                    matches.add(b);
                }
        }
        return matches;
    }

    public boolean deleteAccount() {
        String user = this.getName();
        ArrayList<Seller> sellers = UserInfo.getSellers();
        ArrayList<Bike> allBikes = UserInfo.getBikes();

        try {
            for (Seller seller : UserInfo.getSellers()) {
                if(seller.getUsername().equalsIgnoreCase(user)) {
                    sellers.remove(seller);
                    
                    //System.out.println("Account deleted.");
                }
            }
            
            int i = 0;
            do {
                
                
                if (allBikes.get(i).getSellerName().equalsIgnoreCase(user)) {
                    allBikes.remove(i);
                } else {
                    i++;
                }
            } while (i < allBikes.size());

            UserInfo.setSellers(sellers);
            UserInfo.setBikes(allBikes);
            UserInfo.writeUsers();
        } catch (Exception e) {
            return false;
        }
        return true;
        
    }

    public boolean exportInventory(String path, String username) {
        try {
            File file = new File(path);
            PrintWriter pw = new PrintWriter(file);
            
            

            for (Bike b : inventory) {
                pw.println(b.toNiceString());
            }
            pw.flush();
            pw.close();
            return true;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred! Try again", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    public void run() {
        try {
            ServerSocket ss = new ServerSocket(port);

            //runs the sellerClient thread
            Thread sellerClient = new Thread() {
                public void run() {
                    SellerPageClient C = new SellerPageClient(name,inventory,port);
                    C.runSellerPageClient(name,inventory);
                }
            };
            sellerClient.start();
            Socket socket = ss.accept();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream()); 

            String sellerName = reader.readLine(); // reads seller name first

            //System.out.println("Name: " + sellerName);

            String sellerInventory = reader.readLine(); // reads the inventory immediately after

            //System.out.println("check1");
            ArrayList<Bike> inv = new ArrayList<>();
            try {
                inv = recieveArrayList(sellerInventory);

            } catch (ArrayIndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(null, "New Seller? Get started by adding your first bike!", 
                "Boilermaker Bikes - Seller Page", JOptionPane.INFORMATION_MESSAGE);
            }

            

            
            SellerPageServer sp = new SellerPageServer(sellerName, inv,port);

            String strOption = null;
            String strConfirmOption = null;
            int option = -1;
            int confirmOption = -1;
            do {
                strConfirmOption = reader.readLine();
                confirmOption = Integer.parseInt(strConfirmOption);
               // System.out.println(confirmOption);

                if (confirmOption != 1) {
                    strOption = reader.readLine(); // will read the input from client
                    //System.out.println("Check2");
                    try {
                        option = Integer.parseInt(strOption);
                    } catch (Exception e) {

                    }
                    //System.out.println(strOption);
                    // Options 1-3 are taken care of within client
                    
                    if (option == 4) {
                        boolean success = true;

                        String term = reader.readLine();

                        if (!(term.equals("null"))) {
                            ArrayList<Bike> matches = new ArrayList<>();
                            try {
                                matches = sp.searchBike(term);

                            } catch(Exception ie) {
                                success = false;
                            }
                            String vtr = sendArrayList(matches);

                            writer.write(vtr);
                            writer.println();
                            writer.flush();

                            vtr = Boolean.toString(success);
                            writer.write(vtr);
                            writer.println();
                            writer.flush();
                        }
                        


                    } else if (option == 5) {
                        
                        boolean deleted = sp.deleteAccount();
                        
                        String vtr = Boolean.toString(deleted);

                        writer.write(vtr);
                        writer.println();
                        writer.flush();

                    } else if (option == 6) {
                        // BELOW CODE IS FOR TESTING SELLERPAGE ALONE
                        String testb1 = "green,24,209.99,209.99,JoyStar,false, Is a class looking cruiser bike,test,1,false,1090";
                        String testb2 = "pink,20,189.99,189.99,RoyalBaby Stargirl,false,Fashionable design and classic color matching,test,2,false,1101";
                        String testb3 = "green,26,1499.00,1499.00,VNUVCOE Electric Bike,false,Normal Bike Mode & Pedal Assist Mode,BikesAreCool,1,false,2102";

                        ArrayList<PurchasedBike> bruh = new ArrayList<>();

                        bruh.add(parsePurchasedBike(testb1));
                        bruh.add(parsePurchasedBike(testb2));
                        bruh.add(parsePurchasedBike(testb3));
                        //System.out.println("test2" + bruh.get(2).getSellerName());
                        ArrayList<PurchasedBike> matches = new ArrayList<>();
                        
                        // ABOVE CODE IS FOR TESTING SELLERPAGE ALONE
                        for (Buyer b : UserInfo.getBuyers()) {
                            for (PurchasedBike pb : b.getShoppingCart()) {
                                if (sp.getName().equals(pb.getSellerName())) {
                                    matches.add(pb);
                                    
                                }
                            }
                        }
                        

                        String vtr = sendPurchasedBikes(matches);
                        
                        // System.out.println(vtr);

                        writer.write(vtr);
                        writer.println();
                        writer.flush();
                    } else if (option == 7) {

                        ArrayList<PurchasedBike> matches = new ArrayList<>();
                        for (Buyer buyer : UserInfo.getBuyers()) {
                            for (PurchasedBike pb : buyer.getPurchaseHistory()) {

                                if (pb.getSellerName().equals(sp.getName())) {
                                    matches.add(pb);
                                }
                            }
                        }

                        String vtr = sendPurchasedBikes(matches);
                        writer.write(vtr);
                        writer.println();
                        writer.flush();
                    } else if (option == 8) {
                        boolean success = true;
                        try {
                            String iString = reader.readLine();

                            ArrayList<Bike> invArrayList = recieveArrayList(iString);

                            sp.setInventory(invArrayList);

                            int sellerIndex = UserInfo.getSellerIndex(sellerName);

                            UserInfo.getSellers().get(sellerIndex).setInventory(sp.getInventory());
                        } catch (Exception e) {
                            success = false;
                        }

                        writer.write(Boolean.toString(success));
                        writer.println();
                        writer.flush();
                        
                    } else if (option == 9) {
                        String filePath = reader.readLine();
                        if (!(filePath.equals("exit"))) {
                            String username = reader.readLine();
                            boolean success = exportInventory(filePath, username);
                            writer.println(success);
                            writer.flush();
                        }
                        
                    }
                } else {
                    boolean success = true;
                    try {
                        String iString = reader.readLine();
        
                        ArrayList<Bike> invArrayList = recieveArrayList(iString);
        
                        sp.setInventory(invArrayList);
        
                        int sellerIndex = UserInfo.getSellerIndex(sellerName);
        
                        UserInfo.getSellers().get(sellerIndex).setInventory(sp.getInventory());
                    } catch (Exception e) {
                        success = false;
                    }
        
                    writer.write(Boolean.toString(success));
                    writer.println();
                    writer.flush();
                }
                

                
                //String updatedInventory = reader.readLine();
                //sp.setInventory(recieveArrayList(updatedInventory));
            } while (option != 8 && option != 5 && confirmOption != 1);

           

            writer.close();
            reader.close();
            ss.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
