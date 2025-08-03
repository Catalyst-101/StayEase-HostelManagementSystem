import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RoomAllocationController {

    @FXML private MenuButton tfRoomNo;
    @FXML private DatePicker tfallocationdate;
    @FXML private TextField tfhostelid;
    @FXML private DatePicker tfleavingdate;
    @FXML private TextField tfroomrent;
    @FXML private MenuButton tfroomtype;
    @FXML private TextField tfstudentid;

    private Connection connection;
    private String selectedRoomType;

    @FXML
    public void initialize() {
        try {
            this.connection = DBHelper.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not connect to the database:\n" + e.getMessage());
        }
    }

    // Handle room type selection
    @FXML
    void handleRoomType(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        selectedRoomType = source.getText();
        tfroomtype.setText(selectedRoomType);
        populateAvailableRooms();
    }

    // Populate available rooms in the menu button
    private void populateAvailableRooms() {
        tfRoomNo.getItems().clear();

        String hostelId = tfhostelid.getText().trim();
        if (!hostelId.matches("\\d{7}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Hostel ID", "Enter a valid 7-digit Hostel ID before selecting a room.");
            return;
        }

        try {
            String sql = "SELECT RoomNumber FROM Room WHERE HostelID = ? AND Capacity = ? AND IsFull = FALSE";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(hostelId));
            ps.setInt(2, selectedRoomType.equals("Two Seater") ? 2 : 3);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                found = true;
                String roomNo = rs.getString("RoomNumber");
                MenuItem item = new MenuItem(roomNo);
                item.setOnAction(e -> tfRoomNo.setText(roomNo));
                tfRoomNo.getItems().add(item);
            }

            if (!found) {
                showAlert(Alert.AlertType.INFORMATION, "No Rooms", "No available rooms of selected type.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load rooms: " + e.getMessage());
        }
    }

    // Allocate the selected room
    @FXML
    void btnallocate(ActionEvent event) {
        String hostelId = tfhostelid.getText().trim();
        String studentId = tfstudentid.getText().trim();
        String roomNo = tfRoomNo.getText();
        LocalDate allocDate = tfallocationdate.getValue();

        if (!hostelId.matches("\\d{7}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Hostel ID", "Enter a valid 7-digit Hostel ID.");
            return;
        }

        if (!studentId.matches("\\d{7}|\\d{13}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Student ID", "Enter a valid 7-digit Student ID or 13-digit CNIC.");
            return;
        }

        if (roomNo.equals("Select Room Number")) {
            showAlert(Alert.AlertType.ERROR, "Room Not Selected", "Please select a room.");
            return;
        }

        if (allocDate == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid Allocation Date", "Please select a valid allocation date.");
            return;
        }

        try {
            PreparedStatement ps;

            // Check if hostel exists
            ps = connection.prepareStatement("SELECT * FROM Hostel WHERE HostelID = ?");
            ps.setInt(1, Integer.parseInt(hostelId));
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Hostel ID", "No hostel found with ID: " + hostelId);
                return;
            }

            // Check if student exists and get their RoomID and UserID
            int studentUserId = -1;
            int existingRoomId = -1;
            if (studentId.length() == 7) {
                ps = connection.prepareStatement("SELECT UserID, RoomID FROM Student WHERE StudentID = ?");
                ps.setInt(1, Integer.parseInt(studentId));
            } else {
                ps = connection.prepareStatement("SELECT s.UserID, s.RoomID FROM Student s JOIN User u ON s.UserID = u.UserID WHERE u.CNIC = ?");
                ps.setString(1, studentId);
            }

            rs = ps.executeQuery();
            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Student", "No student found with provided ID or CNIC.");
                return;
            }

            studentUserId = rs.getInt("UserID");
            existingRoomId = rs.getInt("RoomID");

            // Check if student already has a room
            if (existingRoomId != 0) {
                showAlert(Alert.AlertType.ERROR, "Student Already Assigned", "This student is already assigned to a room.");
                return;
            }

            // Get room details
            ps = connection.prepareStatement("SELECT RoomID, Capacity, IsFull FROM Room WHERE RoomNumber = ? AND HostelID = ?");
            ps.setString(1, roomNo);
            ps.setInt(2, Integer.parseInt(hostelId));
            rs = ps.executeQuery();
            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Room Not Found", "Room " + roomNo + " not found in hostel " + hostelId);
                return;
            }

            int roomId = rs.getInt("RoomID");
            int capacity = rs.getInt("Capacity");
            boolean isFull = rs.getBoolean("IsFull");

            if (isFull) {
                showAlert(Alert.AlertType.ERROR, "Room Full", "Selected room is already full.");
                return;
            }

            // Update student allocation
            ps = connection.prepareStatement("UPDATE Student SET RoomID = ?, HostelID = ?, AllotmentDate = ? WHERE UserID = ?");
            ps.setInt(1, roomId);
            ps.setInt(2, Integer.parseInt(hostelId));
            ps.setDate(3, java.sql.Date.valueOf(allocDate));
            ps.setInt(4, studentUserId);
            int updated = ps.executeUpdate();

            if (updated == 0) {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to allocate room.");
                return;
            }

            // Check if room is full now
            ps = connection.prepareStatement("SELECT COUNT(*) AS count FROM Student WHERE RoomID = ?");
            ps.setInt(1, roomId);
            rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) count = rs.getInt("count");

            if (count >= capacity) {
                ps = connection.prepareStatement("UPDATE Room SET IsFull = TRUE WHERE RoomID = ?");
                ps.setInt(1, roomId);
                ps.executeUpdate();
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Room allocated successfully.");
            clearFields();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong:\n" + e.getMessage());
        }
    }

    // Clear fields
    @FXML
    void btnclear(ActionEvent event) {
        clearFields();
    }

    private void clearFields() {
        tfhostelid.clear();
        tfstudentid.clear();
        tfRoomNo.setText("Select Room Number");
        tfRoomNo.getItems().clear();
        tfroomtype.setText("Select Room Type");
        tfroomtype.getItems().clear();
        tfallocationdate.setValue(null);
        tfleavingdate.setValue(null);
        tfroomrent.clear();
    }

    // Unused button stubs (linked in FXML)
    @FXML void btnRoomNo(ActionEvent event) {}
    @FXML void btnRoomType(ActionEvent event) {}
    @FXML void btnAllocationDate(ActionEvent event) {}
    @FXML void btnLeavingDate(ActionEvent event) {}

    // Utility to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

