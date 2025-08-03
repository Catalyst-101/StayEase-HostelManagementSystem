import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class AddVisitor {

    @FXML private TextField fullName;
    @FXML private TextField cnic;
    @FXML private TextField phone;
    @FXML private TextField email;
    @FXML private TextField studentId;
    @FXML private ComboBox<String> genderCombo;
    @FXML private Button clearButton;
    @FXML private Button addButton;

    @FXML
    private void initialize() {
        genderCombo.getItems().addAll("Male", "Female", "Other");

        addButton.setOnAction(event -> addVisitor());
        clearButton.setOnAction(event -> clearForm());
    }

    @FXML
    private void clearForm() {
        fullName.clear();
        cnic.clear();
        phone.clear();
        email.clear();
        studentId.clear();
        genderCombo.setValue(null);
    }

    @FXML
    private void addVisitor() {
        if (!validateInputs()) return;

        String[] nameParts = fullName.getText().trim().split(" ");
        if (nameParts.length < 2 || nameParts.length > 3) {
            showAlert(Alert.AlertType.ERROR, "Name Format Error",
                    "Please enter either 'First Last' or 'First Middle Last'.");
            return;
        }

        String firstName = nameParts[0];
        String middleName = nameParts.length == 3 ? nameParts[1] : "";
        String lastName = nameParts.length == 3 ? nameParts[2] : nameParts[1];

        String cnicText = cnic.getText();
        String phoneText = phone.getText();
        String emailText = email.getText();
        String studentIdText = studentId.getText();
        String gender = genderCombo.getValue();

        try (Connection conn = DBHelper.getConnection()) {
            // Check if Visitor CNIC already exists
            PreparedStatement checkVisitor = conn.prepareStatement(
                "SELECT 1 FROM Visitor WHERE CNIC = ?"
            );
            checkVisitor.setString(1, cnicText);
            ResultSet rs1 = checkVisitor.executeQuery();
            if (rs1.next()) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Visitor", "A visitor with this CNIC already exists.");
                return;
            }

            // Check if Student exists
            PreparedStatement checkStudent = conn.prepareStatement(
                "SELECT 1 FROM Student WHERE StudentID = ?"
            );
            checkStudent.setInt(1, Integer.parseInt(studentIdText));
            ResultSet rs2 = checkStudent.executeQuery();
            if (!rs2.next()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Student ID", "No student found with the given ID.");
                return;
            }

            String emailCheckQuery = "SELECT * FROM Visitor WHERE Email = ?";
            try (PreparedStatement emailStmt = conn.prepareStatement(emailCheckQuery)) {
                emailStmt.setString(1, emailText);
                ResultSet rs = emailStmt.executeQuery();
                if (rs.next()) {
                    showAlert(Alert.AlertType.ERROR, "Duplicate Email", "This email is already associated with another visitor.");
                    return;
                }
            }

            // Insert Visitor
            CallableStatement cs = conn.prepareCall("{ call AddVisitor(?, ?, ?, ?, ?, ?, ?, ?) }");
            cs.setString(1, firstName);
            cs.setString(2, middleName);
            cs.setString(3, lastName);
            cs.setString(4, cnicText);
            cs.setString(5, gender);
            cs.setString(6, emailText);
            cs.setString(7, phoneText);
            cs.setInt(8, Integer.parseInt(studentIdText));


            int rowsInserted = cs.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Visitor added successfully.");
                clearForm();
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Student ID must be a number.");
        }
    }

    private boolean validateInputs() {
        if (fullName.getText().trim().isEmpty() ||
            cnic.getText().isEmpty() ||
            phone.getText().isEmpty() ||
            email.getText().isEmpty() ||
            studentId.getText().isEmpty() ||
            genderCombo.getValue() == null) {

            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled.");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
