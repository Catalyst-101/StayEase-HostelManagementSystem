// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.control.Alert;
// import javafx.scene.control.TextField;
// import javafx.scene.text.Text;

// public class ViewSTUController {

//     @FXML
//     private TextField TF_sid_cnic;

//     @FXML private Text addDate;
//     @FXML private Text address;
//     @FXML private Text cellno;
//     @FXML private Text cnic;
//     @FXML private Text dept;
//     @FXML private Text dob;
//     @FXML private Text emailstudent;
//     @FXML private Text emergencyno;
//     @FXML private Text father;
//     @FXML private Text fatheremail;
//     @FXML private Text fatherno;
//     @FXML private Text gender;
//     @FXML private Text name;
//     @FXML private Text roll;
//     @FXML private Text uni;

//     // Hardcoded student data
//     private final String STUDENT_ID = "1000000";
//     private final String STUDENT_NAME = "Ali Khan";
//     private final String STUDENT_CNIC = "1234567890123";
//     private final String STUDENT_CELL = "0300-1234567";
//     private final String STUDENT_EMERGENCY = "0312-3456789";
//     private final String STUDENT_EMAIL = "ali.khan@student.edu.pk";
//     private final String STUDENT_FATHER = "Mr. Khan";
//     private final String STUDENT_FATHER_EMAIL = "mr.khan@domain.com";
//     private final String STUDENT_FATHER_NO = "0301-9988776";
//     private final String STUDENT_DOB = "2000-05-15";
//     private final String STUDENT_GENDER = "Male";
//     private final String STUDENT_DEPT = "Computer Science";
//     private final String STUDENT_ROLL = "CS2022-10";
//     private final String STUDENT_UNI = "ABC University";
//     private final String STUDENT_ADDRESS = "123 Street, City";
//     private final String STUDENT_ADDDATE = "2022-09-01";

//     // Clears all fields
//     @FXML
//     void btnClear(ActionEvent event) {
//         TF_sid_cnic.clear();
//         name.setText("");
//         cnic.setText("");
//         cellno.setText("");
//         emergencyno.setText("");
//         emailstudent.setText("");
//         father.setText("");
//         fatheremail.setText("");
//         fatherno.setText("");
//         dob.setText("");
//         gender.setText("");
//         dept.setText("");
//         roll.setText("");
//         uni.setText("");
//         address.setText("");
//         addDate.setText("");
//         showAlert(Alert.AlertType.INFORMATION, "Cleared", "All fields cleared.");
//     }

//     // Searches and displays student data
//     @FXML
//     void btnSearch(ActionEvent event) {
//         String input = TF_sid_cnic.getText().trim();

//         if (input.isEmpty()) {
//             showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter Student ID or CNIC.");
//             return;
//         }

//         if (!input.matches("\\d{7}") && !input.matches("\\d{13}")) {
//             showAlert(Alert.AlertType.ERROR, "Invalid Input", "Input must be either:\n- 7 digits (Student ID)\n- 13 digits (CNIC)");
//             return;
//         }

//         if (input.equals(STUDENT_ID) || input.equals(STUDENT_CNIC)) {
//             name.setText("Name: " + STUDENT_NAME);
//             cnic.setText("CNIC: " + STUDENT_CNIC);
//             cellno.setText("Cell No: " + STUDENT_CELL);
//             emergencyno.setText("Emergency No: " + STUDENT_EMERGENCY);
//             emailstudent.setText("Email: " + STUDENT_EMAIL);
//             father.setText("Father: " + STUDENT_FATHER);
//             fatheremail.setText("Father Email: " + STUDENT_FATHER_EMAIL);
//             fatherno.setText("Father No: " + STUDENT_FATHER_NO);
//             dob.setText("DOB: " + STUDENT_DOB);
//             gender.setText("Gender: " + STUDENT_GENDER);
//             dept.setText("Department: " + STUDENT_DEPT);
//             roll.setText("Roll No: " + STUDENT_ROLL);
//             uni.setText("University: " + STUDENT_UNI);
//             address.setText("Address: " + STUDENT_ADDRESS);
//             addDate.setText("Add Date: " + STUDENT_ADDDATE);

//             showAlert(Alert.AlertType.INFORMATION, "Student Found", "✅ Details loaded for student: " + STUDENT_NAME);
//         } else {
//             showAlert(Alert.AlertType.ERROR, "Not Found", "❌ No student found with ID or CNIC: " + input);
//         }
//     }

