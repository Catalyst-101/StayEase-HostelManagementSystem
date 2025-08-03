// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.control.Alert;
// import javafx.scene.control.Button;
// import javafx.scene.control.DatePicker;
// import javafx.scene.control.TextField;

// import java.time.LocalDate;
// import java.time.temporal.ChronoUnit;

// public class SINController {

//     @FXML
//     private TextField TF_destination;

//     @FXML
//     private TextField TF_studentid;

//     @FXML
//     private Button clearallbtn;

//     @FXML
//     private DatePicker leavedate;

//     @FXML
//     private DatePicker returndate;

//     @FXML
//     private Button savebtn;

//     // Storage strings (data saved on Save button click)
//     private String studentId, destination, leaveDate, returnDate;

//     // Clears all fields
//     @FXML
//     void btnClear(ActionEvent event) {
//         TF_studentid.clear();
//         TF_destination.clear();
//         leavedate.setValue(null);
//         returndate.setValue(null);
//         showAlert(Alert.AlertType.INFORMATION, "Cleared", "All fields have been cleared.");
//     }

//     // Handles save logic
//     @FXML
//     void btnSave(ActionEvent event) {
//         String sId = TF_studentid.getText().trim();
//         String dest = TF_destination.getText().trim();
//         LocalDate leave = leavedate.getValue();
//         LocalDate ret = returndate.getValue();

//         // Check for empty fields
//         if (sId.isEmpty() || dest.isEmpty() || leave == null || ret == null) {
//             showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
//             return;
//         }

//         // Validate Student ID: must be either 7 or 13 digits
//         if (!sId.matches("^\\d{7}$") && !sId.matches("^\\d{13}$")) {
//             showAlert(Alert.AlertType.ERROR, "Invalid ID", "Student ID must be either:\n- 7 digits (Student ID)\n- 13 digits (CNIC)");
//             return;
//         }

//         // Validate leave and return dates
//         if (ret.isBefore(leave)) {
//             showAlert(Alert.AlertType.ERROR, "Invalid Dates", "Return date cannot be before leave date.");
//             return;
//         }

//         long daysBetween = ChronoUnit.DAYS.between(leave, ret);

//         // Save to class-level variables
//         studentId = sId;
//         destination = dest;
//         leaveDate = leave.toString();
//         returnDate = ret.toString();

//         showAlert(Alert.AlertType.INFORMATION, "Saved", "Leave request saved:\n" +
//                 "ID: " + studentId + "\n" +
//                 "Destination: " + destination + "\n" +
//                 "Leave Date: " + leaveDate + "\n" +
//                 "Return Date: " + returnDate + "\n" +
//                 "Days on Leave: " + daysBetween);

//         btnClear(null); // Clear after save
//     }

//     // Reusable alert method
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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SINController {

    @FXML
    private TextField TF_destination;

    @FXML
    private TextField TF_studentid;

    @FXML
    private Button clearallbtn;

    @FXML
    private DatePicker leavedate;

    @FXML
    private DatePicker returndate;

    @FXML
    private Button savebtn;

    // Storage strings (data saved on Save button click)
    private String studentId, destination, leaveDate, returnDate;

    // Clears all fields
    @FXML
    void btnClear(ActionEvent event) {
        TF_studentid.clear();
        TF_destination.clear();
        leavedate.setValue(null);
        returndate.setValue(null);
        showAlert(Alert.AlertType.INFORMATION, "Cleared", "All fields have been cleared.");
    }

    // Handles save logic
    @FXML
    void btnSave(ActionEvent event) {
        String sId = TF_studentid.getText().trim();
        String dest = TF_destination.getText().trim();
        LocalDate leave = leavedate.getValue();
        LocalDate ret = returndate.getValue();

        // Check for empty fields
        if (sId.isEmpty() || dest.isEmpty() || leave == null || ret == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        // Validate Student ID: must be either 7 or 13 digits
        if (!sId.matches("^\\d{7}$") && !sId.matches("^\\d{13}$")) {
            showAlert(Alert.AlertType.ERROR, "Invalid ID", "Student ID must be either:\n- 7 digits (Student ID)\n- 13 digits (CNIC)");
            return;
        }

        // Validate leave and return dates
        if (ret.isBefore(leave)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Dates", "Return date cannot be before leave date.");
            return;
        }

        long daysBetween = ChronoUnit.DAYS.between(leave, ret);


        // Save to class-level variables
        if(sId.length() == 7)
            studentId = sId;

        destination = dest;
        leaveDate = leave.toString();
        returnDate = ret.toString();

        // Database Insertion
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "saadkhan2005")) {

            if (sId.length() == 13) {
                // Lookup StudentID using CNIC via JOIN between Student and User
                String lookupSQL = "SELECT Student.StudentID " +
                                "FROM Student " +
                                "JOIN User ON Student.UserID = User.UserID " +
                                "WHERE User.CNIC = ?";
                PreparedStatement lookupStmt = conn.prepareStatement(lookupSQL);
                lookupStmt.setString(1, sId);
                ResultSet rs = lookupStmt.executeQuery();
                if (rs.next()) {
                    studentId = String.valueOf(rs.getInt("StudentID"));
                } else {
                    showAlert(Alert.AlertType.ERROR, "Not Found", "No student found with CNIC: " + sId);
                    return;
                }
            }

            PreparedStatement checkStmt = conn.prepareStatement(
                "SELECT * FROM StudentLeave WHERE StudentID = ? AND " +
                "((LeaveDate <= ? AND ReturnDate >= ?) OR " +  // Overlaps at the start
                "(LeaveDate <= ? AND ReturnDate >= ?) OR " +  // Overlaps at the end
                "(LeaveDate >= ? AND ReturnDate <= ?))"       // Completely inside the range
            );

            checkStmt.setInt(1, Integer.parseInt(studentId));
            checkStmt.setDate(2, java.sql.Date.valueOf(returnDate)); // New return date
            checkStmt.setDate(3, java.sql.Date.valueOf(returnDate));
            checkStmt.setDate(4, java.sql.Date.valueOf(leaveDate));  // New leave date
            checkStmt.setDate(5, java.sql.Date.valueOf(leaveDate));
            checkStmt.setDate(6, java.sql.Date.valueOf(leaveDate));
            checkStmt.setDate(7, java.sql.Date.valueOf(returnDate));

            ResultSet rsCheck = checkStmt.executeQuery();
            if (rsCheck.next()) {
                showAlert(Alert.AlertType.ERROR, "Already On Leave",
                    "❌ This student already has a leave record overlapping with the selected dates.");
                return;
            }


            // Insert statement (ApplyDate = LeaveDate)
            String sql = "INSERT INTO StudentLeave (StudentID, ApplyDate, LeaveDate, ReturnDate, Destination, Reason) VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(studentId));
            stmt.setDate(2, java.sql.Date.valueOf(leave));
            stmt.setDate(3, java.sql.Date.valueOf(leave));
            stmt.setDate(4, java.sql.Date.valueOf(ret));
            stmt.setString(5, destination);
            stmt.setString(6, null);  // Reason is NULL for now

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Saved", "Leave request saved:\n" +
                        "ID: " + studentId + "\n" +
                        "Destination: " + destination + "\n" +
                        "Leave Date: " + leaveDate + "\n" +
                        "Return Date: " + returnDate + "\n" +
                        "Days on Leave: " + daysBetween);
                btnClear(null); // Clear after save
            } else {
                showAlert(Alert.AlertType.ERROR, "Insert Failed", "Could not save leave request.");
            }

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error saving leave request:\n" + e.getMessage());
        }
    }

    // Reusable alert method
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
