import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewAllSTUController {

    @FXML
    private TableView<ObservableList<String>> studentTable;

    @FXML
    private TableColumn<ObservableList<String>, String> colId;

    @FXML
    private TableColumn<ObservableList<String>, String> colName;

    @FXML
    private TableColumn<ObservableList<String>, String> colCNIC;

    @FXML
    private TableColumn<ObservableList<String>, String> colRoomNo;

    @FXML
    private TableColumn<ObservableList<String>, String> colEmail;

    private final ObservableList<ObservableList<String>> studentData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set cell factories
        colId.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colCNIC.setCellFactory(TextFieldTableCell.forTableColumn());
        colRoomNo.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmail.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set value factories
        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        colCNIC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        colRoomNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));

        // Load student data from database
        loadStudentData();
    }

    private void loadStudentData() {
        studentData.clear();

        String query = "SELECT s.StudentID, u.FirstName, u.MiddleName, u.LastName, u.CNIC, u.Email, " +
                       "s.RoomID " +
                       "FROM Student s " +
                       "JOIN User u ON s.UserID = u.UserID";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();

                String studentId = String.valueOf(rs.getInt("StudentID"));
                String fname = rs.getString("FirstName");
                String mname = rs.getString("MiddleName");
                String lname = rs.getString("LastName");

                // Construct full name
                String fullName = fname != null ? fname : "";
                if (mname != null && !mname.trim().isEmpty()) {
                    fullName += " " + mname.trim();
                }
                if (lname != null) {
                    fullName += " " + lname;
                }

                String cnic = rs.getString("CNIC") != null ? rs.getString("CNIC") : "N/A";
                String email = rs.getString("Email") != null ? rs.getString("Email") : "N/A";
               
                String roomNo = "N/A"; // Default value

                String roomId = rs.getString("RoomID");

                if (roomId != null) {
                    // Prepare the query to get the RoomNo from the Room table
                    String query2 = "SELECT RoomNumber FROM Room WHERE RoomID = ?";
                    
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setString(1, roomId);
                        
                        try (ResultSet roomRs = stmt2.executeQuery()) {
                            if (roomRs.next()) {
                                roomNo = roomRs.getString("RoomNumber");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }


                row.add(studentId);
                row.add(fullName.trim());
                row.add(cnic);
                row.add(roomNo);
                row.add(email);

                studentData.add(row);
            }

            studentTable.setItems(studentData);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load student data.\n" + e.getMessage());
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
