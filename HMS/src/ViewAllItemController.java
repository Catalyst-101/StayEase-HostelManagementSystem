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

public class ViewAllItemController {

    @FXML
    private TableColumn<ObservableList<String>, String> categoryColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> descriptionColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> itemIdColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> itemNameColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> quantityColumn;

    @FXML
    private TableView<ObservableList<String>> tableView;

    private final ObservableList<ObservableList<String>> itemData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Map columns to index positions of ObservableList
        itemIdColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 0));
        itemNameColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 1));
        quantityColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 2));
        categoryColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 3));
        descriptionColumn.setCellValueFactory(data -> getValueSafe(data.getValue(), 4));

        loadDataFromDatabase();
    }

    private void loadDataFromDatabase() {
        String sql = "SELECT InventoryID, ItemName, Quantity, Category, Description FROM inventory";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            itemData.clear();

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(rs.getString("InventoryID"));
                row.add(rs.getString("ItemName"));
                row.add(rs.getString("Quantity"));
                row.add(rs.getString("Category"));
                row.add(rs.getString("Description"));
                itemData.add(row);
            }

            tableView.setItems(itemData);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load inventory data.");
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
