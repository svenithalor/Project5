## Test 1: User log in (Existing Buyer/Seller Account)
### Author: Christina J
### Steps: 

1) User runs main method in ControlFlowMenu.java
2) User selects buyer or seller as their usertype.
3) User enters cJoslin (if buyer selected) or Bob (if seller selected) as their username
4) User enters cmj32 (if buyer selected) or bambi (if seller selected) as their password.

Expected result: Application verifies the user's username and password and automatically loads them to either the buyer menu (as cJoslin) or the seller menu (as Bob). 

## Test 2: User log in (New Buyer/Seller Account) 
### Author: Christina J
### Steps: 
1) User runs main method in ControlFlowMenu.java 
2) User selects buyer or seller as their usertype 
3) User enters Jimmy as their username

Expected result: Application will say username not found and prompt the user to create an account by reentering username and password. 
After successfully creating a new account, a success message will be shown and the user will be redirected back to the login to enter their 
new username and password. The login should be successful and the buyer menu should automatically load. 

## Test 3: User Shopping Cart - Checkout 
### Author Christina J
### Steps: 
1) Login using the username cJoslin and password cmj32. 
2) Select "Review cart" from the customer page menu. You should now see that you have one item in your shopping cart 
(a bike with the model name PanAme Mountain). 
3) Select the checkout button. 

Expected result: An error message that states "one or more bikes in your cart are not available". Since the PanAme bike 
is not on the listing page (and thus, is not in any seller's inventory) the bike cannot be purchased by the user. 

## Test 4: User Shopping Cart - Delete 
### Author Christina J
1) Follow steps 1-2 from Test 4 (again, you should see a bike with model name Pan Ame Mountain in your cart) 
2) Select "Delete" and a dropdown menu will display containing the items in your shopping cart. 
3) Select the bike with the model name "PanAme Mountain" and click "Ok". 

Expected result: A message ("Successfully deleted!") will appear on the screen and your shopping cart 
will no longer display the PanAme Mountain Bike. 

## Test 5: User Shopping Cart - new User information saved 
### Author Christina J
### Steps
1. Select user type as buyer. Enter a unique username (e.g. Hannah). You should receive a message asking if you would like to create a new account since the username
given was not recognized. Click "yes". 
2. Reenter the unique username Hannah and then enter a 5 character unique password (note that if your password does not 
meet these requirements you will receive an error message and be prompted to reenter a password). 
3. A message that says "account successfullly created" will appear. You will then reenter the username and password you
were entered previous and now be able to access the buyer page. 
4. Add several bikes to your shopping cart by either navigating to "view cart"
 => "add" or to "view available bikes" => dropdown menu selection => "view bike" 
5. Return to your homepage and press the "x" button. You should not see an information message 
confirming that you would like to logout. Press "yes" and the program will end. 
6. Rerun the ControlFlowMenu and select your user type as "buyer". Enter the same username and password information as before.

Expected Result: You should be able to successfully log in to your previous account and see all the changes you made from 
your previous session. 

## Test 6: User Shopping Cart - 2 Buyers Checking Out The Same Item
1. Login as two separate users (Username: cJoslin password: cmj32 and username: c9sug password: cmj45). 
2. For one user, click on "review cart" => "add", and then buy the maximum quantity of bikes 
available for a specific model (e.g. if 7 bikes are in stock then buy all 7). Do the same for the 
other user (note that you can also add an item by going to "View all available bikes" then using the dropdown
menu to select a bike and pressing "view bike" => "add to cart"). 
3. Have one user checkout the items in their cart (their cart should now be empty). If the user is already in the shopping
cart page, click "back to home" and then have them return to the page and press "checkout" (otherwise, the user can navigate 
to the shopping cart page and click "checkout"). 

Expected result: The second user will receive an error message stating that they cannot checkout their cart
because the item is now out of stock. When they click "add", they should also see that the listing page no 
longer has that bike model listed as an option since it's stock is 0. 


## Test 5: User Seller Page 
### Steps: 
TODO 

