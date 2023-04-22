import javax.swing.*;

public class BikeDetailsGUI {
    private JFrame frame;
    private JLabel wheelSizeLabel, colorLabel, priceLabel, descriptionLabel;
    private JTextField wheelSizeTextField, colorTextField, priceTextField, descriptionTextField;
    private JButton submitButton;
    
    public BikeDetailsGUI() {
        frame = new JFrame("Bike Details");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        
        wheelSizeLabel = new JLabel("Wheel Size:");
        wheelSizeLabel.setBounds(50, 50, 100, 30);
        frame.add(wheelSizeLabel);
        
        wheelSizeTextField = new JTextField();
        wheelSizeTextField.setBounds(160, 50, 150, 30);
        frame.add(wheelSizeTextField);
        
        colorLabel = new JLabel("Color:");
        colorLabel.setBounds(50, 90, 100, 30);
        frame.add(colorLabel);
        
        colorTextField = new JTextField();
        colorTextField.setBounds(160, 90, 150, 30);
        frame.add(colorTextField);
        
        priceLabel = new JLabel("Price:");
        priceLabel.setBounds(50, 130, 100, 30);
        frame.add(priceLabel);
        
        priceTextField = new JTextField();
        priceTextField.setBounds(160, 130, 150, 30);
        frame.add(priceTextField);
        
        descriptionLabel = new JLabel("Description:");
        descriptionLabel.setBounds(50, 170, 100, 30);
        frame.add(descriptionLabel);
        
        descriptionTextField = new JTextField();
        descriptionTextField.setBounds(160, 170, 150, 30);
        frame.add(descriptionTextField);
        
        submitButton = new JButton("Submit");
        submitButton.setBounds(160, 210, 100, 30);
        submitButton.addActionListener(e -> submitButtonClicked());
        frame.add(submitButton);
        
        frame.setVisible(true);
    }
    
    private void submitButtonClicked() {
        String wheelSize = wheelSizeTextField.getText();
        String color = colorTextField.getText();
        String price = priceTextField.getText();
        String description = descriptionTextField.getText();
        
        // Do something with the entered details here
    }
    
    public static void main(String[] args) {
        BikeDetailsGUI gui = new BikeDetailsGUI();
    }
}