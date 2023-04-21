import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ShoppingCartServer {

    /********
     * This method checks if the user entered Bike ID is a 4 digit number. If it is valid, then return false and if
     * it is not, then return false
     * @param input the bike id enterred by the user
     * @return
     */
    public boolean checkBikeID(String input) {
        //checks if the bike id consists of 4 digits
        if (input.length() != 4) {
            return false;
        }
        try {
            //checks if the bikeID consists of numbers. if not then return false
            int bikeId = Integer.parseInt(input);

        } catch (Exception e) {
            return false;

        }
        return true;
    }


    public static void main(String[] args) {
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
                    //do something


                } else if (input.equals("delete")) {
                    boolean validInput = false;
                    do {
                        String d = reader.readLine();
                        validInput = s.checkBikeID(d);
                        //lets the client know if the user input is valid or not
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
