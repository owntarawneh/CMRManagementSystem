import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CMRGui extends JFrame {
    private final CMRManagementSystem system = new CMRManagementSystem();
    private final FrontDeskStaff frontDesk = new FrontDeskStaff("F01", "Front Desk", system);
    private final MechanicStaff mechanic = new MechanicStaff("M01", "Mechanic", system);
    private final TechnicianStaff technician = new TechnicianStaff("T01", "Technician", system);

    public CMRGui() {
        setTitle("CMR Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Front Desk", createFrontDeskPanel());
        tabbedPane.addTab("Mechanic", createMechanicPanel());
        tabbedPane.addTab("Technician", createTechnicianPanel());

        add(tabbedPane);
    }

    private JPanel createFrontDeskPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField nameField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField nidField = new JTextField();
        JTextField contactField = new JTextField();
        JComboBox<String> categoryBox = new JComboBox<>(new String[]{"private", "fleet", "staff"});

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("National ID:"));
        formPanel.add(nidField);
        formPanel.add(new JLabel("Contact Number:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryBox);

        JButton registerBtn = new JButton("Register Customer");
        registerBtn.addActionListener(e -> {
            try {
                frontDesk.registerCustomer(
                    nameField.getText(), 
                    addressField.getText(), 
                    nidField.getText(), 
                    contactField.getText(), 
                    (String) categoryBox.getSelectedItem()
                );
                JOptionPane.showMessageDialog(this, "Customer Registered Successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(registerBtn, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createMechanicPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);

        JButton viewBtn = new JButton("View Today's Appointments");
        viewBtn.addActionListener(e -> {
            List<Appointment> apps = system.printDailyAppointments(LocalDate.now());
            StringBuilder sb = new StringBuilder("--- Today's Appointments ---\n");
            for (Appointment a : apps) {
                sb.append(a.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                  .append(" - ").append(a.getCustomer().getFullName())
                  .append(" (").append(a.getCar().getPlateNumber()).append(")\n");
            }
            outputArea.setText(sb.toString());
        });

        panel.add(new JScrollPane(outputArea), BorderLayout.CENTER);
        panel.add(viewBtn, BorderLayout.NORTH);

        return panel;
    }

    private JPanel createTechnicianPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        JButton reportBtn = new JButton("Generate Daily Report (Text)");
        reportBtn.addActionListener(e -> {
            LocalDate today = LocalDate.now();
            DailyReport report = system.generateDailyReport(today);
            if (report != null) {
                JOptionPane.showMessageDialog(this, "Report generated in daily_report.txt");
            } else {
                JOptionPane.showMessageDialog(this, "No appointments today to report.");
            }
        });
        panel.add(reportBtn);
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CMRGui().setVisible(true);
        });
    }
}
