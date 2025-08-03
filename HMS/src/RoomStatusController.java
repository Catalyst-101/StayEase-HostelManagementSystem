// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.control.Alert;
// import javafx.scene.control.TextField;

// import java.util.HashMap;
// import java.util.Map;

// public class RoomStatusController {

//     @FXML
//     private TextField tf_capacity_show;

//     @FXML
//     private TextField tf_room_type_show;

//     @FXML
//     private TextField tf_roomno_show;

//     @FXML
//     private TextField tf_status_show;

//     @FXML
//     private TextField tfroomno;

//     private Map<String, RoomData> dummyRoomDatabase;

//     @FXML
//     public void initialize() {
//         // Make display fields non-editable
//         tf_capacity_show.setEditable(false);
//         tf_room_type_show.setEditable(false);
//         tf_roomno_show.setEditable(false);
//         tf_status_show.setEditable(false);

//         // Dummy room data (room number → RoomData)
//         dummyRoomDatabase = new HashMap<>();
//         dummyRoomDatabase.put("101", new RoomData("101", "Two Seater", 1, 2, "Occupied"));
//         dummyRoomDatabase.put("102", new RoomData("102", "Three Seater", 0, 3, "Available"));
//         dummyRoomDatabase.put("103", new RoomData("103", "Other", 3, 3, "Full"));
//     }

//     @FXML
//     void btnClear(ActionEvent event) {
//         tfroomno.clear();
//         tf_roomno_show.clear();
//         tf_room_type_show.clear();
//         tf_capacity_show.clear();
//         tf_status_show.clear();
//     }

//     @FXML
//     void btnSearch(ActionEvent event) {
//         String roomNo = tfroomno.getText().trim();

//         if (roomNo.isEmpty()) {
//             showAlert(Alert.AlertType.ERROR, "Error", "Please enter a room number.");
//             return;
//         }

//         if (!roomNo.matches("\\d+")) {
//             showAlert(Alert.AlertType.ERROR, "Invalid Room Number", "Room number must contain digits only.");
//             return;
//         }

//         RoomData room = dummyRoomDatabase.get(roomNo);
//         if (room == null) {
//             showAlert(Alert.AlertType.WARNING, "Not Found", "Room number not found.");
//             return;
//         }

//         tf_roomno_show.setText(room.roomNo);
//         tf_room_type_show.setText(room.roomType);
//         tf_capacity_show.setText(room.occupied + "/" + room.capacity);
//         tf_status_show.setText(room.status);
//     }

//     private void showAlert(Alert.AlertType type, String title, String message) {
//         Alert alert = new Alert(type);
//         alert.setTitle(title);
//         alert.setHeaderText(null);
//         alert.setContentText(message);
//         alert.showAndWait();
//     }

//     // Inner class to represent room data
//     private static class RoomData {
//         String roomNo;
//         String roomType;
//         int occupied;
//         int capacity;
//         String status;

//         RoomData(String roomNo, String roomType, int occupied, int capacity, String status) {
//             this.roomNo = roomNo;
//             this.roomType = roomType;
//             this.occupied = occupied;
//             this.capacity = capacity;
//             this.status = status;
//         }
//     }
// }

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.*;

public class RoomStatusController {

    @FXML
    private TextField tf_capacity_show;

    @FXML
    private TextField tf_room_type_show;

    @FXML
    private TextField tf_roomno_show;

    @FXML
    private TextField tf_status_show;

    @FXML
    private TextField tfhostelid;

    @FXML
    private TextField tfroomno;

    private Connection connection;

    @FXML
    public void initialize() {
        tf_capacity_show.setEditable(false);
        tf_room_type_show.setEditable(false);
        tf_roomno_show.setEditable(false);
        tf_status_show.setEditable(false);

        // Initialize DB connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "saadkhan2005");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
            e.printStackTrace();
        }
    }

    @FXML
    void btnClear(ActionEvent event) {
        tfhostelid.clear();
        tfroomno.clear();
        tf_roomno_show.clear();
        tf_room_type_show.clear();
        tf_capacity_show.clear();
        tf_status_show.clear();
    }

    @FXML
    void btnSearch(ActionEvent event) {
        String hostelId = tfhostelid.getText().trim();
        String roomNo = tfroomno.getText().trim();

        if (hostelId.isEmpty() || roomNo.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter both Hostel ID and Room Number.");
            return;
        }

        if (!hostelId.matches("\\d{7}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Hostel ID", "Hostel ID must be 7 digits.");
            return;
        }

        if (!roomNo.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Room Number", "Room number must be digits only.");
            return;
        }

        try {
            // Check if hostel exists
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM Hostel WHERE HostelID = ?");
            ps.setInt(1, Integer.parseInt(hostelId));
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Invalid Hostel", "Hostel with ID " + hostelId + " does not exist.");
                return;
            }

            // Get room info from Room table
            ps = connection.prepareStatement("SELECT * FROM Room WHERE HostelID = ? AND RoomNumber = ?");
            ps.setInt(1, Integer.parseInt(hostelId));
            ps.setString(2, roomNo);
            rs = ps.executeQuery();
            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Room Not Found", "Room " + roomNo + " not found in Hostel " + hostelId);
                return;
            }

            int roomId = rs.getInt("RoomID");
            String roomType = rs.getString("Type");
            int capacity = rs.getInt("Capacity");

            // Count number of students in this room
            ps = connection.prepareStatement("SELECT COUNT(*) AS count FROM Student WHERE RoomID = ?");
            ps.setInt(1, roomId);
            rs = ps.executeQuery();
            int occupied = 0;
            if (rs.next()) {
                occupied = rs.getInt("count");
            }

            // Determine room status
            String status;
            if (occupied == 0) {
                status = "Available";
            } else if (occupied >= capacity) {
                status = "Full";
            } else {
                status = "Occupied";
            }

            // Display details
            tf_roomno_show.setText(roomNo);
            tf_room_type_show.setText(roomType);
            tf_capacity_show.setText(occupied + " / " + capacity);
            tf_status_show.setText(status);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Something went wrong:\n" + e.getMessage());
            e.printStackTrace();
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

