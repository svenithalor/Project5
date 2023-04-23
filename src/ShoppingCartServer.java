import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**************
 *This
 */
public class ShoppingCartServer {
    private ArrayList<PurchasedBike> shoppingCart; //a copy of the buyer's current shopping cart
    private ArrayList<PurchasedBike> purchaseHistory; //a copy of the buyer's current purchase history
    private ArrayList<Bike> bikesForSale; //a copy of the bikes available on the listing page
    private Buyer buyer; //the buyer using this shopping cart

    //Constructs the customer page and initializes an arraylist of bike
    public ShoppingCartServer(Buyer buyer) {
        this.buyer = buyer;
        this.shoppingCart = buyer.getShoppingCart();
        this.purchaseHistory = buyer.getPurchaseHistory();
        this.bikesForSale = UserInfo.getBikes();
    }


    /*****
     * This method searches the available bikes for the one with the given id number. It creates a new PurchasedBike
     * and adds it to the ShoppingCart arraylist if a sufficient quantity of the desired Bike is available.
     *
     * @param id id of the cart being added
     * @param insurance whether the user wants to buy insurance
     * @param quantity how many of the bike the user wants to buy
     */
    public void addToCart(int id, int quantity, int insurance) {

        //TODO: we will need to edit this accordingly...for instance we still neeed to ask if they want bike insurance
        //and integrate network i/o into this
        boolean alreadyInCart = false;
        for (PurchasedBike pb : shoppingCart) {
            if (pb.getId() == id) {
                pb.setQuantity(pb.getQuantity() + quantity);
                alreadyInCart = true;
            }
        }
        if (!alreadyInCart) {
            Bike bikeToAdd = null;
            for (Bike bike : UserInfo.getBikes()) {
                if (bike.getId() == id) {
                    bikeToAdd = bike;
                }
            }
            if (bikeToAdd == null || bikeToAdd.getQuantity() < quantity) {
                System.out.println("Error: unavailable");
            } else {
                double purchasePrice = quantity * bikeToAdd.getPrice();
                boolean insured = false;
                if (insurance == 1) {
                    insured = true;
                    purchasePrice += 50.0;
                }
                bikeToAdd.setQuantity(quantity);
                PurchasedBike newPurchase = new PurchasedBike(bikeToAdd, purchasePrice, insured);
                shoppingCart.add(newPurchase);
                System.out.println("Added to cart!");
            }
        }
    }


    /*****
     * This method iterates through the shopping cart and adds all of them to purchased bikes. It also updates the list
     * of available bikes by searching for the corresponding bike based on id and reducing the quantity by the amount
     * being purchased. ONLY if the quantities are valid
     */
    public void checkout() {
        for (Bike b : bikesForSale) {
            for (PurchasedBike pb : shoppingCart) {
                if (pb.getId() == b.getId()) {
                    b.setQuantity(b.getQuantity() - pb.getQuantity());
                }
            }
        }
        for (Seller se: UserInfo.getSellers()) {
            for (Bike b : se.getInventory()) {
                for (PurchasedBike pb : shoppingCart) {
                    b.setQuantity(b.getQuantity() - pb.getQuantity());
                }
            }
        }
        // dumping everything from shopping cart to purchased bikes
        int len = shoppingCart.size();
        for (int i = 0; i < len; i++) {
            shoppingCart.add(shoppingCart.get(0));
            shoppingCart.remove(0);

            buyer.setShoppingCart(shoppingCart);
        }

        System.out.println("Checked out successfully.");
    }


    /******
     * need a description for this method and when a
     *
     * @param id
     */
    public void removeBike(int id) {
        //need to integrate this into our existing methods
        PurchasedBike bikeToRemove = null;
        for (PurchasedBike bike : shoppingCart) {
            if (bike.getId() == id) {
                shoppingCart.remove(bike);
                bikeToRemove = bike;
                System.out.println("Bike removed!");
                break;
            }
        }
        if (bikeToRemove == null) {
            System.out.println("No bike found!");
        } else {
            Bike bike = new Bike(bikeToRemove);
            bikesForSale.add(bike);
            UserInfo.setBikes(bikesForSale);
        }
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
    public boolean checkBikeID(String input,String buttonType) {
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
                for (Bike b: UserInfo.getBikes()) {
                    if (b.getId() == bikeId) {
                        return true;
                    }
                }
                break;
            case "delete":
                for (PurchasedBike pb: buyer.getShoppingCart()) {
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
                    /*******
                     * Checks if the user entered a valid Bike ID or not
                     */
                    boolean validId = false;
                    do {
                        String d = reader.readLine();
                        //Sample Buyer for now...
                        validId = s.checkBikeID(d,"add");
                        //lets the client know if the user input is valid or not
                        writer.write("" + validId);
                        writer.println();
                        writer.flush();

                    } while (!validId);


                } else if (input.equals("delete")) {

                    /********
                     * Checks if the user entered a valid Bike ID or not
                     */
                    boolean validInput = false;
                    do {
                        String d = reader.readLine();
                        //Sample Buyer for now...
                        validInput = s.checkBikeID(d,"delete");
                        //lets the client know if the user input is valid or not
                        System.out.println(validInput);
                        writer.write("" + validInput);
                        writer.println();
                        writer.flush();

                    } while (!validInput);

                    //do something

                } else if (input.equals("checkout")) {


                    //do something

                } else if (input.equals("backHome")) {

                    //do something

                } else if (input.equals("refresh")) {

                    //do something

                }


            } while (true);


        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}
