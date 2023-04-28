import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
/*********
 * This class displays GUI for the login and sends the user input to the server. It also generates appropriate
 * error/prompt messages as the user logins in and or creates an account.
 *
 * @author Christina Joslin (userLogin Client Server and main Client/Server) &
 * Duoli Chen (userLogout and main Client/Server) lab sec 4427
 * @version 4/17/2023
 */
public class LoginClient {

    /********
     * This method display the appropriate prompts to the user depending on if the user is accessing their existing
     * account or needs to create a new one
     * @param reader reads the server output that determines if the user
     * @param writer writes the user input to the server to check if they are in the database
     * @throws IOException to be handled in the main method with an error message
     */
    public void userLogin(BufferedReader reader, PrintWriter writer) throws IOException {
        boolean userNameFound; //determines whether or not the username already exists
        int attempt = 1; //keeps track of the number of attempts made by the user to log in
        do {
            //System.out.printf("Attempt #%d%n",attempt);
            try {
                //Asks the user to enter their username
                String username = JOptionPane.showInputDialog(null, "Please enter your username:",
                        "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);

                //allows the user to use the exit button
                if (username == null) {
                    return;
                }

                //sends the username to the server
                writer.write(username);
                writer.println();
                writer.flush();
                //System.out.printf("%s sent to the server%n", username);
                if (attempt == 1) {
                    writer.write("ready");
                    writer.println();
                    writer.flush();
                    //System.out.println("Ready sent to the server");
                }
                String input = reader.readLine();
                userNameFound = Boolean.parseBoolean(input);

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            /********
             * If the username is found, then check the password. Otherwise,
             * prompt the user to create an account
             */
            if (userNameFound) {
                boolean passwordFound = false; //keeps track of if the user enter the correct password corresponding to their username
                do {
                    String password = JOptionPane.showInputDialog(null,"Please enter your password (5 characters only)","Boilermaker Bikes",JOptionPane.QUESTION_MESSAGE);

                    //allow the user to user the exit button
                    if (password == null) {
                        return;
                    }
                    //sends the password to the server
                    writer.write(password);
                    writer.println();
                    writer.flush();
                    //receives the servers input after checking the buyer's database for a username/password match
                    passwordFound = Boolean.parseBoolean(reader.readLine());

                    //if the password is invalid, then print an error message and allow the user to try again
                    if (!passwordFound) {
                        JOptionPane.showMessageDialog(null,"Invalid password. Please try again.");
                    }

                } while (!passwordFound);

                JOptionPane.showMessageDialog(null, "Successful Login!", "Boilermaker Bikes",
                       JOptionPane.INFORMATION_MESSAGE);
                return;
            } else {
                int newAccount = JOptionPane.showConfirmDialog(null, "User not found. Would you" +
                        " like to create a new account?", "Boilermaker Bikes", JOptionPane.YES_NO_OPTION);

                if (newAccount == JOptionPane.NO_OPTION || newAccount == JOptionPane.CLOSED_OPTION) {
                    JOptionPane.showMessageDialog(null, "Thank you for visiting Boilermaker " +
                            "Bikes!", "Boilermaker Bikes", JOptionPane.INFORMATION_MESSAGE);
                    //lets the server know that the user is exiting the program
                    writer.write("yes");
                    writer.println();
                    writer.flush();
                    //System.out.println("'yes' the user wants to exit the program is sent to the server");
                    writer.close();
                    reader.close();
                    return;
                } else {
                    //lets the server know that the user is continuing through the program
                    writer.write("no");
                    //System.out.println("'no' the user wants to continue the program is sent to the server");
                    writer.println();
                    writer.flush();
                }
                /********
                 * Prompts the user to create a new account starting with a username
                 */
                boolean usernameSuccess = false; //keeps track of if the username created is different from what exists

                do {
                    //sends the new username to the server to check if it does not match up with an existing username
                    String newUsername = JOptionPane.showInputDialog(null, "Please enter a username:",
                            "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);
                    writer.write(newUsername);
                    writer.println();
                    writer.flush();
                    //System.out.printf("New username %s sent to the server%n", newUsername);

                    //if the username matches up with an existing one, then have the user try again.
                    String input = reader.readLine();
                    //System.out.printf("Successfully Created an Account: %s%n", input);
                    usernameSuccess = Boolean.parseBoolean(input);
                    //System.out.println("Username Success " + usernameSuccess);
                    if (!usernameSuccess) {
                        JOptionPane.showConfirmDialog(null, "Error, this username is already " +
                                        "taken. Try again.", "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE);
                    }
                } while (!usernameSuccess);

                /*******
                 * Have the user create a new password
                 */
                boolean passwordSuccess = false;  //keeps track of if the password created is different from what exists

                do {
                    //sends the new password to the server to check if it does not match up with an existing username
                    String newPassword = JOptionPane.showInputDialog(null, "Please enter a password (5 characters only):",
                            "Boilermaker Bikes", JOptionPane.QUESTION_MESSAGE);
                    writer.write(newPassword);
                    writer.println();
                    writer.flush();
                    String input = reader.readLine();
                    //if the password is invalid or matches up with existing one, then have the user try again
                    passwordSuccess = Boolean.parseBoolean(input);
                    if (!passwordSuccess) {
                        JOptionPane.showConfirmDialog(null,"Error, this password is invalid." +
                                " Try again.","Boilermaker Bikes", JOptionPane.DEFAULT_OPTION,
                                JOptionPane.ERROR_MESSAGE);
                    }

                } while (!passwordSuccess);

                JOptionPane.showMessageDialog(null, "Account successfully created!",
                        "Boilermaker Bikes", JOptionPane.INFORMATION_MESSAGE);
                attempt++; //increments of the number of attempts made by the user to login
            }
        } while (true);

    }

    /************
     * This method logs the user out of the application and saves their information to a file.
     * @author Duoli
     */
    public static boolean userLogout() {
        int choice = JOptionPane.showConfirmDialog(null, "Confirm logout?", "Boilermaker Bikes",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (choice == JOptionPane.OK_OPTION) {
            UserInfo.writeUsers();
            return true;
        } else {
            return false;
        }
    }

    public static void run(int port) {
        try {
            //use localhost because we are having the server and client run on the same machine
            Socket socket = new Socket("localhost",port);

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String userType = ""; //saves the usertype selected by this user
            // welcome message
           int welcome =  JOptionPane.showConfirmDialog(null, "Welcome to the Boilermaker Bikes!",
                    "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
           //allows the user to exit if they press the x button
           if (welcome == JOptionPane.CLOSED_OPTION) {
               writer.close();
               reader.close();
               return;
           }

            //Asks if they are a buyer or a seller via a dropdown box
            JPanel panel = new JPanel();
            JLabel label1 = new JLabel("Are you a buyer or seller?");
            panel.add(label1);
            JComboBox dropdown = new JComboBox(new String[]{"buyer", "seller"});
            panel.add(dropdown);
            int choice = JOptionPane.showConfirmDialog(null, panel, "Boilermaker Bikes",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (choice == JOptionPane.OK_OPTION) {
                writer.write("" + dropdown.getSelectedIndex());
                writer.println();
                writer.flush();
                //saves the user type selected
                if (dropdown.getSelectedIndex() == 0) {
                    userType = "buyer";
                   // System.out.printf("Sent to the server: %s%n", userType);
                } else if (dropdown.getSelectedIndex() == 1) {
                    userType = "seller";
                    //System.out.printf("Sent to the server: %s%n", userType);
                }

            } else {
                writer.close();
                reader.close();
                return;
            }

            //creates a login client object and goes to the login method
            LoginClient login = new LoginClient();
            login.userLogin(reader, writer);
            reader.close();
            writer.close();
            socket.close(); //closes the socket for the next connection to be make with the buyer/customer pages
            return;

        } catch (IOException e) {
            //prints an error message and exits the program
            JOptionPane.showConfirmDialog(null, "Error. Connection failed.",
                    "Boilermaker Bikes", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return;
        }

    }

}
