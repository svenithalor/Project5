import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ShoppingCartServer {


    public static void main(String[] args) {
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
                    String d = reader.readLine();
                    //converts the input into an integer
                    try {

                        int bikeId = Integer.parseInt(d);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }




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
