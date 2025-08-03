import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.sql.*;

public class AddNewStudentController {

    // UI Elements
    @FXML private TextField ADDRESS, CNIC, CellNO, ContactNoFather, DeptName, Email, EmailFather, EmergencyContact, FatherName, RollNO, StudentName, UniName;
    @FXML private DatePicker DOB, AddmissionDate;
    @FXML private MenuButton GENDER;

    private final int hostelId = 1000000;

    // db config
    private final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagementsystem";
    private final String DB_USER = "root";
    private final String DB_PASS = "saadkhan2005";

    @FXML
    void handleGenderSelect(ActionEvent event) {
        MenuItem selected = (MenuItem) event.getSource();
        GENDER.setText(selected.getText());
    }

    @FXML
    void btnAdd(ActionEvent event) {
        if (!validateInputs()) return;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            // Check if Roll Number already exists
            String checkRollNoQuery = "SELECT COUNT(*) FROM Student WHERE RollNo = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkRollNoQuery);
            checkStmt.setString(1, RollNO.getText());
            ResultSet checkResult = checkStmt.executeQuery();

            if (checkResult.next() && checkResult.getInt(1) > 0) {
                showAlert("A student with Roll Number \"" + RollNO.getText() + "\" already exists.");
                return;
            }

            // Check if CNIC already exists
            String checkCnicQuery = "SELECT COUNT(*) FROM User WHERE CNIC = ?";
            PreparedStatement cnicStmt = conn.prepareStatement(checkCnicQuery);
            cnicStmt.setString(1, CNIC.getText());
            ResultSet cnicResult = cnicStmt.executeQuery();

            if (cnicResult.next() && cnicResult.getInt(1) > 0) {
                showAlert("A user with CNIC \"" + CNIC.getText() + "\" already exists.");
                return;
            }

            // Check if Email already exists
            String checkEmailQuery = "SELECT COUNT(*) FROM User WHERE Email = ?";
            PreparedStatement emailStmt = conn.prepareStatement(checkEmailQuery);
            emailStmt.setString(1, Email.getText());
            ResultSet emailResult = emailStmt.executeQuery();

            if (emailResult.next() && emailResult.getInt(1) > 0) {
                showAlert("A user with Email \"" + Email.getText() + "\" already exists.");
                return;
            }


            conn.setAutoCommit(false);

            // Insert into User table
            String[] fullNameParts = StudentName.getText().trim().split(" ");
            String firstName = fullNameParts[0];
            String lastName = fullNameParts[fullNameParts.length - 1];
            String middleName = fullNameParts.length == 3 ? fullNameParts[1] : null;

            String insertUser = "INSERT INTO User (FirstName, MiddleName, LastName, CNIC, Gender, Phone, Email, Address, Password, ProfilePic) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NULL, NULL)";
            PreparedStatement psUser = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS);
            psUser.setString(1, firstName);
            psUser.setString(2, middleName);
            psUser.setString(3, lastName);
            psUser.setString(4, CNIC.getText());
            psUser.setString(5, GENDER.getText());
            psUser.setString(6, CellNO.getText());
            psUser.setString(7, Email.getText());
            psUser.setString(8, ADDRESS.getText());

