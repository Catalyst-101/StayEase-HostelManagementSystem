// // import javafx.fxml.FXML;
// // import javafx.scene.control.Alert;
// // import javafx.scene.text.Text;
// // import javafx.scene.control.TextField;
// // import java.time.LocalDate;

// // public class Mess2Controller {
// //     @FXML
// //     private TextField tf_student_id_CNIC;
// //     @FXML
// //     private Text t_studentId;
// //     @FXML
// //     private Text t_studentName;
// //     @FXML
// //     private Text t_roomNo;
// //     @FXML
// //     private Text t_messOff;
// //     @FXML
// //     private Text t_messOn;
// //     @FXML
// //     private Text t_reason;
// //     @FXML
// //     private Text t_status;

// //     private final long CNIC = 4230182974383L;
// //     private final int STUDENT_ID = 10;
// //     private final String STUDENT_NAME = "Hanan";
// //     private final LocalDate MESS_OFF = LocalDate.of(2025, 1, 1);
// //     private final LocalDate MESS_ON = LocalDate.of(2025, 1, 5);
// //     private final String REASON = "HOME VISIT";
// //     private final boolean STATUS = true;
// //     private final int ROOM_NO = 334;

// //     @FXML
// //     void btnClearAll() {
// //         tf_student_id_CNIC.clear();
// //         t_studentId.setText("");
// //         t_studentName.setText("");
// //         t_roomNo.setText("");
// //         t_messOff.setText("");
// //         t_messOn.setText("");
// //         t_reason.setText("");
// //         t_status.setText("");
// //     }

// //     @FXML
// //     private void btnSearch() {
// //         String searchText = tf_student_id_CNIC.getText().trim();

// //         if (searchText.isEmpty()) {
// //             showAlert("Please enter a CNIC or Student ID to search.");
// //             return;
// //         }

// //         if (!searchText.matches("\\d+")) {
// //             showAlert("Invalid input! Only numeric values allowed.");
// //             return;
// //         }

// //         if (searchText.length() == 13) {
// //             // CNIC
// //             long inputCNIC = Long.parseLong(searchText);
// //             if (inputCNIC == CNIC) {
// //                 displayStudentInfo();
// //             } else {
// //                 showAlert("No matching CNIC found.");
// //             }
// //         } else if (searchText.length() == 7) {
// //             // Student ID
// //             int inputID = Integer.parseInt(searchText);
// //             if (inputID == STUDENT_ID) {
// //                 displayStudentInfo();
// //             } else {
// //                 showAlert("No matching Student ID found.");
// //             }
// //         } else {
// //             showAlert("Input must be either 13-digit CNIC or 7-digit Student ID.");
// //         }
// //     }

// //     private void displayStudentInfo() {
// //         t_studentId.setText("Student ID: " + STUDENT_ID);
// //         t_studentName.setText("Student Name: " + STUDENT_NAME);
// //         t_roomNo.setText("Room No.: " + ROOM_NO);
// //         t_messOff.setText("Mess Off Date: " + MESS_OFF);
// //         t_messOn.setText("Mess On Date: " + MESS_ON);
// //         t_reason.setText("Reason: " + REASON);
// //         t_status.setText("Status: " + (STATUS ? "Active" : "Inactive"));
// //     }

// //     private void showAlert(String message) {
// //         Alert alert = new Alert(Alert.AlertType.INFORMATION);
// //         alert.setTitle("Search Result");
// //         alert.setHeaderText(null);
// //         alert.setContentText(message);
// //         alert.showAndWait();
// //     }
// // }

// import javafx.fxml.FXML;
// import javafx.scene.control.Alert;
// import javafx.scene.control.TextField;
// import javafx.scene.text.Text;

// import java.sql.*;
// import java.time.LocalDate;

