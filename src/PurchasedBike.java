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
                         String description, String sellerName, int quantity, boolean insured, int id) {
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
     * Returns the purchasing price of the bike when it is put in the shopping cart
     * @return the final price of this bike which includes price per bike * quantity + insurance (if applicable)
     */
    public double getFinalPrice() {
        return finalPrice;
    }

    /*******
     * Updates the final price of the bike when it is put in the shopping cart
     * @param finalPrice of this bike which includes price per bike * quantity + insurance (if applicable)
     */
    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    /*********
     * Returns whether or not this purchased bike is insured under bike-in-a-tree insurance
     * @return the insurance status of this purchased bike
     */
    public boolean isInsured() {
        return insured;
    }


    /***********
     * This method is used to read and write in information about purchased bikes in UserInfo.java
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

    public String toNiceString() {
        return super.toNiceString();
    }


    /******
     * This method is used to display the model name, final price (quantity * price per bike + quantity * $50 if
     * applicable, wheel size, and quantity to be purchased and or that has already been purchased and put in the
     * shopping cart/purchasing history
     * @return message containing the key attributes of a bike in a buyer's shopping cart or purchase history
     */
    public String shoppingCartToString() {
        String message = String.format("Name %s | Price: %.2f | Wheel size: %d | Quantity: %d", getModelName(), getFinalPrice(), getWheelSize(), getQuantity());
        return message;
    }
}
