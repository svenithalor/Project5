import java.io.File;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class CustomerPageServer {

    //Methods
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                int repeat = 1;
                while (repeat == 1) {
                    UserInfo.readUsers();
                    ArrayList<Bike> bikes = UserInfo.getBikes();
                    ArrayList<String> bikeNames = new ArrayList<>();
                    for (Bike bike : bikes) {
                        String format = "%s | $%.2f | Quantity: %d";
                        bikeNames.add(String.format(format, bike.getModelName(), bike.getPrice(), bike.getQuantity()));
                    }
                    writer.println(bikeNames);
                    writer.flush();
                    bikeNames.clear();

                    String input = reader.readLine();
                    int choice = Integer.parseInt(input);


                    switch (choice) {
                        case -1:
                            reader.close();
                            writer.close();
                            repeat = 0;
                            return;
                        case 1: // main menu switch case 1: view available bike
                            int choice1 = Integer.parseInt(reader.readLine());

                            switch (choice1) {
                                default: // writing description of selected bike
                                    Bike chosenBike = bikes.get(choice1);
                                    writer.println(String.format("Name: %s | $%.2f | %d inches", chosenBike.getModelName(), chosenBike.getPrice(), chosenBike.getWheelSize()));
                                    writer.println(String.format("Used: %b | Seller: %s | ID: %d", chosenBike.isUsed(), chosenBike.getSellerName(), chosenBike.getId()));
                                    writer.println(String.format("Description: %s", chosenBike.getDescription()));
                                    writer.flush();
                                case -3: // sort by quantity
                                    ArrayList<Bike> quantitySorted = sortByQuantity(bikes);
                                    ArrayList<String> sortedNames = new ArrayList<>();
                                    for (Bike bike : quantitySorted) {
                                        String format = "%s | $%.2f | Quantity: %d";
                                        sortedNames.add(String.format(format, bike.getModelName(), bike.getPrice(), bike.getQuantity()));
                                    }
                                    writer.println(sortedNames);
                                    writer.flush();
                                    sortedNames.clear();
                                    break;
                                case -2: // sort by price
                                    ArrayList<Bike> priceSorted = sortByPrice(bikes);
                                    ArrayList<String> priceSortedNames = new ArrayList<>();
                                    for (Bike bike : priceSorted) {
                                        String format = "%s | $%.2f | Quantity: %d";
                                        priceSortedNames.add(String.format(format, bike.getModelName(), bike.getPrice(), bike.getQuantity()));
                                    }
                                    writer.println(priceSortedNames);
                                    writer.flush();
                                    priceSortedNames.clear();
                                    break;
                                case -1:
                                    break; // go back to main menu
                                case -4: // search
                                    String searchTerm = reader.readLine();
                                    ArrayList<Bike> matches = search(searchTerm, bikes);
                                    ArrayList<String> matchNames = new ArrayList<>();
                                    if (matches != null) {
                                        for (Bike bike : matches) {
                                            String format = "%s | $%.2f | Quantity: %d";
                                            matchNames.add(String.format(format, bike.getModelName(), bike.getPrice(), bike.getQuantity()));
                                        }
                                        writer.println(matchNames);
                                        writer.flush();
                                        matchNames.clear();
                                    } else {
                                        writer.println(-1);
                                        writer.flush();
                                    }
                                    break;
                            }
                            break;
                        case 2:
                            break; // main menu option 2: view cart
                        // TODO: shopping cart implementation

                        case 3: // main menu option 3: export file with purchase history
                            String fileName = reader.readLine();
                            boolean success = getPurchaseHistory(fileName);
                            writer.println(success);
                            writer.flush();
                            break;
                        case 4: // logout -- do we need processing on the server side for this?
                            repeat = 0;
                            break;
                        case 5: // main menu option 5: delete account
                            String confirm = reader.readLine();
                            String user = reader.readLine();
                            int buyerIndex = Integer.parseInt(reader.readLine());
                            boolean deleted = deleteAccount(confirm, user, buyerIndex);
                            writer.println(deleted);
                            writer.flush();
                            break;
                    }
                }
            }
        } catch (IOException ioe) {

        }
    }

    public static ArrayList<Bike> sortByPrice(ArrayList<Bike> bikes) {
        ArrayList<Bike> sorted = new ArrayList(bikes.size());
        for (int i = 0; i < bikes.size(); i++) {
            sorted.add(bikes.get(i));
        }
        for (int i = 0; i < bikes.size() - 1; i++) {
            Bike thisBike = sorted.get(i);
            Bike lowestBike = thisBike;
            int lowestIndex = i;
            double lowestPrice = lowestBike.getPrice();
            for (int j = i + 1; j < bikes.size(); j++) {
                Bike otherBike = sorted.get(j);
                double price = otherBike.getPrice();
                if (price < lowestPrice) {
                    lowestBike = otherBike;
                    lowestPrice = price;
                    lowestIndex = j;
                }
            }
            sorted.set(i, lowestBike);
            sorted.set(lowestIndex, thisBike);
        }
        return sorted;
    }

    public static ArrayList<Bike> sortByQuantity(ArrayList<Bike> bikes) {
        ArrayList<Bike> sorted = new ArrayList(bikes.size());
        for (int i = 0; i < bikes.size(); i++) {
            sorted.add(bikes.get(i));
        }
        for (int i = 0; i < bikes.size() - 1; i++) {
            Bike thisBike = sorted.get(i);
            Bike lowestBike = thisBike;
            int lowestIndex = i;
            double lowestQuantity = lowestBike.getQuantity();
            for (int j = i + 1; j < bikes.size(); j++) {
                Bike otherBike = sorted.get(j);
                double quantity = otherBike.getQuantity();
                if (quantity < lowestQuantity) {
                    lowestBike = otherBike;
                    lowestQuantity = quantity;
                    lowestIndex = j;
                }
            }
            sorted.set(i, lowestBike);
            sorted.set(lowestIndex, thisBike);
        }

        return sorted;
    }

    public static ArrayList<Bike> search(String searchTerm, ArrayList<Bike> bikes) {
        ArrayList<Bike> matches = new ArrayList<Bike>();
        String term = searchTerm.toLowerCase();
        for (Bike bike : bikes) {
            if (bike.getModelName().contains(term)) {
                matches.add(bike);
            } else if (bike.getSellerName().contains(term)) {
                matches.add(bike);
            } else if (bike.getDescription().contains(term)) {
                matches.add(bike);
            }
        }
        if (matches.size() == 0) {
            return null;
        } else {
            return matches;
        }
    }

    public static boolean getPurchaseHistory(String fileName) {
        try {
            File file = new File(fileName);
            PrintWriter pw = new PrintWriter(file); // TODO: purchased bike error
            /*ArrayList<PurchasedBike> purchasedBikes = UserInfo.getBuyers().get(userIndex).getPurchaseHistory();
            for (PurchasedBike b : purchasedBikes) {
               pw.println(b.toString());
            }*/
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteAccount(String confirm, String user, int buyerIndex) {
        boolean deleted = false;
        ArrayList<Buyer> buyers = UserInfo.getBuyers();
        if (confirm.equals(user)) {
            buyers.remove(buyerIndex);
            UserInfo.setBuyers(buyers);
            deleted = true;
        }
        return deleted;
    }
    /*****
     * This method will display the shopping cart contents to the user and allow them to add/delete
     * items accordingly and check out items
     */
    public void shoppingCart(PrintWriter writer,BufferedReader reader) {
        //displays each bike in their shopping cart
        //complex gui for this...each
        //four buttons -+ add (simple gui textfield), delete (simple GUI textfield),
        // checkout (simple GUI messagedialog), and return (have them return)
    }
}
