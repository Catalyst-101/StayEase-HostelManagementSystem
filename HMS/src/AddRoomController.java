// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.control.Alert;
// import javafx.scene.control.MenuButton;
// import javafx.scene.control.MenuItem;
// import javafx.scene.control.TextField;

// public class AddRoomController {

//     @FXML
//     private MenuButton RoomType;

//     @FXML
//     private TextField TF_hostelID;

//     @FXML
//     private TextField TF_other;

//     @FXML
//     private TextField TF_roomno;

//     private String selectedRoomType = "";

//     @FXML
//     void initialize() {
//         // Optional: Initialize MenuItems dynamically if needed
//     }

//     @FXML
//     void btnAdd(ActionEvent event) {
//         String hostelId = TF_hostelID.getText().trim();
//         String roomNo = TF_roomno.getText().trim();
//         String other = TF_other.getText().trim();

//         if (hostelId.isEmpty() || roomNo.isEmpty() || selectedRoomType.isEmpty()) {
//             showAlert(Alert.AlertType.ERROR, "Missing Data", "Please fill all required fields and select a room type.");
//             return;
//         }

//         // Hostel ID: Must be exactly 7 digits
//         if (!hostelId.matches("\\d{7}")) {
//             showAlert(Alert.AlertType.ERROR, "Invalid Hostel ID", "Hostel ID must contain exactly 7 digits.");
//             return;
//         }

//         // Room No: Must be numeric and <1000
//         if (!roomNo.matches("\\d+")) {
//             showAlert(Alert.AlertType.ERROR, "Invalid Room Number", "Room number must contain digits only.");
//             return;
//         }

//         int roomNumber = Integer.parseInt(roomNo);
//         if (roomNumber >= 1000) {
//             showAlert(Alert.AlertType.ERROR, "Invalid Room Number", "Room number must be less than 1000.");
//             return;
//         }

//         // Success message
//         StringBuilder message = new StringBuilder("Room added successfully:\n");
//         message.append("Hostel ID: ").append(hostelId).append("\n");
//         message.append("Room No: ").append(roomNo).append("\n");
//         message.append("Room Type: ").append(selectedRoomType);

//         if (!other.isEmpty()) {
//             message.append("\nOther: ").append(other);
//         }

//         showAlert(Alert.AlertType.INFORMATION, "Success", message.toString());
//         clearAllFields();
//     }

//     @FXML
//     void btnClearAll(ActionEvent event) {
//         clearAllFields();
//         showAlert(Alert.AlertType.INFORMATION, "Cleared", "All fields have been cleared.");
//     }

//     @FXML
//     void handleRoomType(ActionEvent event) {
//         MenuItem source = (MenuItem) event.getSource();
//         selectedRoomType = source.getText();
//         RoomType.setText(selectedRoomType);
//         showAlert(Alert.AlertType.INFORMATION, "Room Type Selected", selectedRoomType + " selected.");
//     }

//     @FXML
//     void btnRoomType(ActionEvent event) {
//         showAlert(Alert.AlertType.INFORMATION, "Room Type Info", "Please select a room type from the dropdown.");
//     }

//     private void clearAllFields() {
//         TF_hostelID.clear();
//         TF_roomno.clear();
//         TF_other.clear();
//         selectedRoomType = "";
//         RoomType.setText("Select Room Type");
//     }

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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class AddRoomController {

    @FXML
    private MenuButton RoomType;

    @FXML
    private TextField TF_hostelID;

    @FXML
    private TextField TF_other;

    @FXML
    private TextField TF_roomno;

    private String selectedRoomType = "";

    @FXML
    void initialize() {
    }

    @FXML
void btnAdd(ActionEvent event) {
    String hostelIdStr = TF_hostelID.getText().trim();
    String roomNo = TF_roomno.getText().trim();
    String other = TF_other.getText().trim();

    if (hostelIdStr.isEmpty() || roomNo.isEmpty() || selectedRoomType.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, "Missing Data", "Please fill all required fields and select a room type.");
        return;
    }

    // hostel id check validation
    if (!hostelIdStr.matches("\\d{7}")) {
        showAlert(Alert.AlertType.ERROR, "Invalid Hostel ID", "Hostel ID must contain exactly 7 digits.");
        return;
    }

    // validate room number
    if (!roomNo.matches("\\d+")) {
        showAlert(Alert.AlertType.ERROR, "Invalid Room Number", "Room number must contain digits only.");
        return;
    }

    int hostelId = Integer.parseInt(hostelIdStr);
    int roomNumber = Integer.parseInt(roomNo);

    if (roomNumber >= 1000) {
        showAlert(Alert.AlertType.ERROR, "Invalid Room Number", "Room number must be less than 1000.");
        return;
    }

    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "saadkhan2005")) {

        // Check if hostel exists
        String checkHostelSQL = "SELECT * FROM Hostel WHERE HostelID = ?";
        try (PreparedStatement ps = conn.prepareStatement(checkHostelSQL)) {
            ps.setInt(1, hostelId);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Hostel Not Found", "No hostel exists with the given Hostel ID.");
                return;
            }
        }

        // Check if room already exists for THIS hostel
        String checkRoomSQL = "SELECT * FROM Room WHERE HostelID = ? AND RoomNumber = ?";
        try (PreparedStatement ps = conn.prepareStatement(checkRoomSQL)) {
            ps.setInt(1, hostelId);
            ps.setString(2, roomNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                showAlert(Alert.AlertType.ERROR, "Duplicate Room", "Room number already exists for this hostel.");
                return;
            }
        }

        String roomType;
        int capacity;

        if (selectedRoomType.equals("Two Seater")) {
            roomType = "Two Seater";
            capacity = 2;
        } else if (selectedRoomType.equals("Three Seater")) {
            roomType = "Three Seater";
            capacity = 3;
        } else {
            if (other.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Missing Room Type", "Please specify the room type in the 'Other' field.");
                return;
            }
            roomType = other;
            capacity = 100;
        }

     
        try (CallableStatement cs = conn.prepareCall("{ call AddRoom(?, ?, ?, ?) }")) {
            cs.setInt(1, hostelId);
            cs.setString(2, roomNo);
            cs.setString(3, roomType);
            cs.setInt(4, capacity);
            cs.execute();
        }


        showAlert(Alert.AlertType.INFORMATION, "Success", "Room added successfully.");

        clearAllFields();

    } catch (SQLException e) {
        e.printStackTrace();
        showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred: " + e.getMessage());
    }
}

    @FXML
    void btnClearAll(ActionEvent event) {
        clearAllFields();
        showAlert(Alert.AlertType.INFORMATION, "Cleared", "All fields have been cleared.");
    }

    @FXML
    void handleRoomType(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        selectedRoomType = source.getText();
        RoomType.setText(selectedRoomType);
    }

    @FXML
    void btnRoomType(ActionEvent event) {
        showAlert(Alert.AlertType.INFORMATION, "Room Type Info", "Please select a room type from the dropdown.");
    }

    private void clearAllFields() {
        TF_hostelID.clear();
        TF_roomno.clear();
        TF_other.clear();
        selectedRoomType = "";
        RoomType.setText("Select Room Type");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
