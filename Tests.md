## Test 1: User log in (Existing Buyer/Seller Account)
### Steps: 
1) User runs main method in ControlFlowMenu.java
2) User selects buyer or seller as their usertype.
3) User enters cJoslin (if buyer selected) or Bob (if seller selected) as their username
4) User enters cmj32 (if buyer selected) or bambi (if seller selected) as their password.

Expected result: Application verifies the user's username and password and automatically loads them to either the buyer menu (as cJoslin) or the seller menu (as Bob). 

## Test 2: User log in (New Buyer Account) 
### Steps: 
1) User runs main method in ControlFlowMenu.java 
2) User selects buyer as their usertype 
3) User enters Jimmy as their username


Expected result: Application will say username not found and prompt the user to create an account by reentering username and password. 
After successfully creating a new account, a success message will be shown and the user will be redirected back to the login to enter their 
new username and password. The login should be successful and the buyer menu should automatically load. 

## Test 3: User Customer Page
### Steps: 
TODO

## Test 4: User Shopping Cart - Checkout 
### Steps: 
1) Login using the username cJoslin and password cmj32. 
2) Select "Review cart" from the customer page menu. You should now see that you have one item in your shopping cart 
(a bike with the model name PanAme Mountain). 
3) Select the checkout button. 

Expected result: An error message that states "one or more bikes in your cart are not available". Since the PanAme bike 
is not on the listing page (and thus, is not in any seller's inventory) the bike cannot be purchased by the user. 

## Test 5: User Shopping Cart - Delete 
1) Follow steps 1-2 from Test 4 (again, you should see a bike with model name Pan Ame Mountain in your cart) 
2) Select "Delete" and a dropdown menu will display containing the items in your shopping cart. 
3) Select the bike with the model name "PanAme Mountain" and click "Ok". 

Expected result: A message ("Successfully deleted!") will appear on the screen and your shopping cart 
will no longer display the PanAme Mountain Bike. 

## Test 6: User Shopping Cart - Add 
//TODO

## Test 7: User Shopping Cart - 2 Buyers 
1) 


## Test 5: User Seller Page 
### Steps: 
TODO 

