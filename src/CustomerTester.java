import java.util.ArrayList;

public class CustomerTester {
    public static void main(String[] args) {
        String username = "sveni";
        String password = "1508";
        ArrayList<PurchasedBike> cart = new ArrayList<>();
        ArrayList<PurchasedBike> purchases = new ArrayList<>();
        Buyer buyer = new Buyer(username, password, cart, purchases);
        CustomerPageClient.run(buyer);
    }
}
