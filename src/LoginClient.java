import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class LoginClient {
    public static void main(String[] args) {
        //welcomes the user
        //TODO
        String username = JOptionPane.showInputDialog(null,"Please enter your username:","Boilermaker Bikes",JOptionPane.QUESTION_MESSAGE);

        //allows the user to use the exit button
        if (username == null) {
            return;
        }
        //sets up the output stream for the user to use
        try {
            Socket socket = new Socket("local",4242);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

        } catch (IOException e) {
            //need to print an error message here
            e.printStackTrace();
        }
        //writer this username into the file

    }

}
