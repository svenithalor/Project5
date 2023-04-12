/********************
 * The PurchasedBike class creates a purchased Bike to be placed in the shopping cart and the purchasing history
 * @author Christina Joslin, lab sec 4427
 * @version 4/7/2023
 */
public class PurchasedBike extends Bike {
    private boolean insured; //determines whether or not the bike is under bike-in-a-tree insurance
    private double finalPrice; //stores the final price of a purchased bike


    //Constructors

    public PurchasedBike(String color, int wheelSize, double price, double finalPrice, String modelName, boolean used,
                         String description, String sellerName, int quantity, boolean insured,int id) {
        super(color, wheelSize, price, modelName, used, description, sellerName, quantity, id);
        this.insured = insured;
        this.finalPrice = finalPrice;
    }

    public PurchasedBike(Bike bike, double finalPrice, boolean insured) {
        super(bike.getColor(), bike.getWheelSize(), bike.getPrice(), bike.getModelName(), bike.isUsed(),
                bike.getDescription(), bike.getSellerName(), bike.getQuantity(), bike.getId());
        this.insured = insured;
        this.finalPrice = finalPrice;

    }

    /*********
     * Returns whether or not this purchased bike is insured under bike-in-a-tree insurance
     * @return the insurance status of this purchased bike
     */
    public boolean isInsured() {
        return insured;
    }

    /***************
     * Displays general information about this purchased bike to the user to be displayed in the shopping cart including
     * its model name, color, wheel size, price, seller name, if it is used, and if it is insured under bike-in-a-tree
     * insurance
     */
    public void viewPurchasedBikeInfo() {
        String summary; //stores the summary of the bike info (50 characters of less) to be displayed in the shopping
        // cart and listing page
        //if description more than 50 characters, then abridge the content
        if (getDescription().length() > 50) {
            summary = getDescription().substring(0, 51);

        } else {
            summary = getDescription();
        }
        String message = String.format("%s (%s) %d-inch%n$%.2f   %s%nUsed:%b   Quantity:%d   Insured:%b%nDescription: "
                        + "%s",
                getModelName(), getColor(), getWheelSize(), getPrice(), getSellerName(), isUsed(),
                getQuantity(), isInsured(), summary);
        System.out.println(message);

    }



    /***********
     * This method is used to read and write in information about purchased bikes on Boilermaker Bikes
     * @return string containing the parameters for a purchasedBike object
     *
     * Format:
     *
     * color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured,id
     */
    @Override
    public String toString() {
        String message = String.format("%s,%d,%.2f,%.2f,%s,%b,%s,%s,%d,%b,%d", getColor(), getWheelSize(), getPrice(),
                finalPrice, getModelName(), isUsed(), getDescription(), getSellerName(), getQuantity(), insured,
                getId());
        return message;
    }
}

