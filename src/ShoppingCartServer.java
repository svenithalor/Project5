import javax.swing.*;
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

            //waits for what button the user presses
            String input = reader.readLine();
            if (input.equals("add")) {

                //do something

            } else if (input.equals("delete")) {

                //do something

            } else if (input.equals("checkout")) {

                //do something

            } else if (input.equals("backHome")) {

                //do something

            } else if (input.equals("refresh")) {

                //do something

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
