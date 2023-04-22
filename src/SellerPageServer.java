import java.io.*;
import java.net.*;
import javax.swing.*;
import java.util.ArrayList;
import java.lang.StringBuilder;
public class SellerPageServer {
    try {
        ServerSocket ss = new ServerSocket(4242); // change this port later

        Socket socket = ss.accept();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream()); 
        String strOption = null;
        int option = null;
        do {
            String strOption = reader.readLine(); // will read the input from 

            int option = Integer.parseInt(strOption);
             switch (option) {
                case 1: 
                    sp.displayBikes();
                    break;
                case 2: 
                    System.out.println("Are you adding a new bike model?\n1. Yes\n2.No");
                    int addOpt = s.nextInt();
                    s.nextLine();

                    switch (addOpt) {
                        case 1:
                            System.out.println("Please enter a new bike in the following format:\n" +
                                    "color,wheelSize,price,modelName,used,description,sellerName,quantity,ID");
                            String newBike = s.nextLine();
                            String[] sNB = newBike.split(",");
                            int wS = Integer.valueOf(sNB[1]);
                            double p = Double.valueOf(sNB[2]);
                            boolean u = Boolean.parseBoolean(sNB[4]);
                            int q = Integer.valueOf(sNB[7]);
                            int tempID = Integer.valueOf(sNB[8]);

                            Bike bike = new Bike(sNB[0], wS, p, sNB[3], u, sNB[5], sNB[6], q, tempID);
                            sp.addBike(bike);
                            break;
                        case 2: 
                            ArrayList<Integer> IDs = new ArrayList<>();
                            for (Bike b : inventory) {
                                IDs.add(b.getId());
                            }
                            int currentID;
                            
                            do {
                                System.out.println("Enter the ID of an existing bike");
                                currentID = s.nextInt();
                                s.nextLine();

                                if (!(IDs.contains(currentID))) {
                                    System.out.println("ID not found, please try again.");
                                }
                                
                            } while (!(IDs.contains(currentID)));
                            Bike tempBike = null;
                            for (Bike b : inventory) {
                                if (b.getId() == currentID) {
                                    tempBike = b;
                                    break;
                                }
                            }
                            

                            sp.addBike(tempBike);
                            break;
                        default: 
                            System.out.println("Invalid input!");


                    }
                    
                    break;
                case 3: 
                    ArrayList<Integer> IDs = new ArrayList<>();
                    for (Bike b : inventory) {
                        IDs.add(b.getId());
                    }
                    int currentID;
                    
                    do {
                        System.out.println("Enter the ID of the bike that you want to remove.");
                        currentID = s.nextInt();
                        s.nextLine();

                        if (!(IDs.contains(currentID))) {
                            System.out.println("ID not found, please try again.");
                        }
                        
                    } while (!(IDs.contains(currentID)));
                    Bike bike = null;
                    for (Bike b : inventory) {
                        if (b.getId() == currentID) {
                            bike = b;
                            break;
                        }
                    }
                    sp.removeBike(bike);
                    break;
                case 4: 
                    System.out.println("Please enter a search term.");
                    String term = s.nextLine();
                    sp.searchBike(term);
                    break;
                case 5: 
                    
                    sp.deleteAccount(name);
                    break;
                case 6:
                    sp.viewCustomerCarts("buyer.csv");
                    break;
                case 7:
                    sp.viewAnalytics("buyers.csv");
                case 8:
                    //System.out.println("Logging out");
                    repeat = false;

                    
                    seller.setInventory(inventory);
                    ArrayList<Bike> bikes = UserInfo.getBikes();
                    ArrayList<Bike> toAdd = new ArrayList<>();

                    ArrayList<Seller> sellers = UserInfo.getSellers();

                    int sellerLen = sellers.size();
                    for (int i = 0; i < sellerLen; i++) {
                        Seller se = sellers.get(i);
                        if (se.getUsername().equals(name)) {
                            sellers.remove(se);
                            sellers.add(seller);
                        }
                    }

                    int bikeLen = bikes.size();
                    for (int i = 0; i < bikeLen; i++) {
                        Bike b = bikes.get(i);
                        if (b.getSellerName().equals(name)) {
                            toAdd.add(b);
                            bikes.remove(b);
                        }
                    }


                    for (Bike i : inventory) {
                        bikes.add(i);
                    }
                    
                    UserInfo.setBikes(bikes);
                    UserInfo.setSellers(sellers);

                    break;
                    
                    
                default: repeat = true;
                    System.out.println("Invalid Input!");
                    break;
                

                
            }
        }
        

    }
}
