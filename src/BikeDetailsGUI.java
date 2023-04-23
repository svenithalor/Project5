import javax.swing.*;
import java.awt.*;

public class BikeDetailsGUI extends JComponent implements Runnable {
    private JTextField nameField, wheelSizeField, colorField, priceField, descriptionField, idField, usedOrNoField, quantityField, sellerNameField;

    public BikeDetailsGUI() {
        // Create the labels and text fields
        nameField = new JTextField(20);
        wheelSizeField = new JTextField(20);
        colorField = new JTextField(20);
        priceField = new JTextField(20);
        descriptionField = new JTextField(20);
        idField = new JTextField(20);
        usedOrNoField = new JTextField(20);
        quantityField = new JTextField(20);
        sellerNameField = new JTextField(20);

        // Create the panel and add the labels and text fields
        JPanel panel = new JPanel(new GridLayout(9, 2));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Wheel Size:"));
        panel.add(wheelSizeField);
        panel.add(new JLabel("Color:"));
        panel.add(colorField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Description:"));
        panel.add(descriptionField);
        panel.add(new JLabel("ID Number:"));
        panel.add(idField);
        panel.add(new JLabel("Used or New:"));
        panel.add(usedOrNoField);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new JLabel("Seller Name:"));
        panel.add(sellerNameField);

        // Create the button
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            this.sendBike();
            
            // Close the window
            Window window = SwingUtilities.windowForComponent(this);
            if (window != null) {
                window.dispose();
            }
        });

        // Add the panel and button to this component
        this.setLayout(new BorderLayout());
        this.add(panel, BorderLayout.CENTER);
        this.add(submitButton, BorderLayout.PAGE_END);
    }

    @Override
    public void run() {
        // Create the frame
        JFrame frame = new JFrame("Bike Details");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Add this component to the frame
        frame.getContentPane().add(this);

        // Show the frame
        frame.setVisible(true);
    }
    // I'm pretty sure this main method wont do anything besides allow 
    // you to run this on its own (which is useful for testing)
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new BikeDetailsGUI());
    }

    public Bike sendBike() {
        String name = nameField.getText();
        int wheelSize = Integer.valueOf(wheelSizeField.getText());
        String color = colorField.getText();
        double price = Double.parseDouble(priceField.getText());
        String description = descriptionField.getText();
        int id = Integer.valueOf(idField.getText());
        boolean usedOrNo = Boolean.parseBoolean(usedOrNoField.getText());
        int quantity = Integer.valueOf(quantityField.getText());
        String sellerName = sellerNameField.getText();

        Bike vtr = new Bike(color, wheelSize, price, name, usedOrNo, description, sellerName, quantity, id);

        return vtr;
    }
}
