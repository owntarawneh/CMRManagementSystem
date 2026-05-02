import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;

public class CMRGui extends JFrame {

    private static final CMRManagementSystem system = new CMRManagementSystem();

    private JTextArea outputArea;
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public CMRGui() {
        setTitle("CMR Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 680);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        initComponents();
    }

    private void initComponents() {
        JLabel header = new JLabel("CMR Management System", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(new Color(25, 75, 135));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(14, 0, 14, 0));
        add(header, BorderLayout.NORTH);

        JPanel roleBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        roleBar.setBackground(new Color(225, 235, 252));

        JButton frontDeskBtn = makeBtn("Front Desk", new Color(30, 100, 180));
        JButton mechanicBtn  = makeBtn("Mechanic",   new Color(20, 130, 70));
        JButton technicianBtn = makeBtn("Technician", new Color(150, 55, 15));
        JButton clearBtn      = makeBtn("Clear Output", new Color(100, 100, 100));

        roleBar.add(frontDeskBtn);
        roleBar.add(mechanicBtn);
        roleBar.add(technicianBtn);
        roleBar.add(clearBtn);

        cardLayout = new CardLayout();
        mainPanel  = new JPanel(cardLayout);
        mainPanel.add(createWelcomePanel(),    "welcome");
        mainPanel.add(createFrontDeskPanel(), "frontdesk");
        mainPanel.add(createMechanicPanel(),  "mechanic");
        mainPanel.add(createTechnicianPanel(),"technician");

        outputArea = new JTextArea(8, 0);
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setBackground(new Color(248, 248, 248));
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setBorder(BorderFactory.createTitledBorder("Output"));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mainPanel, outputScroll);
        split.setResizeWeight(0.65);
        split.setDividerLocation(400);

        JPanel center = new JPanel(new BorderLayout());
        center.add(roleBar, BorderLayout.NORTH);
        center.add(split,   BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        frontDeskBtn.addActionListener(e -> cardLayout.show(mainPanel, "frontdesk"));
        mechanicBtn.addActionListener(e  -> cardLayout.show(mainPanel, "mechanic"));
        technicianBtn.addActionListener(e -> cardLayout.show(mainPanel, "technician"));
        clearBtn.addActionListener(e     -> outputArea.setText(""));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private JButton makeBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Arial", Font.BOLD, 13));
        b.setPreferredSize(new Dimension(140, 36));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void addRow(JPanel p, GridBagConstraints gc, int row, String label, JComponent field) {
        gc.gridx = 0; gc.gridy = row; gc.gridwidth = 1; gc.weightx = 0;
        p.add(new JLabel(label), gc);
        gc.gridx = 1; gc.weightx = 1.0;
        p.add(field, gc);
    }

    private void out(String text) {
        outputArea.append(text + "\n" + "─".repeat(50) + "\n");
        outputArea.setCaretPosition(outputArea.getDocument().getLength());
    }

    private GridBagConstraints defaultGc() {
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5, 6, 5, 6);
        gc.fill   = GridBagConstraints.HORIZONTAL;
        return gc;
    }

    // ── welcome ───────────────────────────────────────────────────────────────

    private JPanel createWelcomePanel() {
        JPanel p = new JPanel(new GridBagLayout());
        JLabel msg = new JLabel(
            "<html><center><b>Welcome to CMR Management System</b><br/><br/>"
            + "Select a role from the toolbar above to get started.</center></html>",
            SwingConstants.CENTER);
        msg.setFont(new Font("Arial", Font.PLAIN, 15));
        p.add(msg);
        return p;
    }

    // ── front desk ────────────────────────────────────────────────────────────

    private JPanel createFrontDeskPanel() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Register Customer", registerCustomerTab());
        tabs.addTab("Schedule Appointment", scheduleTab());
        tabs.addTab("Print Bill", printBillTab());
        tabs.addTab("Daily Appointments", dailyAppointmentsTab());
        return roleWrapper("Front Desk Staff", new Color(30, 100, 180), tabs);
    }

    private JPanel registerCustomerTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = defaultGc();

        JTextField nameF    = new JTextField(22);
        JTextField addressF = new JTextField(22);
        JTextField nidF     = new JTextField(22);
        JTextField contactF = new JTextField(22);
        JComboBox<String> catBox = new JComboBox<>(new String[]{"private", "fleet", "staff"});
        JTextField makeF    = new JTextField(22);
        JTextField modelF   = new JTextField(22);
        JTextField plateF   = new JTextField(22);
        JComboBox<String> pmBox = new JComboBox<>(new String[]{"cash", "credit"});

        addRow(form, gc, 0, "Full Name:",      nameF);
        addRow(form, gc, 1, "Address:",        addressF);
        addRow(form, gc, 2, "National ID:",    nidF);
        addRow(form, gc, 3, "Contact:",        contactF);
        addRow(form, gc, 4, "Category:",       catBox);

