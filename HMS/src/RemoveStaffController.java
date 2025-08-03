import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.sql.*;

public class RemoveStaffController {

    @FXML
    private TextField TF_Reason;

    @FXML
    private TextField TF_StaffIDCNIC;

    private Connection conn;

    public void initialize() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "saadkhan2005");
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to connect to database.");
            e.printStackTrace();
        }
    }

    @FXML
    void btnClearAll(ActionEvent event) {
        TF_Reason.clear();
        TF_StaffIDCNIC.clear();
    }

    @FXML
    void btnRemove(ActionEvent event) {
        String input = TF_StaffIDCNIC.getText().trim();

        if (input.isEmpty() || TF_Reason.getText().trim().isEmpty()) {
            showAlert("Input Error", "Please provide StaffID/CNIC and Reason.");
            return;
        }

        try {
            if (input.length() == 13) {
                removeByCNIC(input);
            } else {
                int empId = Integer.parseInt(input);
                removeByEmployeeID(empId);
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Enter a valid 13-digit CNIC or numeric Employee ID.");
        } catch (SQLException e) {
            showAlert("Database Error", "An error occurred while removing the staff.");
            e.printStackTrace();
        }
    }

    private void removeByCNIC(String cnic) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT EmployeeID FROM Employee WHERE CNIC = ?");
        ps.setString(1, cnic);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            int empId = rs.getInt("EmployeeID");
            removeByEmployeeID(empId);
        } else {
            showAlert("Not Found", "No employee found with CNIC: " + cnic);
        }
    }

    private void removeByEmployeeID(int empId) throws SQLException {
        // Check if employee is caretaker
        PreparedStatement checkCaretaker = conn.prepareStatement("SELECT UserID FROM Caretaker WHERE EmployeeID = ?");
        checkCaretaker.setInt(1, empId);
        ResultSet rs = checkCaretaker.executeQuery();

        Integer userId = null;
        if (rs.next()) {
            userId = rs.getInt("UserID");
        }

        // Delete from Employee (will cascade to Caretaker/MessHead/OtherStaff/Manager/etc.)
        PreparedStatement deleteEmp = conn.prepareStatement("DELETE FROM Employee WHERE EmployeeID = ?");
        deleteEmp.setInt(1, empId);
        int affected = deleteEmp.executeUpdate();

        if (affected > 0) {
            if (userId != null) {
                // Delete from User table if caretaker
                PreparedStatement deleteUser = conn.prepareStatement("DELETE FROM User WHERE UserID = ?");
                deleteUser.setInt(1, userId);
                deleteUser.executeUpdate();
            }
            showAlert("Success", "Employee removed successfully.");
            btnClearAll(null);
        } else {
            showAlert("Not Found", "No employee found with EmployeeID: " + empId);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
