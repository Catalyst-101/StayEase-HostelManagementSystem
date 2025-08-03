import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TakeAttendenceController {
    @FXML private TextField id_cnic;
    @FXML private DatePicker att_date;
    @FXML private ComboBox<String> attendanceStatus;
    @FXML private Text t_progressMessage;

    @FXML
    private void initialize() {
        att_date.setValue(LocalDate.now());

        attendanceStatus.getItems().clear();
        attendanceStatus.getItems().addAll("Present", "Absent", "Leave");
    }

    @FXML
    private void btnSave() {
        try {
            if (!validateInputs()) {
                return;
            }

            String input = id_cnic.getText().trim();
            String studentId = resolveStudentID(input);
            if (studentId == null) {
                showAlert("Error", "Student Not Found", "Student with this ID/CNIC does not exist");
                return;
            }


            if (attendanceAlreadyRecorded(studentId, att_date.getValue())) {
                showAlert("Error", "Duplicate Entry", "Attendance already recorded for this student on selected date");
                return;
            }

            // Save attendance first
            if (saveAttendanceToDatabase(studentId)) {
                // Only get student name after successful save
                String studentName = getStudentName(studentId);
                System.out.println("Setting progress message: " + "Attendance recorded for " + studentName + "...");
                t_progressMessage.setStyle("-fx-fill: green;");
                t_progressMessage.setText("Attendance recorded for " + studentName + " (ID/CNIC: " + studentId + ")");

                // Clear only the input fields, not the progress message
                id_cnic.clear();
                att_date.setValue(LocalDate.now());
                attendanceStatus.getSelectionModel().clearSelection();
            } else {
                showAlert("Error", "Save Failed", "Failed to save attendance record");
            }
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred", e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Error", "Unexpected Error", e.getMessage());
            e.printStackTrace();
        }
    }

    private boolean validateInputs() {
        if (id_cnic.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Missing Information", "Please enter Student ID/CNIC");
            return false;
        }

        if (attendanceStatus.getValue() == null) {
            showAlert("Validation Error", "Missing Information", "Please select attendance status");
            return false;
        }

        return true;
    }

    // private boolean studentExist(String studentId) throws SQLException {
    //     String sql = "SELECT 1 FROM student s JOIN user u USING(UserID) WHERE s.StudentID = ? OR u.CNIC = ?";
    //     try (Connection conn = DBHelper.getConnection();
    //          PreparedStatement pstmt = conn.prepareStatement(sql)) {
    //         pstmt.setString(1, studentId);
    //         pstmt.setString(2, studentId);
    //         return pstmt.executeQuery().next();
    //     }
    // }

    private String resolveStudentID(String input) throws SQLException {
        // If the input is already a 7-digit StudentID, return it
        if (input.matches("\\d{7}")) {
            return input;
        }
    
        // Otherwise, try to get the StudentID using CNIC
        String sql = "SELECT s.StudentID FROM student s JOIN user u USING(UserID) WHERE u.CNIC = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, input);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("StudentID");
            }
        }
        return null; // not found
    }
    

    private String getStudentName(String studentId) throws SQLException {
        String sql = "SELECT CONCAT(u.FirstName, ' ', COALESCE(u.MiddleName, ''), ' ', u.LastName) AS Name " +
                "FROM student s JOIN user u USING(UserID) WHERE s.StudentID = ? OR u.CNIC = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setString(2, studentId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Name").trim().replaceAll("\\s+", " ");
            }
            return "Unknown Student";
        }
    }

    private boolean attendanceAlreadyRecorded(String studentId, LocalDate date) throws SQLException {
        String sql = "SELECT 1 FROM attendance WHERE StudentID = ? AND AttendanceDate = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setDate(2, java.sql.Date.valueOf(date));
            return pstmt.executeQuery().next();
        }
    }

    private boolean saveAttendanceToDatabase(String studentId) throws SQLException {
        String sql = "INSERT INTO attendance(StudentID, AttendanceDate, Status, RecordedBy) " +
                "VALUES(?, ?, ?, ?)";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, studentId);
            pstmt.setDate(2, java.sql.Date.valueOf(att_date.getValue()));
            pstmt.setString(3, attendanceStatus.getValue());
            pstmt.setInt(4, 0); // Adjust based on authentication system

            return pstmt.executeUpdate() > 0;
        }
    }

    @FXML
    private void btnClearAll() {
        id_cnic.clear();
        att_date.setValue(LocalDate.now());
        attendanceStatus.getSelectionModel().clearSelection();
        t_progressMessage.setText("");
    }

    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private String getCurrentUser() {
        // Replace this with actual user retrieval logic
        return "null";
    }
}
