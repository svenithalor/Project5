# Project 5 - Boilermaker Bikes  

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
 
 public static Bike searchBike(int bikeId)
 **Description** This method allows the user to search for the index of a bike they
 are looking for on the listing page
 @param bikeId the 4 digit unique bike id entered by the user
 @return the bike with the matching id entered
 
 public static int getBuyerIndex(Buyer buyer)
 **Description** This method allows for the user to get the index of a buyer they are looking for in the buyers database
 @param buyer navigating the customer page
 @return the index of the buyer they are looking for in the buyer database. If the buyer is not found, then return an index of -1
 
public static int getSellerIndex(String sellerName)
**Description** Returns index of master seller list given the Seller name String
 @param sellerName the name of the seller navigating the seller page
 @return the index of the seller they are looking for in the sellers database. If the seller is not found, then return an index of -1
 
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
### LoginClient.java
*Functionality* This method display the appropriate prompts to the user depending on if the user is accessing their existing account or needs to create a new one

*Relationship To Other Classes* This class is accessed via the ControlFlowMenu and interacts with LoginServer.java to allow the user to navigate the login.

*Fields*
 none 
 
*Methods*

public void userLogin(BufferedReader reader, PrintWriter writer) throws IOException
**Description** This method display the appropriate prompts to the user depending on if the user is accessing their existing account or needs to create a new one
@param reader reads the server output that determines if the user
@param writer writes the user input to the server to check if they are in the database 
@throws IOException to be handled in the main method with an error message
 
public static boolean userLogout()
**Description** This method logs the user out of the application and saves their information to a file.

 public static void run(int port)
 **Description** This connects the login client with the login server
 @param port number being used by the login server
 
 ----------------------------------
### LoginServer.java 
 
*Functionality* This class handles all the data processing for the login by receiving user input from the client and sending it back to be displayed to the user. This class also searches the buyer and seller databases to check if the user can log in to their existing account or needs to create a new account.

 
 *Relationship To Other Classes* This class accepts a connection form the loginclient and performs the procssing of login and keeps the client informed of whether or not a process was sucessfully performed 
 
 *Fields* 
 private ArrayList<Buyer> buyers; //keeps track of all buyers from UserInfo
 private ArrayList<Seller> sellers; //keeps track of all the sellers from UserInfo
 
 *Constructor* 
 public LoginServer() 
 
 *Methods* 
public int userLogin(String userType, BufferedReader reader, PrintWriter writer) throws IOException
**Description** This method checks if a user's account already exists and has them either log in or create a new account
@param userType the type of user logging in (buyer of seller)
@param reader reads in the username and user type entered on the client side
@param writer writes back to the client if the username already exists
@return the index of the user in the database arraylist
 
 
public boolean NewPasswordChecker(String userType, String password, PrintWriter writer)
**Description** This method checks if the password entered by a new user is 5 characters and does NOT match with a password already in the buyer or seller database
@param userType whether the user is a buyer or a seller
@param password the user entered password
@param writer tells the client if the user entered password is true (valid) or false (invalid)
@return whether the password entered by the user is valid
 

public boolean ExistingPasswordChecker(String userType, String password, String userName, PrintWriter writer)
**Description** This method checks if the password entered by a user is 5 characters and corresponds with the same valid username entered by the user
@param userType whether the user is a buyer or a seller
@param password entered by the user
@param userName entered by the user
@param writer writes back to the client whether or not the password was valid
@return whether the password is valid and exists (true) or is invalid and or does not exist (false)
 
 
 public static String run(int port)
 **Description** This method runs a login server thread which is started in the Control Flow menu. This thread also starts the login client thread. Once the login process is complete, this thread returns the usertype and the userIndex (the index of the current user in the Buyers arraylist)
@param port the available port number
@return the usertype and userindex which are sent back to the control flow menu
 
 ---------------------------------------
 ## CustomerPageServer.java 
 *Functionality* 
 The CustomerPageSever class handles the processing involved with the buyer experience such as adding and deleting items from the buyer's shopping cart, checking out the buyer's shopping cart (moving all items to their purchase history and updating the inventory and listing page accordingly), searching by modelName as well as sorting by quantity/price, exporting purchase history to a file of the buyer's choice, deleting the buyer's account, as well as saving all buyer information during and between sessions

 *Relationship To Other Classes* This class connects with CustomerPageServer.java and reads/writes to and from the customer page server. 
 
 *Fields* 
 private Buyer thisBuyer; //stores the value of the buyer currently navigating the customer page
 
 *Constructors* 
 public CustomerPageServer(Buyer buyer)
 
 *Methods* 
 
 public Buyer getThisBuyer()
 **Description** This method gets the buyer navigating the customer page
 @return the buyer navigating the customer page
 
 public static void run(Buyer buyer,int port) 
 **Description** This method allows the user to run the customer page client and server.
 @param buyer the user who is currently navigating the customer page
 @param port the next available port to connect to

public static void runShoppingCart(BufferedReader reader, PrintWriter writer,CustomerPageServer s)
 **Description** 
 This method allows the buyer to run the shopping cart page and select from this menu to add a bike, delete a bike, checkout items, or return back to home
@param reader waits for the button pressed by the buyer
@param writer allows the server to communicate with the client as they are traversing the shopping cart
@param s the object that contains the buyer navigating this customer page
 
public void addBike(BufferedReader reader, PrintWriter writer)
**Description** 
This method allows the buyer to add a bike from the shopping cart page.
@param reader saves the user input for processing
@param writer informs the client class if the user input is valid/invalid and or the process of adding a bike was successful

 public void checkout(PrintWriter writer)
 **Description** This method first checks if all the bikes in the shopping cart are still available on the listing page. If this is true, then all the bikes in the shopping cart are put into the purchase history and the quantity remaining of the bikes for sale (the bikes on the listing page and in the seller inventories) is updated accordingly. If this is false, then an error message is displayed
@param writer write to the client that the checkout has or has not successfully taken place

public void removeBike(BufferedReader reader, PrintWriter writer)
**Description** This method allows the buyer to remove bikes from their current shopping cart.
@param reader the user entered bikeID
@param writer whether or not the shopping cart item was successfully removed
 
public boolean checkBikeQuantity(String input, int bikeId, boolean inCart, int cartIndex)
 **Description** This method checks if the user entered a bike quantity that is an integer is still in stock (so the quantity on the listing page is not equal to 0 and/or has not been removed), and the quantity requested is not more than the quantity available
@param input the quantity of bikes to be entered by the buyer
@param bikeId the unique 4-digit id of the bike that the buyer wants to purchase
@param inCart checks if a bike that is being added is already in the buyer's shopping cart
@param cartIndex the index of the bike to be added or removed out from the shopping cart
@return true if the quantity is valid (meets the conditions above) and false if the quantity is not valid (does not meet the conditions above)
 
 
 TODO... 
 -------------------------------------------
 ## CustomerPageClient.java 
 
 
 
 ### SellerPageClient.java
*Functionality* This class is TODO
*Relationship To Other Classes* This class is TODO 
 
*Fields*
 TODO
 
*Constructors*
 TODO (if none, then just remove this portion) 
 
*Methods*
 
TODO 
