import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;

import java.sql.*;

public class ViewAllRoomsController {

    @FXML
    private TableView<ObservableList<String>> roomTable;

    @FXML
    private TableColumn<ObservableList<String>, String> hostelid;

    @FXML
    private TableColumn<ObservableList<String>, String> roomid;

    @FXML
    private TableColumn<ObservableList<String>, String> roomno;

    @FXML
    private TableColumn<ObservableList<String>, String> totalcapacity;

    @FXML
    private TableColumn<ObservableList<String>, String> currentcapacity;

    @FXML
    private TableColumn<ObservableList<String>, String> isfull;

    private final ObservableList<ObservableList<String>> roomData = FXCollections.observableArrayList();

    private Connection connection;

    @FXML
    public void initialize() {
        // Initialize DB connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hostelmanagementsystem", "root", "saadkhan2005");
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to connect to the database.");
            return;
        }

        // Make cells editable text (optional)
        hostelid.setCellFactory(TextFieldTableCell.forTableColumn());
        roomid.setCellFactory(TextFieldTableCell.forTableColumn());
        roomno.setCellFactory(TextFieldTableCell.forTableColumn());
        totalcapacity.setCellFactory(TextFieldTableCell.forTableColumn());
        currentcapacity.setCellFactory(TextFieldTableCell.forTableColumn());
        isfull.setCellFactory(TextFieldTableCell.forTableColumn());

        // Bind each column to its respective index
        hostelid.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        roomid.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        roomno.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        totalcapacity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        currentcapacity.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));
        isfull.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(5)));

        // Fetch and display room data
        fetchAndDisplayRoomData();
    }

    private void fetchAndDisplayRoomData() {
        // Empty the existing data before adding new data
        roomData.clear();

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT Room.HostelID, Room.RoomID, Room.RoomNumber, Room.Capacity, " +
                           "COUNT(Student.StudentID) AS CurrentCapacity " +
                           "FROM Room " +
                           "LEFT JOIN Student ON Room.RoomID = Student.RoomID " +
                           "GROUP BY Room.RoomID";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String hostelId = rs.getString("HostelID");
                String roomId = rs.getString("RoomID");
                String roomNo = rs.getString("RoomNumber");
                int totalCapacity = rs.getInt("Capacity");
                int currentCapacity = rs.getInt("CurrentCapacity");
                String isFull = (currentCapacity >= totalCapacity) ? "Yes" : "No";

                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(hostelId);
                row.add(roomId);
                row.add(roomNo);
                row.add(String.valueOf(totalCapacity));
                row.add(String.valueOf(currentCapacity));
                row.add(isFull);

                roomData.add(row);
            }

            roomTable.setItems(roomData);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to retrieve room data.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
