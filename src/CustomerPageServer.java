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
                    String input = reader.readLine();
                    int choice = Integer.parseInt(input);
                    //if the user attempts to exit the website, then end the program.
                    if (choice == -1) {
                        reader.close();
                        writer.close();
                        return;
                    }
                    UserInfo.readUsers(); //this is just a temporary value for reading new bikes in without using the login
                    ArrayList<Bike> bikes = UserInfo.getBikes();
                    switch (choice) {
                        case 1: // main menu switch case 1: view available bikes
                            for (Bike bike : bikes) {
                                writer.println(bike.toString());
                                writer.flush();
                            }
                            int choice1 = Integer.parseInt(reader.readLine());
                            switch (choice1) {
                                case 1: // sort by quantity
                                    ArrayList<Bike> quantitySorted = sortByQuantity(bikes);
                                    for (Bike bike : quantitySorted) {
                                        writer.println(bike.toString());
                                        writer.flush();
                                    }
                                    break;
                                case 2: // sort by price
                                    ArrayList<Bike> priceSorted = sortByPrice(bikes);
                                    for (Bike bike : priceSorted) {
                                        writer.println(bike.toString());
                                        writer.flush();
                                    }
                                    break;
                                case 3: // view bike listing
                                    int id = Integer.parseInt(reader.readLine());
                                    Bike bikeToView = null;
                                    for (Bike bike : bikes) {
                                        if (bike.getId() == id) {
                                            bikeToView = bike;
                                            break; // breaks out of for loop not switch case
                                        }
                                    }
                                    if (bikeToView == null) {
                                        writer.println("No bike found!");
                                        writer.flush();
                                    } else {
                                        writer.println(bikeToView.toString());
                                        writer.flush();
                                    }
                                    break;
                                case 4:
                                    break; // go back to main menu
                                case 5: // search
                                    String searchTerm = reader.readLine();
                                    ArrayList<Bike> matches = search(searchTerm, bikes);
                                    if (matches != null) {
                                        for (Bike bike : matches) {
                                            writer.println(bike.toString());
                                            writer.flush();
                                        }
                                    } else {
                                        writer.println("No matches!");
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
                            writer.println(getPurchaseHistory(fileName));
                            writer.flush();
                            break;
                        case 4: // logout -- do we need processing on the server side for this?
                            repeat = 0;
                            break;
                        case 5: // main menu option 5: delete account
                            String user = reader.readLine();
                            boolean deleted = deleteAccount(user);
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
            /*for (PurchasedBike b : purchasedBikes) {
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

    public static boolean deleteAccount(String user) {
        boolean deleted = false;
        ArrayList<Buyer> buyers = UserInfo.getBuyers();
        for (Buyer buyer : buyers) {
            if (buyer.getUsername().equalsIgnoreCase(user)) {
                buyers.remove(buyer);
                UserInfo.setBuyers(buyers);
                deleted = true;
            }
        }
        return deleted;
    }
}
