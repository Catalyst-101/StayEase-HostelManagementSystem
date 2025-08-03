import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;

public class AuditController {

    @FXML
    private TableView<ObservableList<String>> AuditTable;

    @FXML
    private TableColumn<ObservableList<String>, String> colAuditId;

    @FXML
    private TableColumn<ObservableList<String>, String> colTableName;

    @FXML
    private TableColumn<ObservableList<String>, String> colActionType;

    @FXML
    private TableColumn<ObservableList<String>, String> colActionDate;

    private final String DB_URL = "jdbc:mysql://localhost:3306/hostelmanagementsystem";
    private final String DB_USER = "root";
    private final String DB_PASS = "saadkhan2005";

    @FXML
    public void initialize() {
        colAuditId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        colTableName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        colActionType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        colActionDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));

        loadAuditLogs();
    }

    private void loadAuditLogs() {
        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        String query = "SELECT * FROM AuditLog";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("AuditID"));
                row.add(rs.getString("TableName"));
                row.add(rs.getString("ActionType"));
                row.add(rs.getString("ActionDate"));
                data.add(row);
            }

            AuditTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
