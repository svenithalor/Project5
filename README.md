# Project 5 - Boilermaker Bikes 
Author: Christina Joslin 

## Instructions (from Intellij IDEA) 

1) All threads will be generated from the main method in ControlFlowMenu.java. To run this program for multiple users at the same time, click on the ControlFlowMenu.java class, go to the configuration icon on the top right corner of your screen, click "edit configurations", "modify options", and then select "allow multiple instances." 

2) Now that you have set up the multiple instances feature, run the main method in ControlFlowMenu.java. A JOptionPane will appear and you will be prompted if you are a buyer or seller. From there, you will enter your existing login information and/or create a new account. 

3) A JOptionPane stating that you logged in successfully will appear and from there you can navigate across the buyer/seller pages accordingly. 


**********
## Submission Details
Student 1 - Submitted report on Brightspace
Student 2 - Submitted vocareum workspace 
**********
## Description 

### UserInfo.java 
*Functionality* This class stores user information reads past user information and writes current user information into buyer.csv or seller.csv 

*Relationship To Other Classes* This class contains the buyers, sellers, and bikes arraylists which are accessed and frequently updated by the customer page client/server as well as the seller page client/server. 

*Fields* 
private static ArrayList<Buyer> buyers; //stores all previous and new buyers 
private static ArrayList<Seller> sellers; //stores all previous and new sellers 
private static ArrayList<Bike> bikes; //stores all bikes that are for sale 

 *Methods* 
 
 synchronized public static void readUsers()
 **Description** This method reads past user information and stores each user into either the buyer arraylist or the seller arraylist. If the user is a seller, then the bikes associated with their account will be put into the bikes arraylist to be made available to the customers
 
synchronized public static void writeUsers()
**Description** This method writes existing user information into both buyer.csv and seller.csv
 
synchronized public static void setBuyers(ArrayList<Buyer> buyers)
**Description** Updates the buyers database
@param buyers of Boilermaker Bikes
 
 public static ArrayList<Buyer> getBuyers()
 **Description** returns the buyers database 
 @return buyers of Boilermaker Bikes 
 
 synchronized public static void setSellers(ArrayList<Buyer> sellers)
**Description** Updates the sellers database
@param sellers of Boilermaker Bikes
 
 public static ArrayList<Bike> getBikes()
 **Description** Returns all the available bikes for sale
 @return the available bikes for sale
 
 synchronized public static void setBikes(ArrayList<Bike> bikes)
 **Description** Updates the available bikes for sale
 @param bikes available for sale
 
 public static ArrayList<Seller> getSellers()
 **Description** returns the sellers database 
 @return sellers of Boilermaker Bikes 
 
 
 
 
 --------------------------------------------
### ControlFlowMenu.java
*Functionality* The ControlFlowMenu class allows the user to navigate to the buyer or seller pages on the Boilermaker Bikes website

*Relationship To Other Classes* This class is accessed via the ControlFlowMenu. 

*Fields* 
private static ArrayList<Buyer> buyers = new ArrayList<Buyer>();
private static ArrayList<Bike> bikes = new ArrayList<>(); //stores the bikes t
that are going to be included on the listing page

*Methods* 
 None except for the main method
 
------------------------------------------

### Bike.java
*Functionality* This class creates bike objects to be used by both the buyer and the seller.
*Relationship To Other Classes*  This class is a superclass of PurchasedBike.java 

*Fields* 
private String color; //the color of this bike
private int wheelSize; //the wheel size of this bike
private double price; //the price of this  bike
private String modelName; //the model Name of this bike
private boolean used; //states if this  bike has been used or not
private String description; //the description of this bike
private String sellerName; //the seller username associated with this bike
private int quantity; //the quantity of this bike
private int id; //the id of this bike

*Constructors*
public Bike(String color, int wheelSize, double price, String modelName, boolean used, String description, String sellerName, int quantity,int id)

public Bike(Bike b)

public Bike(PurchasedBike b)


*Methods* 

public String getColor()    
**Description** returns the color of this bike 

public int getWheelSize()
**Description** returns the wheel size of this bike 

public double getPrice()   
**Description** returns the price of this bike 

public String getModelName()
**Description** returns the model name of this bike

public boolean isUsed()
**Description** returns if the bike is used or not 

public String getDescription()  
**Description** returns the description of this bike 