            int rowsAffected = psUser.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            ResultSet rs = psUser.getGeneratedKeys();
            if (!rs.next()) {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
            int userId = rs.getInt(1);

            // Insert into Student table
            String insertStudent = "INSERT INTO Student (UserID, HostelID, DOB, InstiName, RollNo, InstiAddmissionDate, Department, " +
                    "AllotmentDate, GuardianName, GuardianPhone, GaurdianEmail, EmergencyNo) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE(), ?, ?, ?, ?)";

            PreparedStatement psStudent = conn.prepareStatement(insertStudent, Statement.RETURN_GENERATED_KEYS);
            psStudent.setInt(1, userId);
            psStudent.setInt(2, hostelId);
            psStudent.setDate(3, Date.valueOf(DOB.getValue()));
            psStudent.setString(4, UniName.getText());
            psStudent.setString(5, RollNO.getText());
            psStudent.setDate(6, Date.valueOf(AddmissionDate.getValue()));
            psStudent.setString(7, DeptName.getText());
            psStudent.setString(8, FatherName.getText());
            psStudent.setString(9, ContactNoFather.getText());
            psStudent.setString(10, EmailFather.getText());
            psStudent.setString(11, EmergencyContact.getText());

            int rowsInserted = psStudent.executeUpdate();
            int studentId = -1;

            if (rowsInserted > 0) {
                ResultSet generatedKeys = psStudent.getGeneratedKeys();
                if (generatedKeys.next()) {
                    studentId = generatedKeys.getInt(1);
                }
            }

            conn.commit();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Student added successfully with ID: " + studentId);
            alert.showAndWait();
            btnClearAll(event);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        if (StudentName.getText().isEmpty() || FatherName.getText().isEmpty() ||
            DOB.getValue() == null || GENDER.getText().equals("Select Gender") ||
            CNIC.getText().isEmpty() || RollNO.getText().isEmpty() ||
            DeptName.getText().isEmpty() || UniName.getText().isEmpty() ||
            AddmissionDate.getValue() == null || Email.getText().isEmpty() ||
            CellNO.getText().isEmpty() || ADDRESS.getText().isEmpty() ||
            EmailFather.getText().isEmpty() || ContactNoFather.getText().isEmpty() ||
            EmergencyContact.getText().isEmpty()) {

            showAlert("Please fill in all the fields.");
            return false;
        }

        if (!StudentName.getText().matches("^[A-Za-z]+(\\s[A-Za-z]+){1,2}$")) {
            showAlert("Student name must only contain letters and be in format: First Middle Last or First Last.");
            return false;
        }

        if (!FatherName.getText().matches("^[A-Za-z]+(\\s[A-Za-z]+){1,2}$")) {
            showAlert("Father name must only contain letters and be in format: First Middle Last or First Last.");
            return false;
        }

        if (!CNIC.getText().matches("^\\d{13}$")) {
            showAlert("CNIC must contain exactly 13 digits.");
            return false;
        }

        if (!Email.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert("Invalid student email format.");
            return false;
        }

        if (!EmailFather.getText().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showAlert("Invalid father/guardian email format.");
            return false;
        }

        if (!CellNO.getText().matches("^03\\d{9}$")) {
            showAlert("Cell number must be in the format 03XXXXXXXXX.");
            return false;
        }

        if (!ContactNoFather.getText().matches("^03\\d{9}$")) {
            showAlert("Father/Guardian contact number must be in the format 03XXXXXXXXX.");
            return false;
        }

        if (!EmergencyContact.getText().matches("^03\\d{9}$")) {
            showAlert("Emergency contact number must be in the format 03XXXXXXXXX.");
            return false;
        }

        return true;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void btnClearAll(ActionEvent event) {
        StudentName.clear();
        FatherName.clear();
        DOB.setValue(null);
        GENDER.setText("Select Gender");
        CNIC.clear();
        RollNO.clear();
        DeptName.clear();
        UniName.clear();
        AddmissionDate.setValue(null);
        Email.clear();
        CellNO.clear();
        ADDRESS.clear();
        EmailFather.clear();
        ContactNoFather.clear();
        EmergencyContact.clear();
    }

    @FXML void btnAddress(ActionEvent event) {}
    @FXML void btnAdmitDate(ActionEvent event) {}
    @FXML void btnCNIC(ActionEvent event) {}
    @FXML void btnCell(ActionEvent event) {}
    @FXML void btnDOB(ActionEvent event) {}
    @FXML void btnDepartment(ActionEvent event) {}
    @FXML void btnEmailStudent(ActionEvent event) {}
    @FXML void btnEmergencyContact(ActionEvent event) {}
    @FXML void btnFatherContact(ActionEvent event) {}
    @FXML void btnFatherEmail(ActionEvent event) {}
    @FXML void btnFatherName(ActionEvent event) {}
    @FXML void btnGender(ActionEvent event) {}
    @FXML void btnName(ActionEvent event) {}
    @FXML void btnRollNo(ActionEvent event) {}
    @FXML void btnUniNAME(ActionEvent event) {}
}
