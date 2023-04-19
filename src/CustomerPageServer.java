import java.io.File;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
public class CustomerPageServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                int repeat = 1;
                while (repeat == 1) {
                    int choice = Integer.parseInt(reader.readLine());
                    ArrayList<Bike> bikes = UserInfo.getBikes();
                    switch(choice) {
                        case 1:
                            for (Bike bike : bikes) {
                                writer.println(bike.toString());
                                writer.flush();
                            }
                            int choice1 = Integer.parseInt(reader.readLine());
                            switch (choice1) {
                                case 1: ArrayList<Bike> quantitySorted = sortByQuantity(bikes);
                                    for (Bike bike: quantitySorted) {
                                        writer.println(bike.toString());
                                        writer.flush();
                                    }
                                    break;
                                case 2: ArrayList<Bike> priceSorted = sortByPrice(bikes);
                                    for (Bike bike: priceSorted) {
                                        writer.println(bike.toString());
                                        writer.flush();
                                    }
                                    break;
                                case 3: int id = Integer.parseInt(reader.readLine());
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
                                case 4: break;
                                case 5: String searchTerm = reader.readLine();
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
                        case 2: break; // TODO: shopping cart implementation
                        case 3: String fileName = reader.readLine();
                            writer.println(getPurchaseHistory(fileName));
                            writer.flush();
                            break;
                        case 5: String user = reader.readLine();
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
            PrintWriter pw = new PrintWriter(file);
            for (PurchasedBike b : purchasedBikes) {
                pw.println(b.toString());
            }
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
