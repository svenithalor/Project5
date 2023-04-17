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
     * This method display the appropriate prompts to the user depending on if the username is already found or if
     * a new account needs to be created
     * @param reader reads the server output that determines if the user
     * @param writer
     * @param userType the type of user (buyer or seller)
     */
    public void userLogin(BufferedReader reader, PrintWriter writer, String userType) throws IOException {
            boolean userNameFound = false; //determines whether or not the username already exists
            try {
                writer.write("ready");
                writer.println();
                writer.flush();
                userNameFound = Boolean.parseBoolean(reader.readLine());

            } catch (IOException e) {
                e.printStackTrace();
            }
            /********
             * If the username is found, then print a success message and leave this method. Otherwise,
             * prompt the user to create an account
             */
            if (userNameFound) {
                JOptionPane.showMessageDialog(null,"Successful Login!","Boilermaker Bikes",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                int newAccount = JOptionPane.showConfirmDialog(null,"User not found. Would you" +
                        " like to create a new account?","Boilermaker Bikes",JOptionPane.YES_NO_OPTION);

                if (newAccount == JOptionPane.NO_OPTION || newAccount == JOptionPane.CLOSED_OPTION) {
                    JOptionPane.showMessageDialog(null, "Thank you for visiting Boilermaker " +
                                    "Bikes!", "Boilermaker Bikes", JOptionPane.INFORMATION_MESSAGE);
                    //lets the server know that the user is exiting the program
                    writer.write("yes");
                    writer.println();
                    writer.flush();
                    writer.close();
                    reader.close();
                    return;
                } else {
                    //lets the server know that the user is continuing through the program
                    writer.write("no");
                    writer.println();
                    writer.flush();
                }
                /********
                 * Prompts the user to create a new account
                 */
                boolean success = false; //keeps track of if the username created is different from what exists

                do {
                    //sends the new username to the server to check if it does not match up with an existing username
                    String newUsername = JOptionPane.showInputDialog(null,"Please enter a username:",
                            "Boilermaker Bikes",JOptionPane.QUESTION_MESSAGE);
                    writer.write(newUsername);
                    writer.println();
                    writer.flush();

                    //if the username matches up with an existing one, then have the user try again.
                    String input = reader.readLine();
                    System.out.printf("Succesfully Created an Account: %s%n",input);
                    success = Boolean.parseBoolean(input);
                    if (!success) {
                        JOptionPane.showConfirmDialog(null,"Error, this username is already " +
                                "taken. Try again.","Boilermaker Bikes",JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE);
                    }
                } while (!success);

                JOptionPane.showMessageDialog(null,"Account successfully created!","Boilermaker Bikes",JOptionPane.INFORMATION_MESSAGE);

            }
        switch(userType) {
            case "buyer":
                //TODO
                break;
            case "seller":

                //TODO
                break;
        }


    }
    public static void main(String[] args) {

        //sets up the output stream for the user to use
        try {
            Socket socket = new Socket("localhost",4242);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String userType = ""; //saves the usertype selected by this user
            //welcomes the user
            //TODO


            //Asks if they are a buyer or a seller via a dropdown box
            JPanel panel = new JPanel();
            JLabel label1 = new JLabel("Are you a buyer or seller?");
            panel.add(label1);
            JComboBox dropdown = new JComboBox(new String[]{"buyer", "seller"});
            panel.add(dropdown);
            int choice = JOptionPane.showConfirmDialog(null,panel,"Boilermaker Bikes",
                    JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
            if (choice == JOptionPane.OK_OPTION) {
                writer.write("" + dropdown.getSelectedIndex());
                writer.println();
                writer.flush();
                //saves the user type selected
                if (dropdown.getSelectedIndex() == 0) {
                    userType = "buyer";
                } else if (dropdown.getSelectedIndex() == 1) {
                    userType = "seller";
                }

            } else {
                writer.close();
                reader.close();
                return;
            }

            //Asks the user to enter their username
            String username = JOptionPane.showInputDialog(null,"Please enter your username:",
                    "Boilermaker Bikes",JOptionPane.QUESTION_MESSAGE);

            //allows the user to use the exit button
            if (username == null) {
                return;
            }

            //sends the username to the server
            writer.write(username);
            writer.println();
            writer.flush();
            System.out.printf("%s sent to the server%n",username);

            //creates a login client object and goes to the login method
            LoginClient login = new LoginClient();

            login.userLogin(reader, writer,userType);


        } catch (IOException e) {
            //prints an error message and exits the program
            JOptionPane.showConfirmDialog(null, "Error. Connection failed.",
                    "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }




    }

}
