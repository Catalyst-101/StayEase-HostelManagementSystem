import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.*;
import java.time.LocalDate;

public class ViewStaffController {

    @FXML
    private TextField TF_sid_cnic;

    @FXML
    private Text staffAddress, staffCNIC, staffEmail, staffEmploymentDate,
                 staffFullName, staffGender, staffJoiningDate,
                 staffPhone, staffPosition, staffStatus;

    private final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagementsystem";
    private final String DB_USER = "root";
    private final String DB_PASS = "saadkhan2005";

    @FXML
    void btnClear(ActionEvent event) {
        TF_sid_cnic.clear();
        staffAddress.setText("");
        staffCNIC.setText("");
        staffEmail.setText("");
        staffEmploymentDate.setText("");
        staffFullName.setText("");
        staffGender.setText("");
        staffJoiningDate.setText("");
        staffPhone.setText("");
        staffPosition.setText("");
        staffStatus.setText("");
    }

    @FXML
    void btnSearch(ActionEvent event) {
        String input = TF_sid_cnic.getText().trim();
        if (input.isEmpty()) {
            staffStatus.setText("Please enter Employee ID or CNIC.");
            return;
        }

        String query = input.length() == 13 ? 
            "SELECT * FROM Employee WHERE CNIC = ?" : 
            "SELECT * FROM Employee WHERE EmployeeID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (input.length() == 13)
                stmt.setString(1, input);
            else
                stmt.setInt(1, Integer.parseInt(input));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int employeeID = rs.getInt("EmployeeID");

                // Set staff fields
                String fullName = rs.getString("FirstName") + " " +
                                  (rs.getString("MiddleName") != null ? rs.getString("MiddleName") : "") + " " +
                                  rs.getString("LastName");
                staffFullName.setText("Name: " + fullName.trim());
                staffCNIC.setText("CNIC: " + rs.getString("CNIC"));
                staffGender.setText("Gender: " + rs.getString("Gender"));
                staffPhone.setText("Phone: " + rs.getString("Phone"));
                staffEmail.setText("Email: " + rs.getString("Email"));
                staffAddress.setText("Address: " + rs.getString("Address"));
                staffEmploymentDate.setText("Employement Date: " + String.valueOf(rs.getDate("EmploymentDate")));
                staffJoiningDate.setText("Joining Date: " + String.valueOf(rs.getDate("JoiningDate")));

                // Determine position
                String position = getStaffPosition(conn, employeeID);
                staffPosition.setText("Position: " + position);

                // Determine status
                staffStatus.setText("Availability: " + (isOnLeave(conn, employeeID) ? "Not Available (On Leave)" : "Available"));
            } else {
                staffStatus.setText("Staff not found.");
                clearInfo();
            }

        } catch (Exception e) {
            e.printStackTrace();
            staffStatus.setText("Error occurred while fetching data.");
        }
    }

    private void clearInfo() {
        staffAddress.setText("");
        staffCNIC.setText("");
        staffEmail.setText("");
        staffEmploymentDate.setText("");
        staffFullName.setText("");
        staffGender.setText("");
        staffJoiningDate.setText("");
        staffPhone.setText("");
        staffPosition.setText("");
    }

    private String getStaffPosition(Connection conn, int employeeID) throws SQLException {
        if (recordExists(conn, "SELECT * FROM Manager WHERE EmployeeID = ?", employeeID)) return "Manager";
        if (recordExists(conn, "SELECT * FROM Caretaker WHERE EmployeeID = ?", employeeID)) return "Caretaker";
        if (recordExists(conn, "SELECT * FROM MessHead WHERE EmployeeID = ?", employeeID)) return "Mess Head";
        if (recordExists(conn, "SELECT * FROM OtherStaff WHERE EmployeeID = ?", employeeID)) return "Other Staff";
        return "Unknown";
    }

    private boolean isOnLeave(Connection conn, int employeeID) throws SQLException {
        String query = "SELECT * FROM StaffLeave WHERE EmployeeID = ? AND ? BETWEEN LeaveDate AND ReturnDate";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeID);
            stmt.setDate(2, Date.valueOf(LocalDate.now()));
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    private boolean recordExists(Connection conn, String query, int employeeID) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, employeeID);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }
}
