/*********************
 * This class contains methods used to check bike quantity and
 */
public class ShoppingCart {
    private Buyer thisBuyer; //the buyer currently navigating the buyer shopping cart

    //Constructor
    public ShoppingCart (Buyer buyer) {
        this.thisBuyer = buyer;

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
            purchaseQuantity += thisBuyer.getShoppingCart().get(cartIndex).getQuantity();
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
                for (PurchasedBike pb : thisBuyer.getShoppingCart()) {
                    if (pb.getId() == bikeId) {
                        return true;
                    }
                }
                break;

        }
        return false;
    }
}
