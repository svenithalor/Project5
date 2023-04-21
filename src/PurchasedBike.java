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

    /***************
     * Displays general information about this purchased bike to the user to be displayed in the shopping cart including
     * its model name, color, wheel size, price, seller name, if it is used, and if it is insured under bike-in-a-tree
     * insurance
     * @return String[] array message containing the attributes of a purchased bike
     */
    public Object[][] shoppingCartInfo() {
        //three things, bike ID, bike name, final price, and quantity to be purchased
        Object [][] items = new Object[][];


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
}

