import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaxCalculatorFrameWithFeatures extends JFrame {
    private JTextField incomeSalaryField; // Input field for income/salary
    private JRadioButton monthlyRadioButton; // Radio button for monthly calculation
    private JRadioButton annualRadioButton; // Radio button for annual calculation
    private JLabel taxLabel; // Label for Tax Amount
    private JLabel incomeAfterTaxLabel; // Label for Income After Tax

    public TaxCalculatorFrameWithFeatures() {
        initComponents(); // Initialize GUI components
        setTitle("Tax Calculator"); // Set title for the window
        setSize(400, 400); // Set the window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application when the window is closed
        setLocationRelativeTo(null); // Center the window on the screen
        setVisible(true); // Make the window visible
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout()); // Create a panel with GridBagLayout
        panel.setBackground(new Color(0xADD8E6)); // Set the background color

        GridBagConstraints gbc = new GridBagConstraints(); // Create constraints for grid bag layout
        gbc.insets = new Insets(20, 10, 10, 10); // Set insets for spacing

        JLabel headingLabel = new JLabel("Tax Calculator"); // Create a label
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Set font for the label
        headingLabel.setForeground(new Color(0x007ACC)); // Set text color
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span two columns
        panel.add(headingLabel, gbc); // Add the label to the panel

        gbc.gridy++;
        gbc.insets = new Insets(10, 10, 10, 10); // Reset insets for spacing
        panel.add(new JLabel("Income/Salary:"), gbc); // Add a label for income/salary input

        gbc.gridy++;
        incomeSalaryField = new JTextField(15); // Create an input field for income/salary
        panel.add(incomeSalaryField, gbc); // Add the income/salary input field

        gbc.gridy++;
        panel.add(new JLabel("Calculation Period:"), gbc); // Add a label for calculation period selection

        gbc.gridy++;
        JPanel periodPanel = new JPanel(); // Create a panel for radio buttons
        ButtonGroup periodButtonGroup = new ButtonGroup(); // Create a button group for radio buttons
        monthlyRadioButton = new JRadioButton("Monthly"); // Create a radio button for monthly calculation
        annualRadioButton = new JRadioButton("Annual"); // Create a radio button for annual calculation
        monthlyRadioButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for the radio button
        annualRadioButton.setFont(new Font("Arial", Font.PLAIN, 14)); // Set font for the radio button
        periodButtonGroup.add(monthlyRadioButton); // Add the radio button to the button group
        periodButtonGroup.add(annualRadioButton); // Add the radio button to the button group
        periodPanel.add(monthlyRadioButton); // Add the radio button to the panel
        periodPanel.add(annualRadioButton); // Add the radio button to the panel
        panel.add(periodPanel, gbc); // Add the panel with radio buttons

        gbc.gridy++;
        gbc.gridwidth = 2; // Span two columns
        JButton calculateButton = new JButton("Calculate"); // Create a calculate button
        calculateButton.setBackground(new Color(0xED563B)); // Set the background color of the button
        calculateButton.setFont(new Font("Arial", Font.BOLD, 16)); // Set font for the button
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateTax(); // Handle the calculate button click event
            }
        });
        panel.add(calculateButton, gbc); // Add the calculate button to the panel

        gbc.gridy++;
        JButton reloadButton = new JButton("Reload"); // Create a reload button
        reloadButton.setBackground(new Color(0x3CBE8E)); // Set the background color of the button
        reloadButton.setFont(new Font("Arial", Font.BOLD, 16)); // Set font for the button
        reloadButton.setForeground(Color.WHITE); // Set text color
        reloadButton.setFocusPainted(false); // Remove button focus border
        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields(); // Handle the reload button click event to reset fields
            }
        });
        panel.add(reloadButton, gbc); // Add the reload button to the panel

        gbc.gridy++;
        gbc.gridwidth = 1; // Reset grid width
        taxLabel = new JLabel(""); // Initialize label for Tax Amount
        taxLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set font for Tax Amount label
        taxLabel.setForeground(Color.BLACK); // Set text color to black
        panel.add(taxLabel, gbc); // Add the Tax Amount label to the panel

        gbc.gridy++;
        incomeAfterTaxLabel = new JLabel(""); // Initialize label for Income After Tax
        incomeAfterTaxLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Set font for Income After Tax label
        incomeAfterTaxLabel.setForeground(Color.BLACK); // Set text color to black
        panel.add(incomeAfterTaxLabel, gbc); // Add the Income After Tax label to the panel

        add(panel); // Add the panel to the frame
    }

    private void calculateTax() {
        String incomeSalaryText = incomeSalaryField.getText(); // Get text from income/salary field

        if (isInvalidInput(incomeSalaryText)) { // Check for invalid input
            showError("Invalid income/salary. Please enter a valid number."); // Display error message
            return;
        }

        try {
            double incomeSalary = Double.parseDouble(incomeSalaryText); // Parse income/salary as a double
            String calculationPeriod = monthlyRadioButton.isSelected() ? "Monthly" : "Annual"; // Determine calculation period

            double calculatedTax = calculateSARSTax(incomeSalary, calculationPeriod); // Calculate tax
            double incomeAfterTax = incomeSalary - calculatedTax; // Calculate income after tax

            DecimalFormat df = new DecimalFormat("#,##0.00"); // Create a decimal format
            String taxResult = "Tax Amount: R" + df.format(calculatedTax); // Format tax result
            String incomeAfterTaxResult = "Income After Tax: R" + df.format(incomeAfterTax); // Format income after tax result

            taxLabel.setText(taxResult); // Set Tax Amount label text
            incomeAfterTaxLabel.setText(incomeAfterTaxResult); // Set Income After Tax label text

            // Show the amounts in a pop-up
            JOptionPane.showMessageDialog(this, taxResult + "\n" + incomeAfterTaxResult, "Calculation Result", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            showError("Invalid input. Please enter valid numbers."); // Display error for invalid input
        }
    }

    private boolean isInvalidInput(String input) {
        if (input.isEmpty()) { // Check if input is empty
            return true;
        }

        try {
            double value = Double.parseDouble(input); // Try to parse input as a double
            return value < 0; // Check if the value is negative
        } catch (NumberFormatException e) {
            return true; // Catch any NumberFormatException as invalid input
        }
    }

    private double calculateSARSTax(double income, String calculationPeriod) {
        double tax = 0.0; // Initialize tax amount

        if ("Monthly".equals(calculationPeriod)) { // Check the calculation period
            if (income <= 205900 / 12) {
                tax = 0.18 * income; // Calculate tax based on income
            } else if (income <= 321600 / 12) {
                tax = (37062 / 12) + 0.26 * (income - 205900 / 12); // Calculate tax based on income
            } else if (income <= 445100 / 12) {
                tax = (67144 / 12) + 0.31 * (income - 321600 / 12); // Calculate tax based on income
            } else if (income <= 584200 / 12) {
                tax = (105429 / 12) + 0.36 * (income - 445100 / 12); // Calculate tax based on income
            } else if (income <= 744800 / 12) {
                tax = (155505 / 12) + 0.39 * (income - 584200 / 12); // Calculate tax based on income
            } else {
                tax = (218139 / 12) + 0.41 * (income - 744800 / 12); // Calculate tax based on income
            }
        } else if ("Annual".equals(calculationPeriod)) { // Check the calculation period
            if (income <= 205900) {
                tax = 0.18 * income; // Calculate tax based on income
            } else if (income <= 321600) {
                tax = 37062 + 0.26 * (income - 205900); // Calculate tax based on income
            } else if (income <= 445100) {
                tax = 67144 + 0.31 * (income - 321600); // Calculate tax based on income
            } else if (income <= 584200) {
                tax = 105429 + 0.36 * (income - 445100); // Calculate tax based on income
            } else if (income <= 744800) {
                tax = 155505 + 0.39 * (income - 584200); // Calculate tax based on income
            } else {
                tax = 218139 + 0.41 * (income - 744800); // Calculate tax based on income
            }
        }

        return tax; // Return the calculated tax amount
    }

    private void resetFields() {
        incomeSalaryField.setText(""); // Clear income/salary field
        taxLabel.setText(""); // Clear Tax Amount label
        incomeAfterTaxLabel.setText(""); // Clear Income After Tax label
        monthlyRadioButton.setSelected(true); // Set monthly radio button as default
        annualRadioButton.setSelected(false); // Unselect annual radio button
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE); // Display error message
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) { // Iterate over available look and feels
                if ("Nimbus".equals(info.getName())) { // Check if Nimbus look and feel is available
                    UIManager.setLookAndFeel(info.getClassName()); // Set Nimbus as the look and feel
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(TaxCalculatorFrameWithFeatures.class.getName()).log(Level.SEVERE, null, ex);
        }

        SwingUtilities.invokeLater(TaxCalculatorFrameWithFeatures::new); // Create and show the GUI in the event dispatch thread
    }
}



























