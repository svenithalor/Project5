import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ShoppingCartServer {

    /********
     * This method checks if the user entered Bike ID is a 4 digit number that is already in the user's shopping cart (add)
     * or is already in the listing page (delete). If meets these requirements, then return true. If it does not,
     * then return false.
     * @param input the bike id entered by the user
     * @param buttonType the type of button (add or delete) to be used by the user
     * @param buyer who is using the shopping cart
     * @return true or false indicating is the user input is valid
     */
    public boolean checkBikeID(String input,String buttonType,Buyer buyer) {
        int bikeId = -1; //saves the bike ID entered by the user

        //checks if the bike id consists of 4 digits
        if (input.length() != 4) {
            return false;
        }
        try {
            //checks if the bikeID consists of numbers. if not then return false
            bikeId = Integer.parseInt(input);

        } catch (Exception e) {
            return false;
        }
        //checks if the bike id is either found on the listing page ("add") or in the shopping cart ("delete")
        switch (buttonType) {
            case "add":
                for (Bike b: UserInfo.getBikes()) {
                    if (b.getId() == bikeId) {
                        return true;
                    }
                }
                break;
            case "delete":
                for (PurchasedBike pb: buyer.getShoppingCart()) {
                    if (pb.getId() == bikeId) {
                        return true;
                    }
                }
                break;

        }
        return false;
    }



    public static void main(String[] args) {
        UserInfo.readUsers(); //TEMP VAlUE reads the users in

        //creates a Shopping Cart server object to navigate the shopping cart server
        ShoppingCartServer s = new ShoppingCartServer();
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            Socket socket = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            do {
                //waits for what button the user presses
                String input = reader.readLine();

                if (input.equals("add")) {
                    boolean validInput = false;
                    do {
                        String d = reader.readLine();
                        //Sample Buyer for now...
                        validInput = s.checkBikeID(d,"add",UserInfo.getBuyers().get(0)); //TEMP value
                        //lets the client know if the user input is valid or not
                        writer.write("" + validInput);
                        writer.println();
                        writer.flush();

                    } while (!validInput);


                } else if (input.equals("delete")) {
                    boolean validInput = false;
                    do {
                        String d = reader.readLine();
                        //Sample Buyer for now...
                        validInput = s.checkBikeID(d,"delete",UserInfo.getBuyers().get(0)); //TEMP value
                        //lets the client know if the user input is valid or not
                        System.out.println(validInput);
                        writer.write("" + validInput);
                        writer.println();
                        writer.flush();

                    } while (!validInput);

                    //do something

                } else if (input.equals("checkout")) {


                    //do something

                } else if (input.equals("backHome")) {

                    //do something

                } else if (input.equals("refresh")) {

                    //do something

                }


            } while (true);


        } catch (IOException e) {
            e.printStackTrace();

        }


    }
}
