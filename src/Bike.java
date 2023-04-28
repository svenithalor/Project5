/***************
 * The Bike class creates bikes to be added by the seller and displayed on the listing page
 * @author Christina Joslin, lab sec 4427
 * @version 4/7/2023
 */
public class Bike {

    private String color; //the color of this bike
    private int wheelSize; //the wheel size of this bike
    private double price; //the price of this  bike
    private String modelName; //the model Name of this bike
    private boolean used; //states if this  bike has been used or not
    private String description; //the description of this bike
    private String sellerName; //the seller username associated with this bike
    private int quantity; //the quantity of this bike
    private int id; //the 4 digit unique id of this bike

    public Bike(String color, int wheelSize, double price, String modelName, boolean used, String description, String sellerName, int quantity,int id) {
        this.color = color;
        this.wheelSize = wheelSize;
        this.price = price;
        this.modelName = modelName;
        this.used = used;
        this.description = description;
        this.sellerName = sellerName;
        this.quantity = quantity;
        this.id = id;
    }
    // copy constructor
    public Bike(Bike b) {
        this.color = b.getColor();
        this.wheelSize = b.getWheelSize();
        this.price = b.getPrice();
        this.modelName = b.getModelName();
        this.used = b.isUsed();
        this.description = b.getDescription();
        this.sellerName = b.getSellerName();
        this.quantity = b.getQuantity();
        this.id = b.getId();
    }

    public Bike(PurchasedBike b) {
        this.color = b.getColor();
        this.wheelSize = b.getWheelSize();
        this.price = b.getPrice();
        this.modelName = b.getModelName();
        this.used = b.isUsed();
        this.description = b.getDescription();
        this.sellerName = b.getSellerName();
        this.quantity = b.getQuantity();
        this.id = b.getId();
    }

    /******
     * returns the color of this bike
     * @return the color of this bike
     */
    public String getColor() {
        return color;
    }


    /********
     * returns the wheel size of this bike
     * @return the wheel size of this bike
     */
    public int getWheelSize() {
        return wheelSize;
    }

    /********
     * returns the price of this bike
     * @return the price of this bike
     */
    public double getPrice() {
        return price;
    }

    /***********
     * returns the model name of this bike
     * @return the model name of this bike
     */
    public String getModelName() {
        return modelName;
    }

    /***********
     * returns if this bike is used or not
     * @return if this bike is used or not
     */
    public boolean isUsed() {
        return used;
    }

    /***********
     * returns the description of this bike
     * @return the description of this bike
     */
    public String getDescription() {
        return description;
    }

    /*************
     * returns the username of the seller of this bike
     * @return the username of the seller of this bike
     */
    public String getSellerName() {
        return sellerName;
    }

    /********
     * returns the quantity of this bike
     * @return the quantity of this bike
     */
    public int getQuantity() {
        return quantity;
    }

    /*******
     * Updates the price of this bike
     * @param price of this bike
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**********
     * Updates the quantity of this bike
     * @param quantity of this bike
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /*********
     * Returns the unique id of this bike
     * @return id of this bike
     */
    public int getId() {
        return id;
    }


    /***************
     * Displays general information about this bike including the model name, wheel size, price, the seller name,
     * and the quantity available
     */
    public void viewBikeInfo() {
        String summary; //stores the summary of the bike info (50 characters of less) to be displayed in the shopping
        // cart and listing page
        //if description more than 50 characters, then abridge the content

        if (getDescription().length() > 50) {
            summary = getDescription().substring(0, 51);

        } else {
            summary = getDescription();
        }
        String message = String.format("%s %d-inch%n$%.2f   %s%nUsed:%b   %d remaining%nDescription: %s",
                getModelName(), getWheelSize(), getPrice(), getSellerName(), isUsed(),
                getQuantity(), summary);
        System.out.println(message);
    }

    /***********
     * This method is used to read and write in information about specific bikes on Boilermaker Bikes
     * @return string containing the parameters for a Bike object
     */
    @Override
    public String toString() {
        String message = String.format("%s,%d,%.2f,%s,%b,%s,%s,%d,%d", color, wheelSize, price, modelName,
                used, description, sellerName, quantity,id);
        return message;
    }

    /*******
     * This method returns a string with the essential attributes of the bike, for the user's readability
     * @return String containing only some attributes, with spacing, etc
     */
    public String toNiceString() {
        String vtr = String.format("ID: %d | Name: %s | Price: $%.2f | Wheel size: %d | Quantity: %d", id,modelName, price,
            wheelSize, quantity);
        return vtr;
    }
}

