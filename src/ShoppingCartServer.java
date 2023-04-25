import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**************
 *This class processes the user input of adding, removing, and checking out items from the shopping cart.
 *
 * @author Christina Joslin, lab sec 4427
 * @version 4/24/2023
 */
public class ShoppingCartServer {
    private ArrayList<PurchasedBike> shoppingCart; //a copy of the buyer's current shopping cart
    private ArrayList<PurchasedBike> purchaseHistory; //a copy of the buyer's current purchase history
    private ArrayList<Bike> bikesForSale; //a copy of the bikes available on the listing page
    private Buyer buyer; //the buyer using this shopping cart
    private ArrayList<Buyer> buyers; //a copy of the buyers database

    //Constructs the customer page and initializes an arraylist of bike
    public ShoppingCartServer(Buyer buyer) {
        this.buyer = buyer;
        this.shoppingCart = buyer.getShoppingCart();
        this.purchaseHistory = buyer.getPurchaseHistory();
        this.bikesForSale = UserInfo.getBikes();
        this.buyers = UserInfo.getBuyers();
    }


    //Methods

    /*************
     * This method allows the buyer to add a bike to their shopping cart
     * @param reader
     * @param writer
     * @param s
     */
    public void addBike(BufferedReader reader, PrintWriter writer, ShoppingCartServer s) {
        /*******
         * Checks if the user entered a valid Bike ID or not
         */
        boolean validId = false;
        String d = ""; //temporarily saves the bike id entered by the user and if valid converts it to an integer
        do {
            try {
                d = reader.readLine();
            } catch (Exception e) {
                System.out.println("addBike method error under id");
                return;
            }
            validId = s.checkBikeID(d, "add");
            //lets the client know if the user input is valid or not
            writer.write("" + validId);
            writer.println();
            writer.flush();

        } while (!validId);
        //saves the bike Id entered
        int bikeId = Integer.parseInt(d);

        /*******
         * Searches for the bike ID in the existing shopping cart. If it is already in the cart AND is on hte listing page
         * then simply request that the buyer add to the quantity they entered
         */
        boolean inCart = false;
        int bikeIndex = -1; //saves the bikeId that you want to add on to in the shopping cart
        int i = 0;
        for (PurchasedBike p : shoppingCart) {
            if (p.getId() == bikeId) {
                inCart = true;
                bikeIndex = i;
                i++;
                break;
            }
        }
        writer.write("" + inCart);
        writer.println();
        writer.flush();

        if (inCart) {
            /****
             * Checks if the user entered a valid Bike Quantity to add on to the existing total
             */
            boolean validQuantity = false;
            String q = ""; //temporarily saves the quantity entered by the user and if valid converts it to an integer
            do {
                try {
                    q = reader.readLine();
                } catch (Exception e) {
                    System.out.println("addBike method error under quantity");
                    return;
                }
                validQuantity = s.checkBikeQuantity(q, bikeId, inCart, bikeIndex);
                writer.write("" + validQuantity);
                writer.println();
                writer.flush();
            } while (!validQuantity);
            int quantity = Integer.parseInt(q);  //saves the quantity entered if valid
            /******
             * Updates the current bike in the buyer's shopping cart
             */
            int existingQuantity = shoppingCart.get(bikeIndex).getQuantity();
            shoppingCart.get(bikeIndex).setQuantity(existingQuantity + quantity);
            buyer.setShoppingCart(shoppingCart);
            buyers.set(UserInfo.getBuyerIndex(buyer), buyer);
            UserInfo.setBuyers(buyers);
            //for (Buyer b : UserInfo.getBuyers()) {
            // System.out.println(b.toString());
            //}

            //lets the client know that it has been successfully added to the shopping cart
            writer.write("true");
            writer.println();
            writer.flush();


        } else if (!inCart) {

            /****
             * Checks if the user entered a valid Bike Quantity to add
             */
            boolean validQuantity = false;
            String q = ""; //temporarily saves the quantity entered by the user and if valid converts it to an integer
            do {
                try {
                    q = reader.readLine();
                } catch (Exception e) {
                    System.out.println("addBike method error under quantity");
                    return;
                }
                validQuantity = s.checkBikeQuantity(q, bikeId, inCart, bikeIndex);
                writer.write("" + validQuantity);
                writer.println();
                writer.flush();
            } while (!validQuantity);
            //saves the quantity entered
            int quantity = Integer.parseInt(q);

            double finalPrice = 0.0; //saves the price (including quantity and insurance that the user wants to purchase a bike)


            /********
             * Checks if the user wants $50 insurance added to their bike
             */
            Bike bikeToAdd = UserInfo.searchBike(bikeId);
            try {
                boolean insured = Boolean.parseBoolean(reader.readLine());
                if (!insured) {
                    finalPrice = bikeToAdd.getPrice() * quantity;
                } else {
                    finalPrice = bikeToAdd.getPrice() * quantity + 50.00;
                }

                /******
                 * Adds the purchased bike to the buyer's shopping cart
                 */
                PurchasedBike newPurchase = new PurchasedBike(bikeToAdd, finalPrice, insured);
                shoppingCart.add(newPurchase);
                buyer.setShoppingCart(shoppingCart);
                buyers.set(UserInfo.getBuyerIndex(buyer), buyer);
                UserInfo.setBuyers(buyers);


                //lets the client know that it has been successfully added to the shopping cart
                writer.write("true");
                writer.println();
                writer.flush();


            } catch (Exception e) {
                System.out.println("Error under adding insurance in AddBike");
                return;
            }
        }


    }

