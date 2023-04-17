import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.awt.*;
import java.net.Socket;

/*********
 * This class displays GUI for the login and sends the user input to the server.
 * @author Christina Joslin, lab sec 4427
 * @version 4/17/2023
 */
public class LoginClient {
    /********
     * This class
     * @param reader
     * @param writer
     */
    public void userLogin(BufferedReader reader, PrintWriter writer, int userType) {



    }
    public static void main(String[] args) {

        //sets up the output stream for the user to use
        try {
            Socket socket = new Socket("localhost",4242);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            //welcomes the user
            //TODO


            //Asks if they are a buyer or a seller via a dropdown box
            JPanel panel = new JPanel();
            JLabel label1 = new JLabel("Are you a buyer or seller?");
            panel.add(label1);
            JComboBox dropdown = new JComboBox(new String[]{"buyer", "seller"});
            panel.add(dropdown);
            int choice = JOptionPane.showConfirmDialog(null,panel,"Boilermaker Bikes",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if (choice == JOptionPane.OK_OPTION) {
                writer.write("" + dropdown.getSelectedIndex());
                writer.println();
                writer.flush();
            } else {
                return;
            }

            //Asks the user to enter their username
            String username = JOptionPane.showInputDialog(null,"Please enter your username:","Boilermaker Bikes",JOptionPane.QUESTION_MESSAGE);

            //allows the user to use the exit button
            if (username == null) {
                return;
            }

            //sends the username to the server
            writer.write(username);
            writer.println();
            writer.flush();
            System.out.printf("%s sent to the server%n",username);
            //writer this username into the file


        } catch (IOException e) {
            //prints an error message and exits the program
            JOptionPane.showConfirmDialog(null, "Error. Connection failed.",
                    "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }




    }

}
