import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.*;

public class RemoveStudentController {

    @FXML
    private TextField TF_HostelID;

    @FXML
    private TextField TF_Reason;

    @FXML
    private TextField TF_StudentID;

    private Connection connection;

    public void initialize() {
        connectToDatabase();
    }

    private void connectToDatabase() {
        String url = "jdbc:mysql://localhost:3306/hostelmanagementsystem";
        String user = "root";
        String password = "saadkhan2005"; 

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Connected to database.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "❌ Failed to connect to the database.");
        }
    }

    @FXML
    void btnClearAll(ActionEvent event) {
        TF_HostelID.clear();
        TF_StudentID.clear();
        TF_Reason.clear();
    }

    @FXML

    void btnRemove(ActionEvent event) {
        String hostelId = TF_HostelID.getText().trim();
        String studentIdInput = TF_StudentID.getText().trim();
        String reason = TF_Reason.getText().trim();

        if (hostelId.isEmpty() || studentIdInput.isEmpty() || reason.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields (Hostel ID, Student ID, and Reason) must be filled.");
            return;
        }

        if (!hostelId.matches("\\d{7}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Hostel ID", "Hostel ID must be exactly 7 digits.");
            return;
        }

        if (!studentIdInput.matches("\\d{7}") && !studentIdInput.matches("\\d{13}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Student ID", "Student ID must be:\n- 7 digits (Student ID)\n- 13 digits (CNIC)");
            return;
        }

        try {
            boolean removed = false;

            if (studentIdInput.length() == 13) {
                // CNIC entered
                String query = """
                    SELECT u.UserID FROM User u
                    JOIN Student s ON u.UserID = s.UserID
                    WHERE u.CNIC = ? AND s.HostelID = ?
                """;

                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setString(1, studentIdInput);
                    stmt.setInt(2, Integer.parseInt(hostelId));
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        int userId = rs.getInt("UserID");
                        removed = deleteUserByID(userId);
                    }
                }

            } else {
                // StudentID entered
                String query = "SELECT UserID FROM Student WHERE StudentID = ? AND HostelID = ?";

                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, Integer.parseInt(studentIdInput));
                    stmt.setInt(2, Integer.parseInt(hostelId));
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        int userId = rs.getInt("UserID");
                        removed = deleteUserByID(userId);
                    }
                }
            }

            if (removed) {
                showAlert(Alert.AlertType.INFORMATION, "Student Removed",
                        "✅ Student with ID **" + studentIdInput + "** has been removed from hostel **" +
                                hostelId + "**.\n📌 Reason: " + reason);
                btnClearAll(null);
            } else {
                showAlert(Alert.AlertType.WARNING, "Not Found",
                        "⚠️ No student found with the given ID in hostel " + hostelId + ".");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while removing the student.");
        }
    }

    private boolean deleteUserByID(int userId) throws SQLException {
        String deleteQuery = "DELETE FROM User WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteQuery)) {
            stmt.setInt(1, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        }
    }
    

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