// public class Mess2Controller {
//     @FXML
//     private TextField tf_student_id_CNIC;
//     @FXML
//     private Text t_studentId;
//     @FXML
//     private Text t_studentName;
//     @FXML
//     private Text t_roomNo;
//     @FXML
//     private Text t_messOff;
//     @FXML
//     private Text t_messOn;
//     @FXML
//     private Text t_reason;
//     @FXML
//     private Text t_status;

//     private Connection connect() throws SQLException {
//         return DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "saadkhan2005");
//     }

//     @FXML
//     void btnClearAll() {
//         tf_student_id_CNIC.clear();
//         t_studentId.setText("");
//         t_studentName.setText("");
//         t_roomNo.setText("");
//         t_messOff.setText("");
//         t_messOn.setText("");
//         t_reason.setText("");
//         t_status.setText("");
//     }

//     @FXML
//     void btnSearch() {
//         String input = tf_student_id_CNIC.getText().trim();

//         if (input.isEmpty()) {
//             showAlert("Please enter a CNIC or Student ID to search.");
//             return;
//         }

//         if (!input.matches("\\d+")) {
//             showAlert("Only numeric values are allowed.");
//             return;
//         }

//         try (Connection conn = connect()) {
//             PreparedStatement ps;
//             ResultSet rs;

//             int studentId;
//             String studentName;
//             String roomNo;

//             if (input.length() == 13) {
//                 // CNIC case
//                 ps = conn.prepareStatement(
//                         "SELECT s.StudentID, CONCAT(u.FirstName, ' ', u.MiddleName, ' ', u.LastName) AS FullName,e, r.RoomNumber FROM Student s " +
//                         "JOIN User u ON s.UserID = u.UserID " +
//                         "JOIN Room r ON s.RoomID = r.RoomID " +
//                         "WHERE u.CNIC = ?");
//                 ps.setString(1, input);
//             } else if (input.length() == 7) {
//                 // Student ID case
//                 ps = conn.prepareStatement(
//                         "SELECT s.StudentID, u.FullName, r.RoomNumber FROM Student s " +
//                         "JOIN User u ON s.UserID = u.UserID " +
//                         "JOIN Room r ON s.RoomID = r.RoomID " +
//                         "WHERE s.StudentID = ?");
//                 ps.setInt(1, Integer.parseInt(input));
//             } else {
//                 showAlert("Input must be either 13-digit CNIC or 7-digit Student ID.");
//                 return;
//             }

//             rs = ps.executeQuery();

//             if (!rs.next()) {
//                 showAlert("Student not found.");
//                 return;
//             }

//             studentId = rs.getInt("StudentID");
//             studentName = rs.getString("FullName");
//             roomNo = rs.getString("RoomNumber");

//             // Now get latest MessStatus
//             ps = conn.prepareStatement(
//                     "SELECT LeaveDate, JoinDate, Reason FROM MessStatus WHERE StudentID = ? ORDER BY LeaveDate DESC LIMIT 1");
//             ps.setInt(1, studentId);
//             rs = ps.executeQuery();

//             if (!rs.next()) {
//                 showAlert("No Mess Off record found for this student.");
//                 return;
//             }

//             LocalDate leaveDate = rs.getDate("LeaveDate").toLocalDate();
//             LocalDate joinDate = rs.getDate("JoinDate").toLocalDate();
//             String reason = rs.getString("Reason");
//             boolean isActive = LocalDate.now().isBefore(joinDate);

//             // Display on screen
//             t_studentId.setText("Student ID: " + studentId);
//             t_studentName.setText("Student Name: " + studentName);
//             t_roomNo.setText("Room No.: " + roomNo);
//             t_messOff.setText("Mess Off Date: " + leaveDate);
//             t_messOn.setText("Mess On Date: " + joinDate);
//             t_reason.setText("Reason: " + reason);
//             t_status.setText("Status: " + (isActive ? "Active" : "Inactive"));

//         } catch (SQLException e) {
//             e.printStackTrace();
//             showAlert("Database error: " + e.getMessage());
//         }
//     }

