import java.util.Scanner;
import java.util.ArrayList;
import java.io.*;


/**
 * Seller Page
 * @author Aditya Bharadwaj, lab sec 4427
 * @version 4/7/2023
 */

public class SellerPage {
    private String name;
    private ArrayList<Bike> inventory;
    // not sure how this inventory will be created

    //Constructor
    public SellerPage(String name, ArrayList<Bike> inventory) {
        this.name = name;
        this.inventory = inventory;
    }


    public void runSellerPage(Seller seller) {
        Scanner s = new Scanner(System.in);
        // need to implement something about adding a bike

        
        SellerPage sp = new SellerPage(seller.getUsername(), seller.getInventory());
        int option;
        boolean repeat = true;
        do {
            System.out.println("Please select an option\n1. View current bikes\n2. Add new bike\n3. Remove Bike\n" +
            "4. Search Bike\n5. Delete Account\n6. View Customer Shopping Carts\n7. View analytics\n8. Exit");
            option = s.nextInt();
            s.nextLine();

            
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
                    sp.viewAnalytics()
                case 7: 
                    // System.out.println("Logging out...");

                    repeat = false;

                    
                    seller.setInventory(inventory);
                    ArrayList<Bike> bikes = ControlFlowMenu.getBikes();
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
                    
                    ControlFlowMenu.setBikes(bikes);
                    UserInfo.setSellers(sellers);

                    break;
                    
                    
                default: repeat = true;
                    System.out.println("Invalid Input!");
                    break;
                

                
            }
        } while(repeat);
        
    }   
    /********
     * This method displays all the bikes in the seller's inventory
     * 
     */
    public void displayBikes() {
        System.out.println("Current inventory:");
        for (Bike bike : inventory) {
            String vtr = String.format("%s, $%.2f, %d inches, %s, %d", 
                bike.getModelName(), bike.getPrice(), bike.getWheelSize(), bike.getColor(), bike.getQuantity());
            System.out.println(vtr + "\n");
            System.out.println("--------------------");
        }
    }

    /*********
     * This method adds a bike to the seller inventory
     * @param b the bike to be added to the seller inventory
     */
    public void addBike(Bike b) {
        
        boolean duplicate = false;

        Bike toEdit = null;
        for (Bike i : inventory) {
            if (i.getId() == b.getId()) {
                duplicate = true;
                toEdit = i;
                break;

                
            }
        }
        
        if (duplicate) {
            String out = String.format("You have added %d bikes to the stock of %s.", b.getQuantity(), 
                b.getModelName());
            System.out.println(out);

            toEdit.setQuantity(toEdit.getQuantity() + b.getQuantity());

        } else {
            System.out.printf("You have added %d of new bike %s\n", b.getQuantity(), b.getModelName());
            inventory.add(b);
        }
        
    }

    /********
     * This method removes bikes from the seller inventory
     * @param b the bike to be removed from the inventory
     */
    public void removeBike(Bike b) {
        // removes the bike from the inventory
        for (Bike check : inventory) {
            if (check.getId() == b.getId()) {
                inventory.remove(b);
                System.out.printf("Successfully removed '%s' from inventory.\n", b.getModelName());
                return;
            }
        }
        System.out.printf("Bike '%s' was not found! No action was made.\n", b.getModelName());
    }

    //Note* We could probably make the searchBike() method a more general method that both the buyer and the seller
    //could utilize by entering two parameters => the term that is being searched for
    // an ArrayList of the bikes on the listing page
    /**********
     * This method searches for a bike in the seller inventory based on a keyword
     * @param term the term
     * @return
     */
    public void searchBike(String term) {
        ArrayList<Bike> matches = new ArrayList<>();

        String t = term.toLowerCase();
        for (Bike b : inventory) {
            if (b.getColor().contains(t) || b.getModelName().contains(t)
                || b.getDescription().contains(t) || Integer.toString(b.getId()).contains(t)) {
                    matches.add(b);
                }
        }

        System.out.println("Matches for '" + term + "':");
        if (matches.size() == 0) {
            System.out.println("No matches found!");
        } else {
            for (Bike bike : matches) {
                String vtr = String.format("%s, $%.2f, %d inches, %s, %d", 
                    bike.getModelName(), bike.getPrice(), bike.getWheelSize(), bike.getColor(), bike.getQuantity());
                System.out.println(vtr + "\n");
                System.out.println("--------------------\n");
            }
        }
        

       
    }
    /*******
     * Deletes the account of this user
     * @param user
     */
    public void deleteAccount(String user) {
        ArrayList<Seller> sellers = UserInfo.getSellers();
        for (Seller seller : sellers) {
            if(seller.getUsername().equalsIgnoreCase(user)) {
                sellers.remove(seller);
                System.out.println("Account deleted.");
            }
        }
    }

    public void viewAnalytics(String filename) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(filename));
            ArrayList<PurchasedBike> carts = new ArrayList<>();

            String line = bfr.readLine();

            while (line != null) {
                if (line.startsWith(String.format("%s.purchasehistory", name))) {
                    String part = line.substring(line.indexOf(" ") + 1);
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
                    carts.add(new PurchasedBike(color, wheelSize, price, finalPrice, modelName, used, description, 
                        sellerName, quantity, insured, id));
                }

                line = bfr.readLine();
            }

            int totalItems = 0;
            System.out.println("Customer Cart Information:");
            if (carts.size() == 0) {
                System.out.println("No information found");
            } else {
                for (PurchasedBike p : carts) {
                    System.out.printf(p.toString() + "| Revenue: $%.2f",(p.getPrice() * p.getQuantity()));
                    totalItems += p.getQuantity();

                }
                System.out.printf("There are %d items currently in customer carts.\n", totalItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong...");
        }
    }

    /*****
     * Able to view all the shopping cart information of all the buyers.
     * @param filename
     */
    public void viewCustomerCarts(String filename) {
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(filename));
            ArrayList<PurchasedBike> carts = new ArrayList<>();

            String line = bfr.readLine();

            while (line != null) {
                if (line.startsWith(String.format("%s.purchasehistory", name))) {
                    String part = line.substring(line.indexOf(" ") + 1);
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
                    carts.add(new PurchasedBike(color, wheelSize, price, finalPrice, modelName, used, description, 
                        sellerName, quantity, insured, id));
                }

                line = bfr.readLine();
            }

            int totalItems = 0;
            System.out.println("Customer Cart Information:");
            if (carts.size() == 0) {
                System.out.println("No information found");
            } else {
                for (PurchasedBike p : carts) {
                    System.out.println(p.toString());
                    totalItems += p.getQuantity();

                }
                System.out.printf("There are %d items currently in customer carts.\n", totalItems);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong...");
        }
       
        
    }
    
    // NOTE: commented out this method because it is obsolete, although I'll leave it here just in case
    //     public void exit() {
    //         System.out.println("Saving Data...");

    //         ArrayList<String> data = new ArrayList<>();


    //         try {
    //             BufferedReader bfr = new BufferedReader(new FileReader("SellerData.csv"));

    //             BufferedWriter bfw = new BufferedWriter(new FileWriter("SellerData.csv"));

    //             String line = bfr.readLine();

    //             while (line != null) {
    //                 if (!(line.contains(name))) {
    //                     data.add(line);
    //                     // adding all the file's lines that AREN'T the seller accessing the page
    //                 }

    //                 line = bfr.readLine();
    //             }

    //             for (String line : data) {
    //                 bfw.write(line);
    //             }

    //             StringBuilder currentSeller = new StringBuilder();

    //             currentSeller.append(name + ",");

    //             for (Bike b : inventory) {
    //                 currentSeller.append(b.toString() + ",");
                    
    //             }

    //             currentSeller.deleteCharAt(currentSeller.length() - 1);
    //             // deletes the comma that will end up at the end of the StringBuilder
                
    //             bfw.write(currentSeller.toString());

    //         } catch (Exception e) {
    //             e.printStackTrace();
    //             System.out.println("There was an error saving data...");
    //         }

    //         System.out.println("Exited Successfully.");


    //     }
}