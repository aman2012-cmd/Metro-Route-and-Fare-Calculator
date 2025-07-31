package MetroApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class MetroApp {

    private final Graph_M graph = new Graph_M();
    private JFrame frame;

    private JComboBox<String> sourceCombo;
    private JComboBox<String> destCombo;
    private JTextField dateDisplayField;
    private JCheckBox discountBox;
    private JTextArea resultArea;

    private LocalDate selectedDate = LocalDate.now();

    public MetroApp() {
        frame = new JFrame("Metro Route and Fare Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(70, 130, 180));
        JLabel titleLabel = new JLabel("Delhi Metro Route Planner");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        sourceCombo = new JComboBox<>(graph.getStations().toArray(new String[0]));
        destCombo = new JComboBox<>(graph.getStations().toArray(new String[0]));

        dateDisplayField = new JTextField(selectedDate.toString(), 10);
        dateDisplayField.setEditable(false);
        JButton datePickerButton = new JButton("🗓️");
        datePickerButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        datePickerButton.setFocusPainted(false);
        datePickerButton.addActionListener(e -> showDatePickerDialog());

        JPanel datePanel = new JPanel(new BorderLayout(5, 0));
        datePanel.add(dateDisplayField, BorderLayout.CENTER);
        datePanel.add(datePickerButton, BorderLayout.EAST);

        discountBox = new JCheckBox("Student/Senior (15% off)");
        resultArea = new JTextArea(20, 120); // Corrected: Removed markdown stars
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setBackground(new Color(255, 255, 240));
        resultArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JScrollPane resultScrollPane = new JScrollPane(resultArea);
        resultScrollPane.setPreferredSize(new Dimension(1200, 300)); // Corrected: Removed markdown stars

        JButton calculateFareBtn = new JButton("Calculate Fare & Routes");
        JButton bookTicketBtn = new JButton("Book Ticket");

        Font buttonFont = new Font("Segoe UI", Font.BOLD, 16);

        calculateFareBtn.setBackground(new Color(150, 200, 255));
        calculateFareBtn.setForeground(Color.BLACK);
        calculateFareBtn.setFont(buttonFont);
        calculateFareBtn.setFocusPainted(false);
        calculateFareBtn.setBorder(BorderFactory.createRaisedBevelBorder());

        bookTicketBtn.setBackground(new Color(150, 255, 150));
        bookTicketBtn.setForeground(Color.BLACK);
        bookTicketBtn.setFont(buttonFont);
        bookTicketBtn.setFocusPainted(false);
        bookTicketBtn.setBorder(BorderFactory.createRaisedBevelBorder());

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        JLabel sourceLabel = new JLabel("Source Station:");
        JLabel destinationLabel = new JLabel("Destination Station:");
        JLabel dateLabel = new JLabel("Travel Date:");

        sourceLabel.setFont(labelFont);
        destinationLabel.setFont(labelFont);
        dateLabel.setFont(labelFont);
        discountBox.setFont(labelFont);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(new Color(240, 248, 255));
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                new TitledBorder(BorderFactory.createEtchedBorder(), "Ticket Booking Details", TitledBorder.LEFT, TitledBorder.TOP, new Font("Segoe UI", Font.BOLD, 16), new Color(0, 0, 128)),
                new EmptyBorder(10, 10, 10, 10)));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(sourceLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(sourceCombo, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(destinationLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(destCombo, gbc);

        row++; // Adjust row index due to removal (now points to dateLabel row)
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(dateLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(datePanel, gbc);

        row++;
        gbc.gridx = 1;
        gbc.gridy = row;
        inputPanel.add(discountBox, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(calculateFareBtn, gbc);
        gbc.gridx = 1;
        inputPanel.add(bookTicketBtn, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        inputPanel.add(resultScrollPane, gbc);

        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Book Ticket", inputPanel);
        tabPane.addTab("Map View", new MapView(graph));
        tabPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        mainPanel.add(tabPane, BorderLayout.CENTER);
        frame.getContentPane().add(mainPanel);

        showWelcomePage();

        calculateFareBtn.addActionListener((ActionEvent e) -> {
            String src = (String) sourceCombo.getSelectedItem();
            String dest = (String) destCombo.getSelectedItem();
            boolean isEligible = discountBox.isSelected();

            if (src == null || dest == null) {
                JOptionPane.showMessageDialog(frame, "Please select source and destination to view routes.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<Graph_M.PathResult> allRoutes = graph.findAllPaths(src, dest);
            if (allRoutes.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No route found between " + src + " and " + dest + ".", "No Route", JOptionPane.INFORMATION_MESSAGE);
                resultArea.setText("");
                return;
            }
            allRoutes.sort(Comparator.comparingInt(r -> r.cost));
            resultArea.setText(getFormattedRoutes(allRoutes, isEligible));
        });

        bookTicketBtn.addActionListener((ActionEvent e) -> {
            String src = (String) sourceCombo.getSelectedItem();
            String dest = (String) destCombo.getSelectedItem();
            String travelDate = dateDisplayField.getText();
            boolean isEligible = discountBox.isSelected();

            if (src == null || dest == null || travelDate.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select all details (Source, Destination, Date) to book a ticket.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String name = "";
            String recipientEmail = "";

            while (true) {
                JPanel inputPanelDialog = new JPanel(new GridLayout(2, 2, 10, 10));
                JTextField nameFieldDialog = new JTextField(name);
                JTextField emailFieldDialog = new JTextField(recipientEmail);
                inputPanelDialog.add(new JLabel("Your Name:"));
                inputPanelDialog.add(nameFieldDialog);
                inputPanelDialog.add(new JLabel("Recipient Email:"));
                inputPanelDialog.add(emailFieldDialog);

                int result = JOptionPane.showConfirmDialog(frame, inputPanelDialog, "Enter Details to Book Ticket", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                    name = nameFieldDialog.getText().trim();
                    recipientEmail = emailFieldDialog.getText().trim();

                    boolean isNameValid = name.matches("^[a-zA-Z\\s]+$");
                    boolean isEmailValid = recipientEmail != null && recipientEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

                    if (name.isEmpty() || (recipientEmail == null || recipientEmail.isEmpty())) {
                        JOptionPane.showMessageDialog(frame, "Name and Email cannot be empty.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    } else if (!isNameValid) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid name (letters and spaces only).", "Input Error", JOptionPane.WARNING_MESSAGE);
                    } else if (!isEmailValid) {
                        JOptionPane.showMessageDialog(frame, "Please enter a valid email address.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    } else {
                        break;
                    }
                } else {
                    return;
                }
            }

            List<Graph_M.PathResult> allRoutes = graph.findAllPaths(src, dest);
            if (allRoutes.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No route found to book a ticket for.", "No Route", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            allRoutes.sort(Comparator.comparingInt(r -> r.cost));
            Graph_M.PathResult best = allRoutes.get(0);
            int bestFare = calculateFare(best.cost);

            String discountMessage = "";
            if (isEligible) {
                bestFare = (int) (bestFare * 0.85);
                discountMessage = " (15% discounted)";
            }

            String confirmationMessage = "<html><b>Confirm Ticket Booking</b><br><br>"
                    + "Source: " + src + "<br>"
                    + "Destination: " + dest + "<br>"
                    + "Fare: <span style='color: green; font-weight: bold;'>Rs. " + bestFare + "</span>" + discountMessage + "<br>"
                    + "Name: " + name + "<br>"
                    + "Email: " + recipientEmail + "<br>"
                    + "Date: " + travelDate + "<br><br>";

            if (isEligible) {
                confirmationMessage += "<b>* Please carry your Senior Citizen/Student ID Card for approval.</b><br><br>";
            }
            confirmationMessage += "Proceed to book and send Email?";

            int confirm = JOptionPane.showConfirmDialog(frame, confirmationMessage, "Confirm Ticket", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                saveTicket(name, src, dest, bestFare, recipientEmail, travelDate);

                String subject = "Metro App: Your Ticket Confirmation from " + src + " to " + dest;
                String body = "Dear " + name + ",\n\n"
                        + "Thank you for booking your metro ticket with us! We are happy to confirm the details of your journey.\n\n"
                        + "Booking Summary:\n"
                        + "--------------------\n"
                        + "Source Station: " + src + "\n"
                        + "Destination Station: " + dest + "\n"
                        + "Date of Travel: " + travelDate + "\n"
                        + "Fare: Rs. " + bestFare + "\n\n"
                        + "We wish you a pleasant and safe journey. We hope to see you again soon to book your next ticket!\n\n"
                        + "Best regards,\n\n"
                        + "The Metro App Team";

                // Hardcoded sender credentials for default sending: amansrivastava1511@gmail.com
                final String senderEmail = "amansrivastava1511@gmail.com"; // Default sender email
                // Using the actual App Password from the screenshot provided earlier
                final String senderPassword = "jjol agmb lywn eurn";
                final String smtpHost = "smtp.gmail.com";
                final String smtpPort = "587";

                EmailSender.sendEmail(senderEmail, senderPassword, smtpHost, smtpPort, recipientEmail, subject, body);

                JOptionPane.showMessageDialog(frame,
                        "<html><h3 style='color: green;'>Ticket booked successfully!</h3>"
                        + "<p>Ticket details sent to " + recipientEmail + "</p>"
                        + "<b>Have a safe journey and visit again!</b></html>",
                        "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Ticket booking cancelled.", "Booking Cancelled", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void showDatePickerDialog() {
        JDialog dateDialog = new JDialog(frame, "Select Travel Date", true);
        dateDialog.setSize(350, 300);
        dateDialog.setLocationRelativeTo(frame);

        JPanel calendarPanel = new JPanel(new BorderLayout());
        JPanel headerPanel = new JPanel(new BorderLayout());

        JButton prevMonthButton = new JButton("◀");
        JButton nextMonthButton = new JButton("▶");
        JLabel monthYearLabel = new JLabel("", SwingConstants.CENTER);
        headerPanel.add(prevMonthButton, BorderLayout.WEST);
        headerPanel.add(monthYearLabel, BorderLayout.CENTER);
        headerPanel.add(nextMonthButton, BorderLayout.EAST);
        calendarPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel daysPanel = new JPanel(new GridLayout(0, 7, 2, 2));
        calendarPanel.add(daysPanel, BorderLayout.CENTER);

        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

        final LocalDate[] monthView = {selectedDate};
        final LocalDate today = LocalDate.now(java.time.ZoneId.of("Asia/Kolkata"));

        Runnable updateCalendar = () -> {
            daysPanel.removeAll();

            for (String day : dayNames) {
                JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
                dayLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
                daysPanel.add(dayLabel);
            }

            monthYearLabel.setText(monthView[0].getMonth() + " " + monthView[0].getYear());

            LocalDate firstDayOfMonth = monthView[0].withDayOfMonth(1);
            DayOfWeek dayOfWeek = firstDayOfMonth.getDayOfWeek();
            int startDayIndex = (dayOfWeek.getValue() % 7);

            for (int i = 0; i < startDayIndex; i++) {
                daysPanel.add(new JLabel(""));
            }

            for (int i = 1; i <= monthView[0].lengthOfMonth(); i++) {
                JButton dayButton = new JButton(String.valueOf(i));

                LocalDate currentDayInView = monthView[0].withDayOfMonth(i);

                // Disable if the current day in view is before today's date
                if (currentDayInView.isBefore(today)) {
                    dayButton.setEnabled(false);
                    dayButton.setForeground(Color.LIGHT_GRAY); // Make text lighter to indicate disabled state
                } else if (currentDayInView.isEqual(today)) {
                    dayButton.setBackground(new Color(173, 216, 230)); // Light blue for today
                    dayButton.setOpaque(true);
                    dayButton.setBorderPainted(false);
                    dayButton.setFont(dayButton.getFont().deriveFont(Font.BOLD)); // Bold today's date
                }

                daysPanel.add(dayButton); // Add button after setting its state

                final int day = i;
                dayButton.addActionListener(e -> {
                    // This action listener will only fire if dayButton is enabled
                    selectedDate = monthView[0].withDayOfMonth(day);
                    dateDisplayField.setText(selectedDate.toString());
                    dateDialog.dispose();
                });
            }

            // Disable prevMonthButton if the current calendar view is the current month or a past month
            // It will also check if the *displayed* first day of the month is before or equal to today's first day of the month
            if (monthView[0].withDayOfMonth(1).isBefore(today.withDayOfMonth(1)) || monthView[0].withDayOfMonth(1).isEqual(today.withDayOfMonth(1))) {
                prevMonthButton.setEnabled(false);
            } else {
                prevMonthButton.setEnabled(true);
            }

            dateDialog.revalidate();
            dateDialog.repaint();
        };

        prevMonthButton.addActionListener(e -> {
            // The enable/disable logic for the button itself is in updateCalendar.
            // This listener just handles the actual month change.
            monthView[0] = monthView[0].minusMonths(1);
            updateCalendar.run();
        });
        nextMonthButton.addActionListener(e -> {
            monthView[0] = monthView[0].plusMonths(1);
            updateCalendar.run();
        });

        updateCalendar.run();
        dateDialog.getContentPane().add(calendarPanel);
        dateDialog.setVisible(true);
    }

    private void showWelcomePage() {
        JDialog welcomeDialog = new JDialog(frame, "Welcome to Metro App", true);
        welcomeDialog.setSize(500, 350);
        welcomeDialog.setLocationRelativeTo(null);
        welcomeDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(new Color(230, 240, 250));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel welcomeLabel = new JLabel("<html><h1 style='color: #004080; text-align: center; font-size: 32px;'>Welcome to Metro App!</h1><p style='color: #333333; text-align: center; font-size: 16px;'>Your ultimate guide for metro routes and fares.</p><p style='color: #666666; text-align: center; font-size: 14px;'>Plan your journey, view all routes, and book tickets instantly.</p></html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        JButton startButton = new JButton("Start Exploring");
        startButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        startButton.setBackground(new Color(150, 255, 150));
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> {
            welcomeDialog.dispose();
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(230, 240, 250));
        buttonPanel.add(startButton);

        panel.add(welcomeLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        welcomeDialog.getContentPane().add(panel);
        welcomeDialog.setVisible(true);
    }

    private String getFormattedRoutes(List<Graph_M.PathResult> routes, boolean isEligible) {
        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("--- Top 5 Available Routes (Sorted by Shortest Distance/Time) ---\n\n");

        int routeNum = 1;
        for (int j = 0; j < Math.min(routes.size(), 5); j++) {
            Graph_M.PathResult route = routes.get(j);

            int fare = calculateFare(route.cost);
            String fareString = "Rs. " + fare;
            if (isEligible) {
                fare = (int) (fare * 0.85);
                fareString = "Rs. " + fare + " (15% discounted)";
            }

            int estimatedTimeMinutes = route.cost;

            resultBuilder.append("Route ").append(routeNum++).append(":\n");
            resultBuilder.append("  Path: ");
            for (int i = 0; i < route.path.size(); i++) {
                resultBuilder.append(route.path.get(i));
                if (i < route.path.size() - 1) {
                    String currentStation = route.path.get(i);
                    String nextStation = route.path.get(i + 1);
                    String line = graph.getLine(currentStation, nextStation);
                    resultBuilder.append(" (").append(line).append(" Line) -> ");
                }
            }
            resultBuilder.append("\n  Distance: ").append(route.cost).append(" units")
                    .append(" | Est. Time: ").append(estimatedTimeMinutes).append(" minutes")
                    .append(" | Fare: ").append(fareString);

            String prevLine = null;
            StringBuilder interchanges = new StringBuilder();
            for (int i = 0; i < route.path.size(); i++) {
                String currentStation = route.path.get(i);
                if (i < route.path.size() - 1) {
                    String nextStation = route.path.get(i + 1);
                    String currentSegmentLine = graph.getLine(currentStation, nextStation);
                    if (prevLine != null && !prevLine.equals(currentSegmentLine)) {
                        interchanges.append(currentStation).append(" (").append(prevLine).append(" to ").append(currentSegmentLine).append("), ");
                    }
                    prevLine = currentSegmentLine;
                }
            }
            if (interchanges.length() > 0) {
                interchanges.setLength(interchanges.length() - 2);
                resultBuilder.append("\n  Interchanges: ").append(interchanges.toString());
            } else {
                resultBuilder.append("\n  Interchanges: None");
            }
            resultBuilder.append("\n\n");
        }
        return resultBuilder.toString();
    }

    private int calculateFare(int cost) {
        if (cost <= 4) {
            return 10;
        } else if (cost <= 8) {
            return 20;
        } else if (cost <= 12) {
            return 30;
        } else if (cost <= 16) {
            return 40;
        } else {
            return 50;
        }
    }

    private void saveTicket(String name, String src, String dest, int fare, String email, String travelDate) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/metro", "root", "Aman1511@")) {
            String sql = "INSERT INTO tickets (name, source, destination, fare, email, travel_date) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, src);
                ps.setString(3, dest);
                ps.setInt(4, fare);
                ps.setString(5, email);
                ps.setString(6, travelDate);
                ps.executeUpdate();
                System.out.println("Ticket saved successfully to DB.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving ticket to database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(MetroApp::new);
    }
}
