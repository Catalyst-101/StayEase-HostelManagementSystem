import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewAttendanceController {

    @FXML
    private TableColumn<ObservableList<String>, String> studentIdColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> attendanceIdColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> recordedByColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> studentNameColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> roomNoColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> dateColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> statusColumn;

    @FXML
    private TableView<ObservableList<String>> tableView;

    private final ObservableList<ObservableList<String>> attData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Map columns to index positions of ObservableList
        attendanceIdColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 0));
        studentIdColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 1));
        studentNameColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 2));
        roomNoColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 3));
        dateColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 4));
        statusColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 5));
        recordedByColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 6));


        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        String sql = "SELECT a.AttendanceID,a.StudentID,a.AttendanceDate,a.Status,a.RecordedBy,CONCAT(u.FirstName, ' ', COALESCE(u.MiddleName, ''), ' ', u.LastName) AS Name,r.RoomNumber\n" +
                "FROM attendance a\n" +
                "JOIN student s\n" +
                "USING(StudentID)\n" +
                "JOIN user u\n" +
                "USING(UserID)\n" +
                "JOIN room r\n" +
                "ON s.RoomID = r.RoomID;";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            attData.clear();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("AttendanceID"));
                row.add(rs.getString("StudentID"));
                row.add(rs.getString("Name"));
                row.add(rs.getString("RoomNumber"));
                row.add(rs.getString("AttendanceDate"));
                row.add(rs.getString("Status"));
                row.add(rs.getString("RecordedBy"));
                attData.add(row);
            }

            tableView.setItems(attData);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load attendance data.");
        }
    }

    private ReadOnlyStringWrapper getValueSafe(ObservableList<String> list, int index) {
        if (list != null && list.size() > index) {
            return new ReadOnlyStringWrapper(list.get(index));
        } else {
            return new ReadOnlyStringWrapper(""); // fallback if missing data
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