//     private void showAlert(String message) {
//         Alert alert = new Alert(Alert.AlertType.INFORMATION);
//         alert.setTitle("Search Result");
//         alert.setHeaderText(null);
//         alert.setContentText(message);
//         alert.showAndWait();
//     }
// }


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class Mess2Controller {

    private int STUDENT_ID;
    private String STUDENT_NAME;
    private String ROOM_NO;
    private String MESS_OFF;
    private String MESS_ON;
    private String REASON;
    private boolean STATUS;

    @FXML
    private TextField tf_student_id_CNIC;
    @FXML
    private Text t_studentId;
    @FXML
    private Text t_studentName;
    @FXML
    private Text t_roomNo;
    @FXML
    private Text t_messOff;
    @FXML
    private Text t_messOn;
    @FXML
    private Text t_reason;
    @FXML
    private Text t_status;

    @FXML
    void btnClearAll() {
        tf_student_id_CNIC.clear();
        clearStudentInfo();
    }

    private void clearStudentInfo() {
        t_studentId.setText("");
        t_studentName.setText("");
        t_roomNo.setText("");
        t_messOff.setText("");
        t_messOn.setText("");
        t_reason.setText("");
        t_status.setText("");
    }

    @FXML
    private void btnSearch() {
        clearStudentInfo();

        if (tf_student_id_CNIC.getText().isEmpty()) {
            showAlert("Error", "Please enter a CNIC or Student ID to search.");
            return;
        }

        String searchId;
            searchId = tf_student_id_CNIC.getText();
            if (Long.parseLong(searchId) <= 0) {
                showAlert("Error", "ID or CNIC must be a positive number!");
                return;
            }
            
        String sql = "SELECT ms.StudentID, " +
            "CONCAT(u.FirstName, ' ', COALESCE(u.MiddleName, ''), ' ', u.LastName) AS Name, " +
            "r.RoomNumber, " +
            "ms.LeaveDate AS OffDate, " +
            "ms.JoinDate AS OnDate, " +
            "COALESCE(ms.Reason, 'N/A') AS Reason, " +
            "CURRENT_DATE BETWEEN ms.LeaveDate AND ms.JoinDate AS Status " +
            "FROM MessStatus ms " +
            "JOIN Student s ON ms.StudentID = s.StudentID " +
            "LEFT JOIN Room r ON s.RoomID = r.RoomID " +
            "JOIN User u ON s.UserID = u.UserID " +
            "WHERE ms.StudentID = ? OR u.CNIC = ? " +
            "ORDER BY ms.LeaveDate DESC " +
            "LIMIT 1";


        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchId);
            pstmt.setString(2, tf_student_id_CNIC.getText());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {

                    STUDENT_ID = rs.getInt("StudentID");
                    STUDENT_NAME = rs.getString("Name");
                    ROOM_NO = rs.getString("RoomNumber");

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    MESS_OFF = rs.getDate("OffDate").toLocalDate().format(formatter);
                    MESS_ON = rs.getDate("OnDate").toLocalDate().format(formatter);

                    REASON = rs.getString("Reason");
                    STATUS = rs.getBoolean("Status");

                    displayStudentInfo();
                } else {
                    showAlert("Not Found", "No student found with the provided ID/CNIC or no active mess status.");
                }
            }
        } catch (SQLException e) {
            showAlert("Database Error", "Error searching student: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayStudentInfo() {
        t_studentId.setText("Student ID: " + STUDENT_ID);
        t_studentName.setText("Name: " + STUDENT_NAME);
        t_roomNo.setText("Room: " + ROOM_NO);
        t_messOff.setText("Mess Off: " + MESS_OFF);
        t_messOn.setText("Mess On: " + MESS_ON);
        t_reason.setText("Reason: " + REASON);
        t_status.setText("Status: " + (STATUS ? "Active" : "Inactive"));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}