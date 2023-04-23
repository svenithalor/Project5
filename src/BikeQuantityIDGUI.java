import javax.swing.*;
import java.awt.*;

public class BikeQuantityIDGUI extends JComponent implements Runnable {
    
    private JPanel option2No;
    private JLabel option2NoIDLabel;
    private JTextField IDinput;
    private JLabel option2NoQuantity;
    private JTextField QuantityInput;
    private JButton option2SubmitButton;

    

    public BikeQuantityIDGUI() {
        option2No = new JPanel();
        option2NoIDLabel = new JLabel("Enter the ID of the bike.");
        IDinput = new JTextField(10);
        option2NoQuantity = new JLabel("Enter the Quantity");
        QuantityInput = new JTextField(10);
        option2SubmitButton = new JButton("Submit");

        option2SubmitButton.addActionListener(e -> {
            try {
                this.sendIDandQuantity();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(null, "Invalid ID or Quantity.", "Boilermaker Bikes",
                JOptionPane.ERROR_MESSAGE);
            }

            Window window = SwingUtilities.windowForComponent(this);
            if (window != null) {
                window.dispose();
            }

        });



        option2No.add(option2NoIDLabel); 
        option2No.add(IDinput);
        option2No.add(option2NoQuantity);
        option2No.add(QuantityInput);
        // option2No.add(option2SubmitButton);
        // o2Frame.add(option2No);

        this.setLayout(new BorderLayout());
        this.add(option2No, BorderLayout.CENTER);
        this.add(option2SubmitButton, BorderLayout.PAGE_END);
    }
    
    @Override
    public void run() {
        JFrame o2Frame = new JFrame("Bike Details");
        o2Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        o2Frame.setSize(200, 200);
        o2Frame.getContentPane().add(this);
        o2Frame.setVisible(true);

    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new BikeQuantityIDGUI());
    }

    public int[] sendIDandQuantity() {
        int id = Integer.valueOf(IDinput.getText());
        int quantity = Integer.valueOf(QuantityInput.getText());

        int[] vtr = {id, quantity};

        return vtr;
    }

    
}