    /******
     * This method allows the buyer to check out all of the items in their shopping cart and updates
     * the listing page to correspond with this purchase
     * @param reader
     * @param writer
     */

    public void checkout(BufferedReader reader, PrintWriter writer) {
        /********
         * Checks if all the bikes in the shopping cart still exist on the listing page
         */
        boolean stillAvailable = true; //saves whether or not all of the bikes are available for purchase
        int bikeEquivalentIndex = -1; //saves the index of the bike on the listing page corresponding to the bike in the shopping cart

        for (PurchasedBike pb : shoppingCart) {
            if (!UserInfo.getBikes().contains(pb)) {
                stillAvailable = false;
                break;
            } else {
                /*******
                 * Checks if all the quantities in the shopping cart are still valid
                 */
                bikeEquivalentIndex = UserInfo.getBikes().indexOf(pb);
                Bike bikeEquivalent = UserInfo.getBikes().get(bikeEquivalentIndex);

                if (bikeEquivalent.getQuantity() == 0) {
                    stillAvailable = false;
                    break;
                }
                System.out.println(bikeEquivalent.toString());
                System.out.println(pb.toString());

                if (bikeEquivalent.getQuantity() < pb.getQuantity()) {
                    stillAvailable = false;
                    break;
                }

            }
        }

        /******
         * Sends the status of the availability of the bike to the client
         */
        writer.write(stillAvailable + "");
        writer.println();
        writer.flush();

        if (stillAvailable) {
            /******
             * Updates the listing Page
             */
            for (Bike b : bikesForSale) {
                for (PurchasedBike pb : shoppingCart) {
                    if (pb.getId() == b.getId()) {
                        b.setQuantity(b.getQuantity() - pb.getQuantity());
                    }
                }
            }
            UserInfo.setBikes(bikesForSale);

            /******
             * Updates the seller inventory
             */
            ArrayList<Seller> tempSellers = UserInfo.getSellers();
            for (Seller se : tempSellers) {
                for (Bike b : se.getInventory()) {
                    for (PurchasedBike pb : shoppingCart) {
                        b.setQuantity(b.getQuantity() - pb.getQuantity());
                    }
                }
            }
            UserInfo.setSellers(tempSellers);

            /*******
             * Moves everything in the shopping cart to the purchase history
             */
            ArrayList<PurchasedBike> tempPurchaseHistory = buyer.getPurchaseHistory();
            ArrayList<PurchasedBike> tempShoppingCart = buyer.getShoppingCart();

            int len = shoppingCart.size();
            for (int i = 0; i < len; i++) {
                purchaseHistory.add(shoppingCart.get(0));
                shoppingCart.remove(0);

            }
            buyer.setShoppingCart(tempShoppingCart);
            buyer.setPurchaseHistory(tempPurchaseHistory);
            buyers.set(UserInfo.getBuyerIndex(buyer), buyer);

            UserInfo.setBuyers(buyers);

            //once it has completed the saving process send the message success to the buyer
            writer.write("success");
            writer.println();
            writer.flush();

        }
    }


