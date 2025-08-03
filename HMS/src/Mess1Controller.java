import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Mess1Controller {

    @FXML
    private DatePicker dp_leaveDate;

    @FXML
    private DatePicker dp_returnDate;

    @FXML
    private Text t_progressMessage;

    @FXML
    private TextField tf_roomNo;

    @FXML
    private TextField tf_studentID;

    private Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "saadkhan2005");
    }

    @FXML
    void btnClearAll(ActionEvent event) {
        tf_studentID.clear();
        tf_roomNo.clear();
        dp_leaveDate.setValue(null);
        dp_returnDate.setValue(null);
        t_progressMessage.setText("");
    }

    @FXML
    void btnSave(ActionEvent event) {
        String studentIdInput = tf_studentID.getText().trim();
        String roomNoInput = tf_roomNo.getText().trim();
        LocalDate leaveDate = dp_leaveDate.getValue();
        LocalDate returnDate = dp_returnDate.getValue();

        if (studentIdInput.isEmpty() || roomNoInput.isEmpty() || leaveDate == null || returnDate == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        if (!studentIdInput.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Student ID", "Student ID must contain only digits.");
            return;
        }

        if (!(studentIdInput.length() == 7 || studentIdInput.length() == 13)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Student ID", "Must be 7-digit (Student ID) or 13-digit (CNIC).");
            return;
        }

        if (returnDate.isBefore(leaveDate)) {
            showAlert(Alert.AlertType.ERROR, "Date Error", "Return date cannot be before leave date.");
            return;
        }

        try (Connection conn = connect()) {
            // Step 1: Find Student
            int studentId = -1;
            PreparedStatement ps;
            if (studentIdInput.length() == 7) {
                ps = conn.prepareStatement("SELECT StudentID, RoomID FROM Student WHERE StudentID = ?");
                ps.setInt(1, Integer.parseInt(studentIdInput));
            } else {
                ps = conn.prepareStatement(
                        "SELECT s.StudentID, s.RoomID FROM Student s JOIN User u ON s.UserID = u.UserID WHERE u.CNIC = ?");
                ps.setString(1, studentIdInput);
            }
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Student Not Found", "No matching student found.");
                return;
            }

            studentId = rs.getInt("StudentID");
            int actualRoomID = rs.getInt("RoomID");

            // Step 2: Validate Room No
            ps = conn.prepareStatement("SELECT RoomID FROM Room WHERE RoomNumber = ?");
            ps.setString(1, roomNoInput);
            rs = ps.executeQuery();

            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Room Not Found", "No such room exists.");
                return;
            }

            int enteredRoomID = rs.getInt("RoomID");
            if (enteredRoomID != actualRoomID) {
                showAlert(Alert.AlertType.ERROR, "Room Mismatch", "Student is not assigned to this room.");
                return;
            }

            // Step 3: Check for overlapping Mess Off entries
            ps = conn.prepareStatement(
                    "SELECT * FROM MessStatus WHERE StudentID = ? AND NOT (JoinDate < ? OR LeaveDate > ?)");
            ps.setInt(1, studentId);
            ps.setDate(2, Date.valueOf(leaveDate));
            ps.setDate(3, Date.valueOf(returnDate));
            rs = ps.executeQuery();

            if (rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Date Overlap",
                        "This student already has a mess off entry overlapping these dates.");
                return;
            }

            // Step 4: Check monthly limit (max 12 days)
            ps = conn.prepareStatement(
                    "SELECT SUM(DATEDIFF(JoinDate, LeaveDate) + 1) AS TotalDays FROM MessStatus " +
                            "WHERE StudentID = ? AND MONTH(LeaveDate) = ? AND YEAR(LeaveDate) = ?");
            ps.setInt(1, studentId);
            ps.setInt(2, leaveDate.getMonthValue());
            ps.setInt(3, leaveDate.getYear());
            rs = ps.executeQuery();

            int alreadyOffDays = 0;
            if (rs.next()) {
                alreadyOffDays = rs.getInt("TotalDays");
            }

            int newOffDays = (int) ChronoUnit.DAYS.between(leaveDate, returnDate) + 1;
            if (alreadyOffDays + newOffDays > 12) {
                showAlert(Alert.AlertType.ERROR, "Limit Exceeded",
                        "Mess off days exceed monthly limit of 12. Already used: " + alreadyOffDays);
                return;
            }

            CallableStatement cs = conn.prepareCall("{ call AddMessStatus(?, ?, ?, ?) }");
            cs.setInt(1, studentId);
            cs.setDate(2, Date.valueOf(leaveDate));
            cs.setDate(3, Date.valueOf(returnDate));
            cs.setString(4, "User requested via system");
            cs.execute();


            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Mess turned off for Student ID: " + studentIdInput +
                            "\nRoom No: " + roomNoInput +
                            "\nFrom: " + leaveDate +
                            "\nTo: " + returnDate +
                            "\nTotal Days Off: " + newOffDays);

            t_progressMessage.setText("Saved successfully!");
            btnClearAll(null);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
