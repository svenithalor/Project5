import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class LoginServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(4242);
            Socket socket = serverSocket.accept(); //waits until the client connects
            //System.out.println("Client connected!"); Test Code For Terminal
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String userType = reader.readLine(); //reads the userType entered by the user and directs them to login/logout
            //TODO
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
