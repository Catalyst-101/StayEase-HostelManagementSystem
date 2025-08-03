import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.*;
import java.time.LocalDate;

public class Staff2Controller {

    @FXML
    private TextField TF_reason;

    @FXML
    private TextField TF_staffid; // This can be either EmployeeID or CNIC

    @FXML
    private DatePicker leavedate;

    @FXML
    private DatePicker returndate;

    @FXML
    void btnClear(ActionEvent event) {
        TF_staffid.clear();
        TF_reason.clear();
        leavedate.setValue(null);
        returndate.setValue(null);
    }

    @FXML
    void btnSave(ActionEvent event) {
        String input = TF_staffid.getText().trim();
        String reason = TF_reason.getText().trim();
        LocalDate leave = leavedate.getValue();
        LocalDate ret = returndate.getValue();

        if (input.isEmpty() || reason.isEmpty() || leave == null || ret == null) {
            showAlert("Validation Error", "All fields are required.");
            return;
        }

        if (ret.isBefore(leave)) {
            showAlert("Validation Error", "Return date cannot be before leave date.");
            return;
        }

        try {
            Integer employeeId = resolveEmployeeId(input);
            if (employeeId == null) {
                showAlert("Error", "Employee not found for ID/CNIC: " + input);
                return;
            }

            if (hasOverlappingLeave(employeeId, leave, ret)) {
                showAlert("Error", "Overlapping leave already exists for this staff member.");
                return;
            }

            saveStaffLeave(employeeId, leave, ret, reason);
            showAlert("Success", "Leave successfully recorded.");
            btnClear(null);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", e.getMessage());
        }
    }

    // Resolve EmployeeID from CNIC or direct ID input
    private Integer resolveEmployeeId(String input) throws SQLException {
        String sql = "SELECT EmployeeID FROM Employee WHERE EmployeeID = ? OR CNIC = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, input);
            pstmt.setString(2, input);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("EmployeeID");
            }
        }
        return null;
    }

    // Check for overlapping leave
    private boolean hasOverlappingLeave(int empId, LocalDate leave, LocalDate ret) throws SQLException {
        String sql = "SELECT 1 FROM StaffLeave WHERE EmployeeID = ? AND " +
                     "((LeaveDate <= ? AND ReturnDate >= ?) OR (LeaveDate <= ? AND ReturnDate >= ?))";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            java.sql.Date sqlLeave = java.sql.Date.valueOf(leave);
            java.sql.Date sqlReturn = java.sql.Date.valueOf(ret); 
        
            pstmt.setInt(1, empId);
            pstmt.setDate(2, sqlReturn);  // ReturnDate >= LeaveDate
            pstmt.setDate(3, sqlLeave);   // LeaveDate <= ReturnDate
            pstmt.setDate(4, sqlLeave);   // LeaveDate <= ReturnDate
            pstmt.setDate(5, sqlReturn);  // ReturnDate >= LeaveDate

            return pstmt.executeQuery().next();
        }
    }

    // Insert leave into the database
    private void saveStaffLeave(int empId, LocalDate leave, LocalDate ret, String reason) throws SQLException {
        String sql = "INSERT INTO StaffLeave (EmployeeID, ApplyDate, LeaveDate, ReturnDate, Reason) " +
                     "VALUES (?, CURDATE(), ?, ?, ?)";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, empId);
            pstmt.setDate(2, Date.valueOf(leave));
            pstmt.setDate(3, Date.valueOf(ret));
            pstmt.setString(4, reason);
            pstmt.executeUpdate();
        }
    }

    // Utility: show alert
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
