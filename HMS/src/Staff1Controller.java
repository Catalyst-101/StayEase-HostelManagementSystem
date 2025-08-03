import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import java.sql.*;
import java.util.regex.Pattern;

public class Staff1Controller {

    @FXML private MenuButton StaffType;
    @FXML private MenuButton gender;
    @FXML private TextField fullName, cnic, phone, email, address, email1, hostelid;
    @FXML private DatePicker employmentDate, joiningDate;

    private String selectedGender = null;
    private String selectedStaffType = null;

    // Database credentials
    private final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagementsystem";
    private final String DB_USER = "root";
    private final String DB_PASS = "saadkhan2005";

    @FXML
    void handleGenderSelect(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        selectedGender = item.getText();
        gender.setText(selectedGender);
    }

    @FXML
    void handleStaffType(ActionEvent event) {
        MenuItem item = (MenuItem) event.getSource();
        selectedStaffType = item.getText();
        StaffType.setText(selectedStaffType);
    }

    @FXML
    void addButton(ActionEvent event) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            String name = fullName.getText().trim();
            String[] nameParts = name.split(" ");
            if (nameParts.length != 2 && nameParts.length != 3) {
                showAlert("Error", "Name must be First + Last or First + Middle + Last.");
                return;
            }
            String firstName = nameParts[0];
            String middleName = nameParts.length == 3 ? nameParts[1] : null;
            String lastName = nameParts[nameParts.length - 1];

            String cnicValue = cnic.getText().trim();
            if (!cnicValue.matches("\\d{13}")) {
                showAlert("Error", "CNIC must be exactly 13 digits.");
                return;
            }
            if (exists(conn, "SELECT * FROM Employee WHERE CNIC = ?", cnicValue)) {
                showAlert("Error", "CNIC already exists.");
                return;
            }
            if (exists(conn, "SELECT * FROM User WHERE CNIC = ?", cnicValue) && selectedStaffType != "Caretaker") {
                showAlert("Error", "CNIC already exists.");
                return;
            }

            if (selectedGender == null) {
                showAlert("Error", "Please select a gender.");
                return;
            }

            String phoneValue = phone.getText().trim();
            if (!phoneValue.matches("\\d+")) {
                showAlert("Error", "Phone number must contain only digits.");
                return;
            }

            String emailValue = email.getText().trim();
            if (!emailValue.matches("^[\\w.-]+@gmail\\.com$")) {
                showAlert("Error", "Email must be a valid Gmail address (e.g., example@gmail.com).");
                return;
            }
            if (exists(conn, "SELECT * FROM Employee WHERE Email = ?", emailValue)) {
                showAlert("Error", "Email already exists.");
                return;
            }

            int hostelId;
            try {
                hostelId = Integer.parseInt(hostelid.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Error", "Hostel ID must be a number.");
                return;
            }
            if (!exists(conn, "SELECT * FROM Hostel WHERE HostelID = ?", String.valueOf(hostelId))) {
                showAlert("Error", "Hostel ID not found.");
                return;
            }

            Date empDate = Date.valueOf(employmentDate.getValue());
            Date joinDate = Date.valueOf(joiningDate.getValue());
            String addressVal = address.getText().trim();

            // Step 1: Insert into Employee
            PreparedStatement empStmt = conn.prepareStatement(
                    "INSERT INTO Employee (FirstName, MiddleName, LastName, CNIC, Gender, Phone, Email, Address, EmploymentDate, JoiningDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            empStmt.setString(1, firstName);
            empStmt.setString(2, middleName);
            empStmt.setString(3, lastName);
            empStmt.setString(4, cnicValue);
            empStmt.setString(5, selectedGender);
            empStmt.setString(6, phoneValue);
            empStmt.setString(7, emailValue);
            empStmt.setString(8, addressVal);
            empStmt.setDate(9, empDate);
            empStmt.setDate(10, joinDate);
            empStmt.executeUpdate();
            ResultSet rsEmp = empStmt.getGeneratedKeys();
            rsEmp.next();
            int employeeId = rsEmp.getInt(1);

            if (selectedStaffType == null) {
                showAlert("Error", "Please select staff type.");
                return;
            }

            if (selectedStaffType.equals("Caretaker")) {
                // Step 2: Insert into User
                PreparedStatement userStmt = conn.prepareStatement(
                        "INSERT INTO User (FirstName, MiddleName, LastName, CNIC, Gender, Phone, Email, Address, Password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                userStmt.setString(1, firstName);
                userStmt.setString(2, middleName);
                userStmt.setString(3, lastName);
                userStmt.setString(4, cnicValue);
                userStmt.setString(5, selectedGender);
                userStmt.setString(6, phoneValue);
                userStmt.setString(7, emailValue);
                userStmt.setString(8, addressVal);
                userStmt.setString(9, "caretakerpass");
                userStmt.executeUpdate();
                ResultSet rsUser = userStmt.getGeneratedKeys();
                rsUser.next();
                int userId = rsUser.getInt(1);

                // Step 3: Insert into Caretaker
                PreparedStatement caretakerStmt = conn.prepareStatement(
                        "INSERT INTO Caretaker (UserID, EmployeeID, HostelID) VALUES (?, ?, ?)"
                );
                caretakerStmt.setInt(1, userId);
                caretakerStmt.setInt(2, employeeId);
                caretakerStmt.setInt(3, hostelId);
                caretakerStmt.executeUpdate();

            } else if (selectedStaffType.equals("Mess Head")) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO MessHead (EmployeeID, HostelID) VALUES (?, ?)"
                );
                stmt.setInt(1, employeeId);
                stmt.setInt(2, hostelId);
                stmt.executeUpdate();

            } else if (selectedStaffType.equals("Other")) {
                PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO OtherStaff (EmployeeID, HostelID) VALUES (?, ?)"
                );
                stmt.setInt(1, employeeId);
                stmt.setInt(2, hostelId);
                stmt.executeUpdate();
            }

            showAlert("Success", "Staff added successfully.");
            clearButton(event);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Exception", e.getMessage());
        }
    }

    private boolean exists(Connection conn, String query, String param) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, param);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    void clearButton(ActionEvent event) {
        fullName.clear();
        cnic.clear();
        phone.clear();
        email.clear();
        address.clear();
        hostelid.clear();

        employmentDate.setValue(null);
        joiningDate.setValue(null);

        selectedGender = null;
        selectedStaffType = null;

        gender.setText("Select Gender");
        StaffType.setText("Select Staff Type");
    }

}
