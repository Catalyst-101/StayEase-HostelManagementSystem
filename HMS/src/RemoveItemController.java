import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RemoveItemController {

    @FXML
    private Text t_info;

    @FXML
    private TextField tf_itemId;

    @FXML
    private TextField tf_quantity;

    @FXML
    private Button addButton;

    @FXML
    private Button clearButton;

    @FXML
    private void initialize() {
        addButton.setOnAction(this::btnRemoveItem);
        clearButton.setOnAction(this::btnClearAll);
    }

    private void btnRemoveItem(ActionEvent event) {
        String itemIdStr = tf_itemId.getText().trim();
        String quantityStr = tf_quantity.getText().trim();

        if (itemIdStr.isEmpty() || quantityStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Item ID and Quantity are required.");
            return;
        }

        if (!itemIdStr.matches("\\d+")) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Item ID must be a valid number.");
            return;
        }

        int itemId;
        int quantityToRemove;
        try {
            itemId = Integer.parseInt(itemIdStr);
            quantityToRemove = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Item ID and Quantity must be numeric.");
            return;
        }

        if (quantityToRemove <= 0) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Quantity must be greater than 0.");
            return;
        }

        try (Connection conn = DBHelper.getConnection()) {
            // Check if item exists
            String checkSql = "SELECT Quantity,ItemName FROM inventory WHERE InventoryID = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, itemId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        showAlert(Alert.AlertType.ERROR, "Item Not Found", "No item found with the given Item ID.");
                        return;
                    }

                    int currentQuantity = rs.getInt("Quantity");
                    String itemName = rs.getString("ItemName");

                    if (currentQuantity < quantityToRemove) {
                        showAlert(Alert.AlertType.ERROR, "Insufficient Quantity", "Cannot remove more than available quantity.");
                        return;
                    }

                    // Proceed to update quantity
                    int updatedQuantity = currentQuantity - quantityToRemove;

                    String updateSql = "UPDATE inventory SET Quantity = ? WHERE InventoryID = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, updatedQuantity);
                        updateStmt.setInt(2, itemId);

                        int affectedRows = updateStmt.executeUpdate();
                        if (affectedRows > 0) {
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Item quantity updated successfully.");
                            t_info.setText("Item: "+itemName+"\nRemaining Quantity: " + updatedQuantity);
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update item.");
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while accessing the database.");
        }
    }

    private void btnClearAll(ActionEvent event) {
        tf_itemId.clear();
        tf_quantity.clear();
        t_info.setText("");
        showAlert(Alert.AlertType.INFORMATION, "Form Cleared", "All fields have been cleared.");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