    public void removeBike(int id,BufferedReader reader,PrintWriter writer) {
        //ask for valid bike id
        //ask for valid quantity (check in the shopping cart)
        //remove quantity from bike
        //add to the listing page (incrementing existing quantity or adding a new completely

        // check id
        boolean validId = false;
        String d = "";
        do {
            try {
                d = reader.readLine();
            } catch (Exception e) {
                System.out.println("removeBike method error under id");
                return;
            }
           // validId = s.checkBikeID(d, "delete");
            writer.write("" + validId);
            writer.println();
            writer.flush();

        } while (!validId);
        //saves the bike Id entered
        int bikeId = Integer.parseInt(d);


        // check quantity
        boolean validQuantity = false;
        String q = "";
        int quantity;
        int bikeIndex = -1;
        do {
            try {
                q = reader.readLine();
            } catch (Exception e) {
                System.out.println("removeBike method error under quantity");
                return;
            }
            int i = 0;
            for (PurchasedBike b : shoppingCart) {
                if (b.getId() == bikeId) {
                    quantity = b.getQuantity();
                    bikeIndex = i;
                    i++;
                    break;
                }
            }
            //validQuantity = s.checkCartBikeQuantity(q, bikeId, cartIndex);
            writer.write("" + validQuantity);
            writer.println();
            writer.flush();
        } while (!validQuantity);
       // int remainQuantity = quantity - Integer.parseInt(q);


        //need to integrate this into our existing methods
//        PurchasedBike bikeToRemove = null;
//        for (PurchasedBike bike : shoppingCart) {
//            if (bike.getId() == id) {
//                shoppingCart.remove(bike);
//                bikeToRemove = bike;
//                System.out.println("Bike removed!");
//                break;
//            }
//        }
//        if (bikeToRemove == null) {
//            System.out.println("No bike found!");
//        } else {
//            Bike bike = new Bike(bikeToRemove);
//            bikesForSale.add(bike);
//            UserInfo.setBikes(bikesForSale);
//        }
    }

    public boolean checkCartBikeQuantity(String input, int bikeId, int cartIndex) {
        int removeQuantity = -1;
        boolean valid = false;

        try {
            removeQuantity = Integer.parseInt(input);
        } catch (Exception e) {
            return false;
        }

        if (removeQuantity == 0) {
            return false;
        }

        int existQuantity = buyer.getShoppingCart().get(cartIndex).getQuantity();

        if (removeQuantity < existQuantity) {
           // buyer.getShoppingCart().get(cartIndex).getQuantity -= removeQuantity;
            valid = true;
        } else if (removeQuantity == existQuantity) {
            //buyer.getShoppingCart().removeBike(bikeId);
            valid = true;
        } else {
            return false;
        }

        return valid;
    }

