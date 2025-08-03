import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.*;

public class DischargeRoomController {

    @FXML
    private TextField tfstudentid;

    private final String URL = "jdbc:mysql://localhost:3306/hostelmanagementsystem";
    private final String USER = "root"; 
    private final String PASS = "saadkhan2005";  

    @FXML
    void btnclearall(ActionEvent event) {
        tfstudentid.clear();
    }

    @FXML
    void btnsave(ActionEvent event) {
        String input = tfstudentid.getText().trim();

        if (input.isEmpty() || !(input.matches("\\d{7}") || input.matches("\\d{13}"))) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid 7-digit Student ID or 13-digit CNIC.");
            return;
        }

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            PreparedStatement ps;
            ResultSet rs;

            // search student by cnic or id
            if (input.length() == 13) {
                ps = con.prepareStatement("SELECT s.StudentID, s.RoomID FROM Student s JOIN User u ON s.UserID = u.UserID WHERE u.CNIC = ?");
            } else {
                ps = con.prepareStatement("SELECT StudentID, RoomID FROM Student WHERE StudentID = ?");
            }

            ps.setString(1, input);
            rs = ps.executeQuery();

            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "Student not found.");
                return;
            }

            int studentId = rs.getInt("StudentID");
            Integer roomId = rs.getObject("RoomID") != null ? rs.getInt("RoomID") : null;

            if (roomId == null) {
                showAlert(Alert.AlertType.INFORMATION, "No Room Assigned", "This student is not assigned to any room.");
                return;
            }

            // Discharge the student by setting RoomID = NULL
            ps = con.prepareStatement("UPDATE Student SET RoomID = NULL WHERE StudentID = ?");
            ps.setInt(1, studentId);
            int updated = ps.executeUpdate();

            if (updated == 0) {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to discharge the student.");
                return;
            }

            // Check if the room is full and needs to be updated
            ps = con.prepareStatement("SELECT Capacity FROM Room WHERE RoomID = ?");
            ps.setInt(1, roomId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int capacity = rs.getInt("Capacity");

                // Count how many students are currently in that room
                ps = con.prepareStatement("SELECT COUNT(*) AS Occupancy FROM Student WHERE RoomID = ?");
                ps.setInt(1, roomId);
                rs = ps.executeQuery();

                if (rs.next()) {
                    int occupancy = rs.getInt("Occupancy");

                    // If occupancy is less than capacity, mark room as not full
                    if (occupancy < capacity) {
                        ps = con.prepareStatement("UPDATE Room SET IsFull = FALSE WHERE RoomID = ?");
                        ps.setInt(1, roomId);
                        ps.executeUpdate();
                    }
                }
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Student discharged from room successfully.");
            tfstudentid.clear();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