        gc.gridx = 0; gc.gridy = 5; gc.gridwidth = 2;
        JLabel sep = new JLabel("── Car Details ──");
        sep.setFont(new Font("Arial", Font.ITALIC, 11));
        sep.setForeground(Color.GRAY);
        form.add(sep, gc);

        addRow(form, gc, 6, "Car Make:",       makeF);
        addRow(form, gc, 7, "Car Model:",      modelF);
        addRow(form, gc, 8, "Plate Number:",   plateF);
        addRow(form, gc, 9, "Payment Method:", pmBox);

        JButton btn = makeBtn("Register", new Color(30, 100, 180));
        btn.addActionListener(e -> {
            String name    = nameF.getText().trim();
            String address = addressF.getText().trim();
            String nid     = nidF.getText().trim();
            String contact = contactF.getText().trim();
            String cat     = (String) catBox.getSelectedItem();
            String make    = makeF.getText().trim();
            String model   = modelF.getText().trim();
            String plate   = plateF.getText().trim();
            String pm      = (String) pmBox.getSelectedItem();

            if (name.isEmpty() || address.isEmpty() || nid.isEmpty() ||
                contact.isEmpty() || make.isEmpty() || model.isEmpty() || plate.isEmpty()) {
                out("Error: All fields are required.");
                return;
            }
            try {
                system.registerCustomer(name, address, nid, contact, cat);
                Customer cust = system.getCustomerById(nid);
                cust.registerCar(new Car(make, model, plate, cust));
                cust.setPreferredPaymentMethod("credit".equals(pm) ? new CreditCardPayment() : new CashPayment());
                out("Registered: " + name + "  (ID: " + nid + ")\nCar: " + make + " " + model + " [" + plate + "]");
                for (JTextField f : new JTextField[]{nameF, addressF, nidF, contactF, makeF, modelF, plateF})
                    f.setText("");
            } catch (IllegalArgumentException ex) {
                out("Error: " + ex.getMessage());
            }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRow(btn), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel scheduleTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = defaultGc();

        JTextField nidF  = new JTextField(22);
        JTextField plateF = new JTextField(22);
        JTextField dtF   = new JTextField(22);
        dtF.setToolTipText("yyyy-MM-dd HH:mm");

        addRow(form, gc, 0, "National ID:",  nidF);
        addRow(form, gc, 1, "Plate Number:", plateF);
        addRow(form, gc, 2, "Date & Time:",  dtF);

        gc.gridx = 0; gc.gridy = 3; gc.gridwidth = 2;
        hint(form, gc, "Format: yyyy-MM-dd HH:mm  |  Business hours 09:00–17:00  |  Today only");

        JButton btn = makeBtn("Schedule", new Color(30, 100, 180));
        btn.addActionListener(e -> {
            String nid   = nidF.getText().trim();
            String plate = plateF.getText().trim();
            String dtStr = dtF.getText().trim();
            if (nid.isEmpty() || plate.isEmpty() || dtStr.isEmpty()) {
                out("Error: All fields are required.");
                return;
            }
            try {
                LocalDateTime dt = LocalDateTime.parse(dtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                boolean ok = system.scheduleAppointment(nid, plate, dt);
                if (ok) {
                    out("Appointment scheduled: " + nid + " at " + dt);
                    nidF.setText(""); plateF.setText(""); dtF.setText("");
                } else {
                    out("Failed: check ID, plate, and time constraints (same day, 09:00–17:00, no conflicts).");
                }
            } catch (DateTimeParseException ex) {
                out("Error: use format yyyy-MM-dd HH:mm");
            }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRow(btn), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel printBillTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = defaultGc();

        JTextField nidF = new JTextField(22);
        JTextField dtF  = new JTextField(22);

        addRow(form, gc, 0, "National ID:",         nidF);
        addRow(form, gc, 1, "Appointment Date & Time:", dtF);

        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 2;
        hint(form, gc, "Format: yyyy-MM-dd HH:mm");

        JButton btn = makeBtn("Generate Bill", new Color(30, 100, 180));
        btn.addActionListener(e -> {
            String nid   = nidF.getText().trim();
            String dtStr = dtF.getText().trim();
            if (nid.isEmpty() || dtStr.isEmpty()) { out("Error: All fields required."); return; }
            try {
                LocalDateTime dt = LocalDateTime.parse(dtStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                Bill bill = system.printBillByNationalId(nid, dt);
                if (bill != null) {
                    out(bill.getItemizedBreakdown());
                } else {
                    out("No appointment found for " + nid + " at " + dt);
                }
            } catch (DateTimeParseException ex) {
                out("Error: use format yyyy-MM-dd HH:mm");
            }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRow(btn), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel dailyAppointmentsTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = defaultGc();

        JTextField dateF = new JTextField(22);
        addRow(form, gc, 0, "Date:", dateF);
        gc.gridx = 0; gc.gridy = 1; gc.gridwidth = 2;
        hint(form, gc, "Format: yyyy-MM-dd");

        JButton btn = makeBtn("List", new Color(30, 100, 180));
        btn.addActionListener(e -> {
            String ds = dateF.getText().trim();
            if (ds.isEmpty()) { out("Error: Date required."); return; }
            try {
                LocalDate date = LocalDate.parse(ds, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                List<Appointment> appts = system.printDailyAppointments(date);
                if (appts.isEmpty()) {
                    out("No appointments on " + date);
                } else {
                    StringBuilder sb = new StringBuilder("Appointments on " + date + ":\n");
                    for (Appointment a : appts) sb.append(a.getAppointmentDetails()).append("\n---\n");
                    out(sb.toString());
                }
            } catch (DateTimeParseException ex) {
                out("Error: use format yyyy-MM-dd");
            }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRow(btn), BorderLayout.SOUTH);
        return panel;
    }

    // ── mechanic ──────────────────────────────────────────────────────────────

    private JPanel createMechanicPanel() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("View Appointments",  viewAppointmentsTab(new Color(20, 130, 70)));
        tabs.addTab("Add Service Report", serviceReportTab());
        return roleWrapper("Mechanic Staff", new Color(20, 130, 70), tabs);
    }

    private JPanel viewAppointmentsTab(Color btnColor) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = defaultGc();

        JTextField dateF = new JTextField(22);
        addRow(form, gc, 0, "Date:", dateF);
        gc.gridx = 0; gc.gridy = 1; gc.gridwidth = 2;
        hint(form, gc, "Format: yyyy-MM-dd");

        JButton btn = makeBtn("View", btnColor);
        btn.addActionListener(e -> {
            String ds = dateF.getText().trim();
            if (ds.isEmpty()) { out("Error: Date required."); return; }
            try {
                LocalDate date = LocalDate.parse(ds, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                List<Appointment> appts = system.printDailyAppointments(date);
                if (appts.isEmpty()) {
                    out("No appointments on " + date);
                } else {
                    StringBuilder sb = new StringBuilder("Appointments on " + date + ":\n");
                    for (Appointment a : appts) sb.append(a.getAppointmentDetails()).append("\n---\n");
                    out(sb.toString());
                }
            } catch (DateTimeParseException ex) {
                out("Error: use format yyyy-MM-dd");
            }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRow(btn), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel serviceReportTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = defaultGc();

        JTextField nidF    = new JTextField(22);
        JTextField dateF   = new JTextField(22);
        JTextArea  repairsA = new JTextArea(3, 22);
        JTextArea  partsA   = new JTextArea(3, 22);
        JTextField costF   = new JTextField(22);

        addRow(form, gc, 0, "National ID:",          nidF);
        addRow(form, gc, 1, "Date (yyyy-MM-dd):",    dateF);
        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 1; gc.weightx = 0;
        form.add(new JLabel("Repairs (one/line):"),  gc);
        gc.gridx = 1; gc.weightx = 1.0;
        form.add(new JScrollPane(repairsA), gc);
        gc.gridx = 0; gc.gridy = 3; gc.weightx = 0;
        form.add(new JLabel("Parts (one/line):"),    gc);
        gc.gridx = 1; gc.weightx = 1.0;
        form.add(new JScrollPane(partsA),   gc);
        addRow(form, gc, 4, "Parts Cost:",           costF);

        JButton btn = makeBtn("Add Report", new Color(20, 130, 70));
        btn.addActionListener(e -> {
            String nid     = nidF.getText().trim();
            String dateStr = dateF.getText().trim();
            String repairs = repairsA.getText().trim();
            String parts   = partsA.getText().trim();
            String costStr = costF.getText().trim();

            if (nid.isEmpty() || dateStr.isEmpty() || repairs.isEmpty() || costStr.isEmpty()) {
                out("Error: National ID, date, repairs, and cost are required."); return;
            }
            try {
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String[] r = repairs.split("\\n");
                String[] p = parts.isEmpty() ? new String[0] : parts.split("\\n");
                double cost = Double.parseDouble(costStr);
                boolean ok = system.addServiceReport(nid, date, r, p, cost);
                if (ok) {
                    out("Service report added for " + nid + " on " + date);
                    nidF.setText(""); dateF.setText(""); repairsA.setText(""); partsA.setText(""); costF.setText("");
                } else {
                    out("No appointment found for " + nid + " on " + date);
                }
            } catch (DateTimeParseException ex) { out("Error: use format yyyy-MM-dd"); }
              catch (NumberFormatException   ex) { out("Error: invalid cost value."); }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRow(btn), BorderLayout.SOUTH);
        return panel;
    }

    // ── technician ────────────────────────────────────────────────────────────

    private JPanel createTechnicianPanel() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Add Diagnostic Report", diagnosticReportTab());
        tabs.addTab("Generate Daily Report", generateDailyReportTab());
        return roleWrapper("Technician Staff", new Color(150, 55, 15), tabs);
    }

    private JPanel diagnosticReportTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = defaultGc();

        JTextField nidF   = new JTextField(22);
        JTextField dateF  = new JTextField(22);
        JTextArea  issA   = new JTextArea(3, 22);
        JTextArea  recA   = new JTextArea(3, 22);
        JTextField feeF   = new JTextField(22);

        addRow(form, gc, 0, "National ID:",            nidF);
        addRow(form, gc, 1, "Date (yyyy-MM-dd):",      dateF);
        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 1; gc.weightx = 0;
        form.add(new JLabel("Issues (one/line):"),     gc);
        gc.gridx = 1; gc.weightx = 1.0;
        form.add(new JScrollPane(issA),   gc);
        gc.gridx = 0; gc.gridy = 3; gc.weightx = 0;
        form.add(new JLabel("Recommendations (one/line):"), gc);
        gc.gridx = 1; gc.weightx = 1.0;
        form.add(new JScrollPane(recA),   gc);
        addRow(form, gc, 4, "Diagnostic Fee:",         feeF);

        JButton btn = makeBtn("Add Report", new Color(150, 55, 15));
        btn.addActionListener(e -> {
            String nid     = nidF.getText().trim();
            String dateStr = dateF.getText().trim();
            String issues  = issA.getText().trim();
            String recs    = recA.getText().trim();
            String feeStr  = feeF.getText().trim();

            if (nid.isEmpty() || dateStr.isEmpty() || issues.isEmpty() || feeStr.isEmpty()) {
                out("Error: National ID, date, issues, and fee are required."); return;
            }
            try {
                LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String[] i = issues.split("\\n");
                String[] r = recs.isEmpty() ? new String[0] : recs.split("\\n");
                double fee = Double.parseDouble(feeStr);
                boolean ok = system.addDiagnosticReport(nid, date, i, r, fee);
                if (ok) {
                    out("Diagnostic report added for " + nid + " on " + date);
                    nidF.setText(""); dateF.setText(""); issA.setText(""); recA.setText(""); feeF.setText("");
                } else {
                    out("No appointment found for " + nid + " on " + date);
                }
            } catch (DateTimeParseException ex) { out("Error: use format yyyy-MM-dd"); }
              catch (NumberFormatException   ex) { out("Error: invalid fee value."); }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRow(btn), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel generateDailyReportTab() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gc = defaultGc();

        JTextField dateF = new JTextField(22);
        addRow(form, gc, 0, "Date:", dateF);
        gc.gridx = 0; gc.gridy = 1; gc.gridwidth = 2;
        hint(form, gc, "Format: yyyy-MM-dd  |  Report saved to daily_report.txt");

        JButton btn = makeBtn("Generate", new Color(150, 55, 15));
        btn.addActionListener(e -> {
            String ds = dateF.getText().trim();
            if (ds.isEmpty()) { out("Error: Date required."); return; }
            try {
                LocalDate date = LocalDate.parse(ds, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                DailyReport report = system.generateDailyReport(date);
                if (report != null) {
                    out(report.getFormattedText());
                    dateF.setText("");
                } else {
                    out("No serviced appointments on " + date + " (need at least one service report attached).");
                }
            } catch (DateTimeParseException ex) {
                out("Error: use format yyyy-MM-dd");
            }
        });

        panel.add(form, BorderLayout.CENTER);
        panel.add(btnRow(btn), BorderLayout.SOUTH);
        return panel;
    }

    // ── small layout helpers ──────────────────────────────────────────────────

    private JPanel roleWrapper(String title, Color color, JTabbedPane tabs) {
        JPanel p = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel(title, SwingConstants.LEFT);
        lbl.setFont(new Font("Arial", Font.BOLD, 15));
        lbl.setForeground(color);
        lbl.setBorder(BorderFactory.createEmptyBorder(8, 10, 4, 0));
        p.add(lbl,  BorderLayout.NORTH);
        p.add(tabs, BorderLayout.CENTER);
        return p;
    }

    private JPanel btnRow(JButton btn) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        p.add(btn);
        return p;
    }

    private void hint(JPanel form, GridBagConstraints gc, String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Arial", Font.ITALIC, 10));
        lbl.setForeground(Color.GRAY);
        form.add(lbl, gc);
    }

    // ── entry point ───────────────────────────────────────────────────────────

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CMRGui().setVisible(true));
    }
}