    /**********
     * This method checks if the user entered a bike quantity that is an integer is still in stock (so the quantity on
     * the listing page is not equal to 0 and/or has not been removed), and the quantity requested is not more than the
     * quantity available
     * @author Christina Joslin
     *
     * @param input the quantity of bikes to be entered by the buyer
     * @param bikeId the unique 4-digit id of the bike that the buyer wants to purchase
     * @param inCart checks if a bike that is being added is already in the buyer's shopping cart
     * @param cartIndex the index of the bike to be added or removed from the shopping cart
     * @return true if the quantity is valid (meets the conditions above) and false if hte quantity is not valid
     * (does not meet the conditions above)
     * @author Christina Joslin
     */
    public boolean checkBikeQuantity(String input, int bikeId, boolean inCart, int cartIndex) {
        int purchaseQuantity = -1; //stores the quantity of bikes that the buyer wants to purchase
        boolean found = false; //checks if the bikeId they would like to purchase is found in the list

        //checks if the input is an integer
        try {
            purchaseQuantity = Integer.parseInt(input);
        } catch (Exception e) {
            return false;
        }
        //checks if the quantity equals 0
        if (purchaseQuantity == 0) {
            return false;
        }

        //adjusts the quantity if the cart the buyer wants to add on to an already exists in the shopping cart
        if (inCart) {
            purchaseQuantity += buyer.getShoppingCart().get(cartIndex).getQuantity();
        }
        //checks if the bikeID they want to purchase exists on the listing page
        for (Bike b : UserInfo.getBikes()) {
            //finds the corresponding bike id they want to add or purchase
            if (b.getId() == bikeId) {
                found = true;
                //finds the quantity for the bike id they want to remove and if it is equal to 0 then return false
                if (b.getQuantity() == 0) {
                    return false;
                }
                //if the quantity available of this bike id is less than the quantity they want to purchase, return false
                if (b.getQuantity() < purchaseQuantity) {
                    return false;
                }
            }
        }
        //if the bike id they want to purchase does not exist at all then return false
        if (!found) {
            return false;
        }
        return found;
    }

    /********
     * This method checks if the user entered Bike ID is a 4 digit number that is already in the user's shopping cart (add)
     * or is already in the listing page (delete). If meets these requirements, then return true. If it does not,
     * then return false.
     * @author Christina Joslin
     * @param input the bike id entered by the user
     * @param buttonType the type of button (add or delete) to be used by the user
     * @return true or false indicating is the user input is valid
     */
    public boolean checkBikeID(String input, String buttonType) {
        int bikeId = -1; //saves the bike ID entered by the user

        //checks if the bike id consists of 4 digits
        if (input.length() != 4) {
            return false;
        }
        try {
            //checks if the bikeID consists of numbers. if not then return false
            bikeId = Integer.parseInt(input);

        } catch (Exception e) {
            return false;
        }
        //checks if the bike id is either found on the listing page ("add") or in the shopping cart ("delete")
        switch (buttonType) {
            case "add":
                for (Bike b : UserInfo.getBikes()) {
                    if (b.getId() == bikeId) {
                        return true;
                    }
                }
                break;
            case "delete":
                for (PurchasedBike pb : buyer.getShoppingCart()) {
                    if (pb.getId() == bikeId) {
                        return true;
                    }
                }
                break;

        }
        return false;
    }

    public static void main(String[] args) {
        UserInfo.readUsers(); //TEMP VAlUE reads the users in

        //creates a Shopping Cart server object to navigate the shopping cart server
        ShoppingCartServer s = new ShoppingCartServer(UserInfo.getBuyers().get(0)); ///TEMP VALUE!
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            Socket socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            do {
                //waits for what button the user presses
                String input = reader.readLine();

                if (input.equals("add")) {

                    s.addBike(reader, writer, s);

                } else if (input.equals("delete")) {

                    //s.removeBike(reader,writer,s);


                } else if (input.equals("checkout")) {

                    s.checkout(reader, writer);


                } else if (input.equals("backHome")) {

                    //do something

                } else if (input.equals("refresh")) {

                    //do something *we may or may not need this if statement*
                }


            } while (true);


        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}