//     // Alert helper
//     private void showAlert(Alert.AlertType type, String title, String content) {
//         Alert alert = new Alert(type);
//         alert.setTitle(title);
//         alert.setHeaderText(null);
//         alert.setContentText(content);
//         alert.showAndWait();
//     }
// }

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.*;

public class ViewSTUController {

    @FXML private TextField TF_sid_cnic;

    @FXML private Text addDate;
    @FXML private Text address;
    @FXML private Text cellno;
    @FXML private Text cnic;
    @FXML private Text dept;
    @FXML private Text dob;
    @FXML private Text emailstudent;
    @FXML private Text emergencyno;
    @FXML private Text father;
    @FXML private Text fatheremail;
    @FXML private Text fatherno;
    @FXML private Text gender;
    @FXML private Text name;
    @FXML private Text roll;
    @FXML private Text uni;

    // Clears all fields
    @FXML
    void btnClear(ActionEvent event) {
        TF_sid_cnic.clear();
        name.setText("");
        cnic.setText("");
        cellno.setText("");
        emergencyno.setText("");
        emailstudent.setText("");
        father.setText("");
        fatheremail.setText("");
        fatherno.setText("");
        dob.setText("");
        gender.setText("");
        dept.setText("");
        roll.setText("");
        uni.setText("");
        address.setText("");
        addDate.setText("");
        showAlert(Alert.AlertType.INFORMATION, "Cleared", "All fields cleared.");
    }

    // Searches and displays student data
    @FXML
    void btnSearch(ActionEvent event) {
        String input = TF_sid_cnic.getText().trim();

        if (input.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter Student ID or CNIC.");
            return;
        }

        if (!input.matches("\\d{7}") && !input.matches("\\d{13}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Input must be either:\n- 7 digits (Student ID)\n- 13 digits (CNIC)");
            return;
        }

        // DB logic
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "saadkhan2005")) {

            PreparedStatement stmt;
            if (input.length() == 7) {
                // Search by Student ID
                stmt = conn.prepareStatement(
                    "SELECT s.*, u.* FROM Student s JOIN User u ON s.UserID = u.UserID WHERE s.StudentID = ?");
                stmt.setInt(1, Integer.parseInt(input));
            } else {
                // Search by CNIC
                stmt = conn.prepareStatement(
                    "SELECT s.*, u.* FROM Student s JOIN User u ON s.UserID = u.UserID WHERE u.CNIC = ?");
                stmt.setString(1, input);
            }

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                name.setText("Name: " + getFullName(rs.getString("FirstName"), rs.getString("MiddleName"), rs.getString("LastName")));
                cnic.setText("CNIC: " + getOrNA(rs.getString("CNIC")));
                cellno.setText("Cell No: " + getOrNA(rs.getString("Phone")));
                emergencyno.setText("Emergency No: " + getOrNA(rs.getString("EmergencyNo")));
                emailstudent.setText("Email: " + getOrNA(rs.getString("Email")));
                father.setText("Father: " + getOrNA(rs.getString("GuardianName")));
                fatheremail.setText("Father Email: " + getOrNA(rs.getString("GaurdianEmail")));
                fatherno.setText("Father No: " + getOrNA(rs.getString("GuardianPhone")));
                dob.setText("DOB: " + getOrNA(rs.getString("DOB")));
                gender.setText("Gender: " + getOrNA(rs.getString("Gender")));
                dept.setText("Department: " + getOrNA(rs.getString("Department")));
                roll.setText("Roll No: " + getOrNA(rs.getString("RollNo")));
                uni.setText("University: " + getOrNA(rs.getString("InstiName")));
                address.setText("Address: " + getOrNA(rs.getString("Address")));
                addDate.setText("Add Date: " + getOrNA(rs.getString("InstiAddmissionDate")));

                showAlert(Alert.AlertType.INFORMATION, "Student Found", "✅ Student data loaded successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Not Found", "❌ No student found with ID or CNIC: " + input);
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error fetching student: " + e.getMessage());
        }
    }

    // Helpers
    private String getFullName(String first, String middle, String last) {
        StringBuilder sb = new StringBuilder();
        if (first != null) sb.append(first).append(" ");
        if (middle != null && !middle.isEmpty()) sb.append(middle).append(" ");
        if (last != null) sb.append(last);
        return sb.toString().trim();
    }

    private String getOrNA(String value) {
        return (value == null || value.trim().isEmpty()) ? "N/A" : value;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