public String getSellerName()
**Description** returns the seller name of this bike 

public String getWheelSize()
**Description** returns the quantity available of this bike on the listing page 

public int getQuantity()
**Description** returns the price of this bike 

public int getId()
 **Description** updates the unique id of this bike 

public void setQuantity(int quantity)
**Description** updates the quantity available on the listing page of this bike 
@param quantity available on the listing page of this bike 


public String toString() 
**Description** This method is used to read and write in information about specific bikes on Boilermaker Bikes
@return string containing the parameters for a Bike object


public String toNiceString()
**Description*** This method returns a string with the essential attributes of the bike for the user's readability 
@return String containing only some attributes, with spacing, etc. 


-------------------------------------------------------------------------------------------
### PurchasedBike.java
*Functionality* This class creates a purchased Bike to be placed in the shopping cart and the purchase history of a buyer

*Relationship To Other Classes* This class is a subclass of Bike.java

*Fields*

private boolean insured; //determines whether or not the bike is under bike-in-a-tree insurance
private double finalPrice; //stores the final price of a purchased bike
   
*Constructors*

public PurchasedBike(String color, int wheelSize, double price, double finalPrice, String modelName, boolean used, String description, String sellerName, int quantity, boolean insured,int id)

public PurchasedBike(Bike bike, double finalPrice, boolean insured)
   
*Methods*
public double getFinalPrice()
**Description** Returns the purchasing price of the bike when it is put in the shopping cart
@return the final price of this bike which includes (price per bike + insurance) * quantity (if applicable)

public void setFinalPrice(double finalPrice)
**Description** Updates the final price of the bike when it is put in the shopping cart
@param finalPrice of this bike which includes (price per bike + insurance) * quantity (if applicable)

public boolean isInsured()
**Description** Returns whether or not this purchased bike is insured under bike-in-a-tree insurance
@return the insurance status of this purchased bike

public String toNiceString()
**Description** This method is used to display bikes in the user's purchase history to the seller and calls the superclass version of .toNiceString(); 

public String toString()
**Description** This method is used to read and write in information about purchased bikes on Boilermaker Bikes
@return string containing the parameters for a purchasedBike object

public String shoppingCartToString()
**Description** This method is used to display the unique bike id, model name, final price, wheel size, and quantity to be purchased and or that has already been purchased and put in the shopping cart/purchasing history
@return message containing the key attributes of a bike in a buyer's shopping cart or purchase history

----------------------------------------------------------------------
### Buyer.java
*Functionality* This class initializes the attributes and methods specific to a buyer  
*Relationship To Other Classes* It creates buyer objects which interact with the platform via the Control Flow Menu and the Customer Page.

*Fields*
private ArrayList<PurchasedBike> shoppingCart = new ArrayList<>(); //shopping cart where the user can store their purchase bikes
private ArrayList<PurchasedBike> purchaseHistory = new ArrayList<>(); //list of bikes purchased by the user
private String username; //stores the username of this buyer
private String password; //stores the password of this buyer
 
*Constructor*
public Buyer(String username, ArrayList<PurchasedBike> shoppingCart, ArrayList<PurchasedBike> purchaseHistory)
 
*Methods*
public String getUsername()
**Description** Gets the user's username
@return the user's username
 
public String getPassword()
**Description** Get the user's password
@return the user's password 
  
public ArrayList<PurchasedBike> getShoppingCart()
**Description** Returns the shopping cart of this buyer
@return the Shopping Cart of this buyer
 
public void setShoppingCart(ArrayList<PurchasedBike> shoppingCart)
**Description** Updates the shopping cart of this buyer
@return the shoppingCart of this buyer 
  
  
public ArrayList<PurchasedBike> getPurchaseHistory()
**Description** Returns the purchase history of this buyer
@return the purchase history of this buyer
 
public void setShoppingCart(ArrayList<PurchasedBike> shoppingCart)
**Description** Updates the shopping cart of this buyer
@param shoppingCart of this buyer
 
public void setPurchaseHistory(ArrayList<PurchasedBike> purchaseHistory)
**Description** Updates the purchase history of this buyer
@param purchaseHistory of this buyer
 
public String toString()
**Description** This method is used to import/export buyer information in Login.java in the following format
username: [insert name]
password: [insert 5 characters only]
[insert name].shoppingcart color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured
[insert name].purchasehistory color,wheelSize,price,finalPrice,modelName,used,description,sellerName,quantity,insured
 
