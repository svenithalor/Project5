import javax.swing.*;
import java.io.File;
import java.io.*;
import java.net.*;
import java.util.*;

public class CustomerPageServer {
    static Buyer thisBuyer;

    //Methods
    public static void run(Buyer buyer) {
        try {
            thisBuyer = buyer; //makes a shallow copy of the buyer
            ServerSocket serverSocket = new ServerSocket(1233);

            //opens up the customer page client thread
            Thread buyerClient = new Thread() {
                public void run() {
                    CustomerPageClient.runClient(thisBuyer);
                }
            };
            buyerClient.start();

            Socket socket = serverSocket.accept(); //waits until the client connects
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());


            //the buyer currently navigating the customer page
            while (true) {
                int repeat = 1;

                while (repeat == 1) {
                    /*****
                     * Testing ONLY prints the bikes in the inventory
                     */
                    System.out.println("Updated inventory");
                    for (Bike b : UserInfo.getBikes()) {
                        System.out.println(b.toNiceString());
                    }
                    ArrayList<String> bikeNames = new ArrayList<>();
                    for (Bike bike : UserInfo.getBikes()) {
                        String format = "%s | $%.2f | Quantity: %d";
                        bikeNames.add(String.format(format, bike.getModelName(), bike.getPrice(), bike.getQuantity()));
                    }
                    writer.println(bikeNames);
                    writer.flush();
                    bikeNames.clear();


                    String input = reader.readLine();
                    int choice = Integer.parseInt(input);

                    switch (choice) {
                        case -1:
                            reader.close();
                            writer.close();
                            repeat = 0;
                            break;
                        case 1: // main menu switch case 1: view available bike
                            int repeat1 = 1;
                            do {
                                int choice1 = Integer.parseInt(reader.readLine());
                                switch (choice1) {
                                    default: // writing description of selected bike
                                        Bike chosenBike = UserInfo.getBikes().get(choice1);
                                        writer.println(String.format("Name: %s | $%.2f | %d inches", chosenBike.getModelName(), chosenBike.getPrice(), chosenBike.getWheelSize()));
                                        writer.println(String.format("Used: %b | Seller: %s | ID: %d", chosenBike.isUsed(), chosenBike.getSellerName(), chosenBike.getId()));
                                        writer.println(String.format("Description: %s", chosenBike.getDescription()));
                                        writer.flush();
                                        break;
                                    case -3: // sort by quantity
                                        ArrayList<Bike> quantitySorted = sortByQuantity(UserInfo.getBikes());
                                        ArrayList<String> sortedNames = new ArrayList<>();
                                        for (Bike bike : quantitySorted) {
                                            String format = "%s | $%.2f | Quantity: %d";
                                            sortedNames.add(String.format(format, bike.getModelName(), bike.getPrice(), bike.getQuantity()));
                                        }
                                        writer.println(sortedNames);
                                        writer.flush();
                                        sortedNames.clear();
                                        break;
                                    case -2: // sort by price
                                        ArrayList<Bike> priceSorted = sortByPrice(UserInfo.getBikes());
                                        ArrayList<String> priceSortedNames = new ArrayList<>();
                                        for (Bike bike : priceSorted) {
                                            String format = "%s | $%.2f | Quantity: %d";
                                            priceSortedNames.add(String.format(format, bike.getModelName(), bike.getPrice(), bike.getQuantity()));
                                        }
                                        writer.println(priceSortedNames);
                                        writer.flush();
                                        priceSortedNames.clear();
                                        break;
                                    case -1:
                                        repeat1 = 0;
                                        break; // go back to main menu
                                    case -4: // search
                                        String searchTerm = reader.readLine();
                                        ArrayList<Bike> matches = search(searchTerm, UserInfo.getBikes());
                                        ArrayList<String> matchNames = new ArrayList<>();
                                        if (matches != null) {
                                            for (Bike bike : matches) {
                                                String format = "%s | $%.2f | Quantity: %d";
                                                matchNames.add(String.format(format, bike.getModelName(), bike.getPrice(), bike.getQuantity()));
                                            }
                                            writer.println(matchNames);
                                            writer.flush();
                                            matchNames.clear();
                                        } else {
                                            writer.println(-1);
                                            writer.flush();
                                        }
                                        break;
                                }
                            } while (repeat1 == 1);
                            break;
                        case 2: // main menu option 2: view cart
                            runShoppingCart(reader, writer, thisBuyer);  //runs the shopping cart
                            break;

                        case 3: // main menu option 3: export file with purchase history
                            String filePath = reader.readLine();
                            String username = reader.readLine();
                            boolean success = getPurchaseHistory(filePath, username);
                            writer.println(success);
                            writer.flush();
                            break;
                        case 4: // logout -- do we need processing on the server side for this?
                            repeat = 0;
                            break;
                        case 5: // main menu option 5: delete account
                            String confirm = reader.readLine();
                            String user = reader.readLine();
                            int buyerIndex = Integer.parseInt(reader.readLine());
                            boolean deleted = deleteAccount(confirm, user, buyerIndex);
                            writer.println(deleted);
                            writer.flush();
                            break;
                    }
                }
            }
        } catch (IOException ioe) {

        }
    }

    public static ArrayList<Bike> sortByPrice(ArrayList<Bike> bikes) {
        ArrayList<Bike> sorted = new ArrayList(bikes.size());
        for (int i = 0; i < bikes.size(); i++) {
            sorted.add(bikes.get(i));
        }
        for (int i = 0; i < bikes.size() - 1; i++) {
            Bike thisBike = sorted.get(i);
            Bike lowestBike = thisBike;
            int lowestIndex = i;
            double lowestPrice = lowestBike.getPrice();
            for (int j = i + 1; j < bikes.size(); j++) {
                Bike otherBike = sorted.get(j);
                double price = otherBike.getPrice();
                if (price < lowestPrice) {
                    lowestBike = otherBike;
                    lowestPrice = price;
                    lowestIndex = j;
                }
            }
            sorted.set(i, lowestBike);
            sorted.set(lowestIndex, thisBike);
        }
        return sorted;
    }

    public static ArrayList<Bike> sortByQuantity(ArrayList<Bike> bikes) {
        ArrayList<Bike> sorted = new ArrayList(bikes.size());
        for (int i = 0; i < bikes.size(); i++) {
            sorted.add(bikes.get(i));
        }
        for (int i = 0; i < bikes.size() - 1; i++) {
            Bike thisBike = sorted.get(i);
            Bike lowestBike = thisBike;
            int lowestIndex = i;
            double lowestQuantity = lowestBike.getQuantity();
            for (int j = i + 1; j < bikes.size(); j++) {
                Bike otherBike = sorted.get(j);
                double quantity = otherBike.getQuantity();
                if (quantity < lowestQuantity) {
                    lowestBike = otherBike;
                    lowestQuantity = quantity;
                    lowestIndex = j;
                }
            }
            sorted.set(i, lowestBike);
            sorted.set(lowestIndex, thisBike);
        }

        return sorted;
    }

    public static ArrayList<Bike> search(String searchTerm, ArrayList<Bike> bikes) {
        ArrayList<Bike> matches = new ArrayList<Bike>();
        String term = searchTerm.toLowerCase();
        for (Bike bike : bikes) {
            if (bike.getModelName().toLowerCase().contains(term)) {
                matches.add(bike);
            } else if (bike.getSellerName().toLowerCase().contains(term)) {
                matches.add(bike);
            } else if (bike.getDescription().toLowerCase().contains(term)) {
                matches.add(bike);
            }
        }
        if (matches.size() == 0) {
            return null;
        } else {
            return matches;
        }
    }

    public static boolean getPurchaseHistory(String path, String username) {
        try {
            File file = new File(path);
            PrintWriter pw = new PrintWriter(file);
            Buyer thisBuyer = null;
            for (int i = 0; i < UserInfo.getBuyers().size(); i++) {
                if (username.equals(UserInfo.getBuyers().get(i).getUsername())) {
                    thisBuyer = UserInfo.getBuyers().get(i);
                }
            }
            ArrayList<PurchasedBike> purchasedBikes = thisBuyer.getPurchaseHistory();
            for (PurchasedBike b : purchasedBikes) {
                pw.println(b.toNiceString());
            }
            pw.flush();
            pw.close();
            return true;
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteAccount(String confirm, String user, int buyerIndex) {
        boolean deleted = false;
        ArrayList<Buyer> buyers = UserInfo.getBuyers();
        if (confirm.equals(user)) {
            buyers.remove(buyerIndex);
            UserInfo.setBuyers(buyers);
            deleted = true;
        }
        return deleted;
    }

    /*****
     * The following methods update shopping cart contents for the buyer and allow them to add/delete/checkout
     * items accordingly
     */

    //Methods
    public static void runShoppingCart(BufferedReader reader, PrintWriter writer, Buyer buyer) {
        //creates a Shopping Cart object to navigate the additional shopping cart methods in ShoppingCart.java
        ShoppingCart cart = new ShoppingCart(buyer);
        CustomerPageServer s = new CustomerPageServer();
        do {
            String input = ""; //stores the button input entered by the user of where they want to navigate to
            try {
                //waits for what button the user presses
                input = reader.readLine();
            } catch (Exception e) {
                System.out.println("Button error");
                return;
            }

            if (input.equals("add")) {

                s.addBike(reader, writer, cart);

            } else if (input.equals("delete")) {

                s.removeBike(reader, writer);  //TODO


            } else if (input.equals("checkout")) {

                s.checkout(writer); //TODO


            } else if (input.equals("backHome")) {
                return;

            }

        } while (true);

    }


    /*************
     * This method allows the buyer to add a bike to their shopping cart
     * @param reader
     * @param writer
     */
    public void addBike(BufferedReader reader, PrintWriter writer, ShoppingCart cart) {
        /*******
         * Saves the bikeID chosen by the user
         */
        //saves the bike ID retrieved from the server
        int bikeId = -1;

        try {
            bikeId = Integer.parseInt(reader.readLine());
        } catch (Exception e) {
            //e.printStackTrace();
            return;
        }


        /*******
         * Searches for the bike ID in the existing shopping cart. If it is already in the cart AND is on hte listing page
         * then simply request that the buyer add to the quantity they entered
         */
        boolean inCart = false;
        int bikeIndex = -1; //saves the bikeId that you want to add on to in the shopping cart
        int i = 0;
        for (PurchasedBike p : thisBuyer.getShoppingCart()) {
            if (p.getId() == bikeId) {
                inCart = true;
                bikeIndex = i;
                break;
            }
            i++;
        }
        writer.write("" + inCart);
        writer.println();
        writer.flush();


        if (inCart) {
            /****
             * Checks if the user entered a valid Bike Quantity to add on to the existing total
             */
            boolean validQuantity;
            String q = ""; //temporarily saves the quantity entered by the user and if valid converts it to an integer
            do {
                try {
                    q = reader.readLine();
                    System.out.println("User entered quantity " + q);
                } catch (Exception e) {
                    System.out.println("addBike method error under quantity");
                    return;
                }
                validQuantity = cart.checkBikeQuantity(q, bikeId, inCart, bikeIndex);
                System.out.println("Valid Quantity Server? " + validQuantity);
                writer.write("" + validQuantity);
                writer.println();
                writer.flush();
            } while (!validQuantity);
            int quantity = Integer.parseInt(q);  //saves the quantity entered if valid
            /******
             * Updates the current bike in the buyer's shopping cart
             */
            PurchasedBike tempBike = thisBuyer.getShoppingCart().get(bikeIndex);
            int existingQuantity = tempBike.getQuantity();

            //updates the quantity and purchasing price for the buyer
            ArrayList<PurchasedBike> tempShoppingCart = CustomerPageServer.thisBuyer.getShoppingCart();
            tempBike.setQuantity(existingQuantity + quantity);

            if (tempBike.isInsured()) {
                tempBike.setFinalPrice(tempBike.getPrice() * (existingQuantity + quantity) + 50.00);
            } else {
                tempBike.setFinalPrice(tempBike.getPrice() * (existingQuantity + quantity));
            }
            tempShoppingCart.set(tempShoppingCart.indexOf(tempBike), tempBike);
            thisBuyer.setShoppingCart(tempShoppingCart);


            //updates the entire buyer database
            ArrayList<Buyer> tempBuyers = UserInfo.getBuyers();
            tempBuyers.set(UserInfo.getBuyerIndex(thisBuyer), thisBuyer);
            UserInfo.setBuyers(tempBuyers);
            for (Buyer b : UserInfo.getBuyers()) {
                System.out.println(b.toString());
            }
            //lets the client know that it has been successfully added to the shopping cart
            writer.write("true");
            writer.println();
            writer.flush();


        } else if (!inCart) {

            /****
             * Checks if the user entered a valid Bike Quantity to add
             */
            boolean validQuantity;
            String q = ""; //temporarily saves the quantity entered by the user and if valid converts it to an integer
            do {
                try {
                    q = reader.readLine();
                    System.out.println("User entered quantity " + q);
                } catch (Exception e) {
                    System.out.println("addBike method error under quantity");
                    return;
                }
                validQuantity = cart.checkBikeQuantity(q, bikeId, inCart, bikeIndex);
                writer.write("" + validQuantity);
                writer.println();
                writer.flush();
            } while (!validQuantity);
            //saves the quantity entered
            int quantity = Integer.parseInt(q);

            double finalPrice; //saves the price (including quantity and insurance that the user wants to purchase a bike)


            /********
             * Checks if the user wants $50 insurance per bike added to their bike
             */
            Bike bikeToAdd = UserInfo.searchBike(bikeId);
            try {
                boolean insured = Boolean.parseBoolean(reader.readLine());
                System.out.println("Insured?" + insured);
                if (!insured) {
                    finalPrice = bikeToAdd.getPrice() * quantity;
                } else {
                    finalPrice = bikeToAdd.getPrice() * quantity + 50.00;
                }
                System.out.println("finalPrice " + finalPrice);

                /******
                 * Adds the purchased bike to the buyer's shopping cart
                 */
                PurchasedBike newPurchase = new PurchasedBike(bikeToAdd, finalPrice, insured);
                newPurchase.setQuantity(quantity); //sets the new purchase to a new quantity
                newPurchase.setFinalPrice(finalPrice);

                ArrayList<PurchasedBike> tempShoppingCart = thisBuyer.getShoppingCart();
                ArrayList<Buyer> tempBuyers = UserInfo.getBuyers();
                tempShoppingCart.add(newPurchase);
                thisBuyer.setShoppingCart(tempShoppingCart);
                tempBuyers.set(UserInfo.getBuyerIndex(thisBuyer), thisBuyer);
                UserInfo.setBuyers(tempBuyers);


                for (Buyer b : UserInfo.getBuyers()) {
                    System.out.println(b.toString());
                }

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

    /**********
     * This method first checks if all the bikes in the shopping cart are still available on the listing page. If this is true,
     * then all the bikes in the shopping cart are put into the purchase history and the quantity remaining of the bikes for sale
     * (the bikes on the listing page and in the seller inventories) is updated accordingly. If this is false, then an error
     * message is displayed
     * @param writer write to the client that the checkout has or has not successfully taken place
     * @author Christina Joslin
     */
    public void checkout(PrintWriter writer) {
        /********
         * Checks if all the bikes in the shopping cart still exist on the listing page
         */
        boolean stillAvailable = true; //saves whether or not all of the bikes are available for purchase
        int bikeEquivalentIndex = -1; //saves the index of the bike on the listing page corresponding to the bike in the shopping cart

        for (PurchasedBike pb : CustomerPageServer.thisBuyer.getShoppingCart()) {
            //Checks if the bike is in the listing page
            for (Bike b : UserInfo.getBikes()) {
                System.out.println(b.getId());
                System.out.println(pb.getId());
                if (pb.getId() != b.getId()) {
                    //System.out.println("Bike is not available on listing page");
                    stillAvailable = false;
                } else {
                    stillAvailable = true;
                    /*****
                     * If the quantity of this bike in the listing page equals zero, then remove it from the listing page altogether
                     * and return stillAvailable as false
                     */
                    if (b.getQuantity() == 0) {
                        System.out.println("Bike equivalent equals 0");
                        stillAvailable = false;
                        break;
                    }
                    if (b.getQuantity() < pb.getQuantity()) {
                        System.out.println("Bike quantity is less than purchase quantity");
                        stillAvailable = false;
                        break;
                    }

                }
                if (stillAvailable) {
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
             * Updates the listing Page and seller inventory
             */
            ArrayList<Bike> tempBikes = UserInfo.getBikes();
            for (Bike bike : tempBikes) {
                for (PurchasedBike pb : CustomerPageServer.thisBuyer.getShoppingCart()) {
                    System.out.println("Purchased bike ID " + pb);
                    if (pb.getId() == bike.getId()) {
                        bike.setQuantity(bike.getQuantity() - pb.getQuantity());
                    }
                }
            }
            /*****
             * If any bike quantity are equal to 0 then remove them from the listing page
             *
             */
            Iterator<Bike> itr = tempBikes.iterator();
            for (Iterator<Bike> it = itr; it.hasNext(); ) {
                Bike i = it.next();
                if (i.getQuantity() == 0) {
                    tempBikes.remove(i);
                }
            }
            UserInfo.setBikes(tempBikes);

            System.out.println("Bike Listing Page - Post Checkout");
            for (Bike b : UserInfo.getBikes()) {
                System.out.println(b.toString());
            }

            /*******
             * Moves everything in the shopping cart to the purchase history
             */
            ArrayList<PurchasedBike> tempPurchaseHistory = thisBuyer.getPurchaseHistory();
            ArrayList<PurchasedBike> tempShoppingCart = thisBuyer.getShoppingCart();

            int len = thisBuyer.getShoppingCart().size();
            for (int i = 0; i < len; i++) {
                tempPurchaseHistory.add(tempShoppingCart.get(0));
                tempShoppingCart.remove(0);

            }
            thisBuyer.setShoppingCart(tempShoppingCart);
            thisBuyer.setPurchaseHistory(tempPurchaseHistory);
            ArrayList<Buyer> tempBuyers = UserInfo.getBuyers();
            tempBuyers.set(UserInfo.getBuyerIndex(thisBuyer), thisBuyer);
            UserInfo.setBuyers(tempBuyers);
            /********
             * Removes any bike with a quantity of 0 from the seller inventory
             */
            ArrayList<Seller> tempSellers = UserInfo.getSellers();
            for (Seller s : tempSellers) {

                ArrayList<Bike> tempInventory = s.getInventory();
                Iterator<Bike> temp = tempInventory.iterator();
                for (Iterator<Bike> tp = temp; tp.hasNext(); ) {
                    Bike bike = tp.next();
                    if (bike.getQuantity() == 0) {
                        tempInventory.remove(bike);
                    }
                }
                s.setInventory(tempInventory);
            }
            UserInfo.setSellers(tempSellers);

            /****
             * Testing only.Printing out the current bikes on the listing page
             */
            System.out.println("Bikes in the Listing Page");
            for (Bike b : UserInfo.getBikes()) {
                System.out.println(b.toNiceString());
            }
            /**********
             * Testing only. Printing out the current seller's inventory
             */
            System.out.println("Bikes in Seller Inventory");
            for (Seller s : UserInfo.getSellers()) {
                System.out.println(s.toString());
            }
            //once it has completed the saving process send the message of success to the buyer
            writer.write("true");
            writer.println();
            writer.flush();
        }
    }

    /************
     * This method allows the buyer to remove bikes from their current shopping cart.
     * @param reader the user entered bikeID
     * @param writer whether or not the shopping cart item was successfully removed
     * @author Christina Joslin
     */
    public void removeBike(BufferedReader reader, PrintWriter writer) {
        /*******
         * Saves the bikeID chosen by the user
         */
        //saves the bike ID retrieved from the server
        int bikeId = -1;

        try {

            bikeId = Integer.parseInt(reader.readLine());
            System.out.println(bikeId);

        } catch (Exception e) {
            System.out.println("Unable to parse int");
            //e.printStackTrace();
            return;
        }
        /*******
         * Removes the designated bike from the shopping cart
         */
        ArrayList<PurchasedBike> tempShoppingCart = thisBuyer.getShoppingCart();
        Iterator<PurchasedBike> iterator = tempShoppingCart.iterator();
        for (Iterator<PurchasedBike> it = iterator; it.hasNext(); ) {
            PurchasedBike pBike = it.next();
            if (pBike.getId() == bikeId) {
                tempShoppingCart.remove(pBike);
                break;
            }
        }
        /********
         * Updates the buyer shopping cart and the buyer database accordingly
         */
        thisBuyer.setShoppingCart(tempShoppingCart);
        ArrayList<Buyer> tempBuyers = UserInfo.getBuyers();
        tempBuyers.set(UserInfo.getBuyerIndex(thisBuyer), thisBuyer);
        UserInfo.setBuyers(tempBuyers);

        /**
         * Testing only prints the current shopping cart of thisBuyer
         */
        for (PurchasedBike b : thisBuyer.getShoppingCart()) {
            System.out.println(b.toNiceString());
        }
        //sends a message to the client confirming the successful deletes
        writer.write("true");
        writer.println();
        writer.flush();

    }


}
