import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class GenInvoiceController {

    @FXML
    private TextField Amount;

    @FXML
    private DatePicker DueDate;

    @FXML
    private DatePicker IssueDate;

    @FXML
    private MenuButton tfInvoiceType;

    @FXML
    private TextField tfStudentID;

    private String selectedType = null;

    private double amount;

    @FXML
    void initialize() {
        tfStudentID.addEventFilter(KeyEvent.KEY_TYPED, this::validateNumericInput);
        Amount.addEventFilter(KeyEvent.KEY_TYPED, this::validateNumericInput);

        IssueDate.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                DueDate.setValue(newVal.plusDays(15));  // Default 15 days for due date
            }
        });
    }

    private void validateNumericInput(KeyEvent event) {
        if (!event.getCharacter().matches("\\d")) {
            event.consume();
        }
    }

    @FXML
    void btnAdd(ActionEvent event) {
        String studentIdText = tfStudentID.getText().trim();
        String amountText = Amount.getText().trim();
        LocalDate issueDate = IssueDate.getValue();
        LocalDate dueDate = DueDate.getValue();

        if (studentIdText.isEmpty() || issueDate == null || dueDate == null || selectedType == null) {
            showAlert(Alert.AlertType.ERROR, "Missing Data", "Please fill in all fields.");
            return;
        }

        if (!(studentIdText.matches("\\d{7}") || studentIdText.matches("\\d{13}"))) {
            showAlert(Alert.AlertType.ERROR, "Invalid Student ID", "Student ID must be either 7 or 13 digits.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "saadkhan2005")) {
            // Get StudentID if CNIC was provided
            String getStudentQuery = studentIdText.length() == 13
                ? "SELECT s.StudentID, s.RoomID FROM Student s JOIN User u ON s.UserID = u.UserID WHERE u.CNIC = ?"
                : "SELECT StudentID, RoomID FROM Student WHERE StudentID = ?";

            int studentID = -1;
            int roomID = -1;

            try (PreparedStatement ps = conn.prepareStatement(getStudentQuery)) {
                ps.setString(1, studentIdText);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    studentID = rs.getInt("StudentID");
                    roomID = rs.getInt("RoomID");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Student Not Found", "No student found with given ID or CNIC.");
                    return;
                }
            }

            double calculatedAmount = 0;

            if (selectedType.equals("Rent")) {
                // Rent calculation: Check room capacity (2 or 3 seater)
                String capacityQuery = "SELECT Capacity FROM Room WHERE RoomID = ?";
                try (PreparedStatement ps = conn.prepareStatement(capacityQuery)) {
                    ps.setInt(1, roomID);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int capacity = rs.getInt("Capacity");
                        if (capacity == 3) {
                            calculatedAmount = 7500; // Rent for 3-seater
                        } else if (capacity == 2) {
                            calculatedAmount = 10000; // Rent for 2-seater
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Room Error", "Room capacity not found.");
                            return;
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Room Error", "Room not found for the student.");
                        return;
                    }
                }
            } else if (selectedType.equals("Mess")) {
                // Mess calculation: Default 15190, reduced by 490 per mess off day in the month
                int daysOff = 0;
                LocalDate startOfMonth = issueDate.withDayOfMonth(1);
                LocalDate endOfMonth = issueDate.withDayOfMonth(issueDate.lengthOfMonth());

                String messQuery = "SELECT LeaveDate, JoinDate FROM MessStatus WHERE StudentID = ? AND " +
                        "(LeaveDate <= ? AND JoinDate >= ?)";
                try (PreparedStatement ps = conn.prepareStatement(messQuery)) {
                    ps.setInt(1, studentID);
                    ps.setDate(2, Date.valueOf(endOfMonth));
                    ps.setDate(3, Date.valueOf(startOfMonth));
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        LocalDate leave = rs.getDate("LeaveDate").toLocalDate();
                        LocalDate join = rs.getDate("JoinDate").toLocalDate();

                        LocalDate from = leave.isBefore(startOfMonth) ? startOfMonth : leave;
                        LocalDate to = join.isAfter(endOfMonth) ? endOfMonth : join;
                        daysOff += ChronoUnit.DAYS.between(from, to);
                    }
                }

                calculatedAmount = 15190 - (daysOff * 490);  // Default mess fee minus the mess-off days
                amount = calculatedAmount;

            } else if (selectedType.equals("Other")) {
                if (amountText.isEmpty() || !amountText.matches("\\d+")) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Enter a valid amount for 'Other' invoice.");
                    return;
                }
                calculatedAmount = Double.parseDouble(amountText);  // Custom amount for other invoice types
            }

            String insertQuery = "INSERT INTO Invoice (StudentID, InvoiceType, DueDate, Amount) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertQuery)) {
                ps.setInt(1, studentID);
                ps.setString(2, selectedType);
                ps.setDate(3, Date.valueOf(dueDate));
                ps.setDouble(4, calculatedAmount);
                ps.executeUpdate();
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Invoice generated and saved successfully!");
            btnClearAll(event);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not connect to the database or run query.");
        }
    }

    private void showAlert(Alert.AlertType type, String header, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(header);
        alert.showAndWait();
    }

    @FXML
    void btnClearAll(ActionEvent event) {
        tfStudentID.clear();
        Amount.clear();
        IssueDate.setValue(null);
        DueDate.setValue(null);
        tfInvoiceType.setText("Select Type");
        selectedType = null;
        Amount.setEditable(true); // Reset to editable when clearing
    }

    @FXML
    void btnDueDate(ActionEvent event) {
        DueDate.show();
    }

    @FXML
    void btnInvoiceType(ActionEvent event) {
        tfInvoiceType.show();
    }

    @FXML
    void btnIssueDate(ActionEvent event) {
        IssueDate.show();
    }

    @FXML
    void handleInvoiceType(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        String type = source.getText();
        selectedType = type;
        tfInvoiceType.setText(type);

        if (selectedType.equals("Rent")) {
            Amount.setEditable(false);
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "saadkhan2005")) {
                String studentIdText = tfStudentID.getText().trim();
                if (studentIdText.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Missing Student ID", "Please enter a valid Student ID or CNIC.");
                    return;
                }

                String getStudentQuery = studentIdText.length() == 13
                        ? "SELECT s.RoomID FROM Student s JOIN User u ON s.UserID = u.UserID WHERE u.CNIC = ?"
                        : "SELECT RoomID FROM Student WHERE StudentID = ?";

                int roomID = -1;
                try (PreparedStatement ps = conn.prepareStatement(getStudentQuery)) {
                    ps.setString(1, studentIdText);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        roomID = rs.getInt("RoomID");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Student Not Found", "No student found with the given ID or CNIC.");
                        return;
                    }
                }

                // Fetch room capacity
                String capacityQuery = "SELECT Capacity FROM Room WHERE RoomID = ?";
                double rentAmount = 0;
                try (PreparedStatement ps = conn.prepareStatement(capacityQuery)) {
                    ps.setInt(1, roomID);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        int capacity = rs.getInt("Capacity");
                        rentAmount = (capacity == 3) ? 7500 : 10000;  // Rent based on capacity
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Room Error", "Room not found for the student.");
                        return;
                    }
                }

                Amount.setText(String.valueOf(rentAmount));
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Could not fetch room details.");
            }
        } else if (selectedType.equals("Mess")) {
            Amount.setText(String.valueOf((int)amount));
            Amount.setEditable(false);
        } else if (selectedType.equals("Other")) {
            Amount.clear();
            Amount.setEditable(true);  // Allow editing for "Other"
        }
    }
    
    @FXML
    void btnName(ActionEvent event) {

    }
}