--------------------------------------

### Seller.java
*Functionality* This initializes the attributes and methods specific to a seller in the Boilermaker Bikes platform
 
*Relationship To Other Classes* It allows the user to access the Customer Page,Seller Page, and Login classes. 
 
*Fields* 
private ArrayList<Bike> inventory = new ArrayList<>();  //stores the inventory of a specific seller
private String username; //stores the username of this seller
 
*Constructor* 
 public Seller(String username, String password, ArrayList<Bike> inventory)
  
 *Methods* 
 
 public String getUsername()
 **Description** 
 Gets the user's username
 @return the user's username
  
 public String getPassword()
  **Description** 
  Returns the user's password
  @return the user's password 
 
public ArrayList<Bike> getInventory()
**Description** 
Returns the inventory of this Seller
@return the inventory of this Seller
 
public void setInventory(ArrayList<Bike> inventory)
 **Description**
 updates the inventory of this Seller
 @param inventory of this Seller
 
 
public String toString()
**Description** 
 This method is used to import/export seller information in Login.java
 
--------------------------
 ### CustomerPage.java
*Functionality* This class creates eleven functionalities for buyers to have and use. There are eleven methods: search(String searchTerm); sortByPrice(); sortByQuantity(); viewAvailableBikes(); addToCart(int id); viewCart(); checkout(); removeBike(int id); getPurchaseHistory(String fileName); deleteAccount(String user); updateListings(ArrayList<Bike> bikes)

*Relationship To Other Classes* This class is accessed via the ControlFlowMenu.
 
*Fields*
private ArrayList<Bike> bikes;
private ArrayList<PurchasedBike> shoppingCart;
private ArrayList<PurchasedBike> purchasedBikes;
private Buyer buyer;
 
*Constructor*
public CustomerPage(ArrayList<Bike> bikes, Buyer buyer)
 
*Methods*
public ArrayList<Bike> open(Buyer buyer)
 **Description** This method outlines the main customer user experience. It navigates through a series of options and the user can keep selecting options until they decide to exit. At the end of this method, the buyer object is updated with changes and the method returns the updated arraylist of bikes.
@return Updated arraylist of available bikes
 
public void search(String searchTerm)
**Description** This method searches each bike's description and name for a match based on the given search term. It stores the
matches in an arraylist and then prints them out.
@param searchTerm : user-provided search term to look for
 
public void sortByPrice()
**Description** This method sorts the arraylist of available bikes from lowest to highest price. It prints out the sorted array.
 
public void sortByQuantity()
**Description** This method sorts the arraylist of available bikes from lowest to highest available quantity. It prints out the sorted array.

public void viewAvailableBikes()
**Description** This method prints out all the available bikes.

public void addToCart(int id, int quantity, int insurance)
**Description** This method searches the available bikes for the one with the given id number. It creates a new PurchasedBike
and adds it to the ShoppingCart arraylist if a sufficient quantity of the desired Bike is available.
@param id id of the cart being added
@param insurance whether the user wants to buy insurance
@param quantity how many of the bike the user wants to buy
 
public void viewCart()
**Description** This method prints out all the bikes in the shopping cart
 
public void checkout()
**Description** This method iterates through the shopping cart and adds all of them to purchased bikes. It also updates the list
of available bikes by searching for the corresponding bike based on id and reducing the quantity by the amount
being purchased.
 
public void removeBike(int id)
**Description** This method iterates through the shoppingCart and removes the bike with the given ID from the shopping cart.
@param id the id of the bike to be removed
 
public void getPurchaseHistory(String fileName)
**Description** This method iterates through the PurchasedBikes and exports info for each one into a file with the given name
* @param fileName User-specified name of the file to add to
 
public void deleteAccount(String user)
**Description** This method removes the user from the list of buyers stored in Login
 @param user username of account to delete
 
public void updateListings(ArrayList<Bike> bikes)
**Description** This method updates available bikes
@param bikes updated arraylist of available bikes
 
 -------------------------------------
### Login.java
*Functionality* This class creates the login and logout functionalities of the Boilermaker Bikes websites, reads information from either buyer.txt or seller.txt and then writes it back into buyer.txt or seller.txt. The methods included are the intialSetup(int usertype *buyer or seller*) which initially reads through either the buyer.txt or the seller.txt file (depending on the usertype), parses through each line and creates buyer objects, and then from there writes those buyer objects into a buyers arraylist or a seller arraylist "database". The final two methods are the userLogin(scanner,usertype *buyer or seller*) which based on user input either has the user sign into their existing account or create a a new account and the userLogout(userType) which is accessed through the ControlFlowMenu.java and the SellerPage.java or the customerPage.java to write all of the buyer or seller objects in the arraylist back into the seller.txt file or the buyer.txt file. This was tested using the TestClassLogin.java which contains a main method and using login and logout reprints the seller.txt and buyer.txt sample files.

*Relationship To Other Classes* This class is accessed via the ControlFlowMenu.

*Fields*
private static ArrayList<Buyer> buyers = new ArrayList<Buyer>();
 //keeps track of all the buyers on the bicycle website
 
private static ArrayList<Seller> sellers = new ArrayList<Seller>();
//keeps track of all the sellers on the bicycle website
 
*Methods*
 
public static void initialSetup(int userType)
**Description** This method iterates through a file containing all buyer or seller information and stores it into the buyer and seller arraylists.
@param userType determines whether the buyer.txt or the seller.txt file will be iterated through

public int userLogin(Scanner scanner, int userType)
**Description** This method logs the user into the Boiler Bikes website and or has them create a new account
@param scanner the username to be enterred by the user
@param userType the type of user logging in (a buyer or seller)
 
public void userLogout(int userType)
**Description** This method logs the user out of the application and saves their information to a file
@param userType the user that is logging our whether a buyer or seller
 
public static void setBuyers(ArrayList<Buyer> buyers)
**Description** This method sets the buyer from the arraylist
@param buyer from the arraylist

public static ArrayList<Buyer> getBuyers()
**Description** Returns the buyer of this ArrayList
@return the buyer of this ArrayList
 
public static void setSellers(ArrayList<Seller> sellers)
**Description** This method sets the seller from the arraylist
@param seller from the arraylist
 
public static ArrayList<Seller> getSellers()
**Description** Returns the seller of this arraylist
@return the seller of this arraylist
 
 ----------------------------------
 ### SellerPage.java
*Functionality* This class creates six functionalities for sellers to have and use. There are six methods: displayBikes() to get the current inventory of bikes with their properties of names, prices, wheel sizes, colors and qunatities; addBike(Bike b) to add a bike(as parameter) to the seller inventory; removeBike(Bike b) to remove a bike(as parameter) to the seller inventory; searchBike(String term) to search for a bike in the seller inventory based on a keyword(paramter); deleteAccount(String user) to delete a user's account from the shop; viewCustomerCarts(String filename) to show the items with all properties of a user's purchasehistory. These methods all help withe main sellerpage method runSellerPage(Seller seller) to run these methods.

*Relationship To Other Classes* This class is accessed via the ControlFlowMenu.
 
*Fields*
 
private String name;
private ArrayList<Bike> inventory;
 
*Constructors*
 
public SellerPage(String name, ArrayList<Bike> inventory)
 
*Methods*
 
public ArrayList<Bike> runSellerPage(Seller seller)
**Description** This method outlines the main sller user experience. It navigates through a series of options and the user
can keep selecting options until they decide to exit. At the end of this method, the slelr object is updated with
changes and the method returns the updated arraylist of bikes.
@return Updated arraylist of available bikes
 
public void displayBikes()
**Description** This method displays a bike with its characteristics

public void addBike(Bike b)
**Description** This method adds a bike to the seller inventory
@param b the bike to be added to the seller inventory
 
public void removeBike(Bike b)
**Description** This method removes bikes from the seller inventory
@param b the bike to be removed from the inventory

public void searchBike(String term)
**Description** is method searches for a bike in the seller inventory based on a keyword
@param term the term
 
public void deleteAccount(String user)
**Description** This method removes all the information of the user from the arraylist
@param name of the user to be deleted
 
public void viewCustomerCarts(String filename)
**Description** Displays general information about the file of the user to be displayed in the shopping cart including
its model name, color, wheel size, price, seller name, if it is used, and if it is insured under bike-in-a-tree insurance 
@param name of the file to be displayed in the shopping cart 
