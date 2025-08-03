import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddItemController {

    @FXML
    private Button addButton;
    @FXML
    private Button clearButton;
    @FXML
    private MenuButton mb_category;
    @FXML
    private MenuButton tfOption;
    @FXML
    private TextField tf_description;
    @FXML
    private TextField tf_itemId;
    @FXML
    private TextField tf_itemName;
    @FXML
    private TextField tf_quantity;

    private String selectedCategory = "Category";
    private String selectedOption = "Add Item";

    @FXML
    private void initialize() {
        setupCategoryMenu();
        setupOptionMenu();

        addButton.setOnAction(this::handleAction);
        clearButton.setOnAction(this::btnClearAll);
    }

    private void setupCategoryMenu() {
        String[] categories = {"Food", "Cleaning", "Repairing", "Other"};
        mb_category.getItems().clear();
        for (String cat : categories) {
            MenuItem item = new MenuItem(cat);
            item.setOnAction(e -> {
                selectedCategory = item.getText();
                mb_category.setText(selectedCategory);
            });
            mb_category.getItems().add(item);
        }
    }

    private void setupOptionMenu() {
        MenuItem addItemOption = new MenuItem("Add Item");
        addItemOption.setOnAction(e -> setOption("Add Item"));
        MenuItem restockOption = new MenuItem("Restocking");
        restockOption.setOnAction(e -> setOption("Restocking"));

        tfOption.getItems().clear();
        tfOption.getItems().addAll(addItemOption, restockOption);
    }

    private void setOption(String option) {
        selectedOption = option;
        tfOption.setText(option);

        if ("Restocking".equals(selectedOption)) {
            tf_itemId.setEditable(true);
            tf_itemName.setEditable(false);
            tf_description.setEditable(false);
            tf_itemName.clear();
        } else {
            tf_itemId.setEditable(false);
            tf_itemId.clear();
            tf_itemName.setEditable(true);
            tf_description.setEditable(true);
        }
    }

    private void handleAction(ActionEvent event) {
        try {
            if ("Restocking".equals(selectedOption)) {
                handleRestocking();
            } else if ("Add Item".equals(selectedOption)) {
                handleAddItem();
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleRestocking() throws SQLException {
        String inventoryId = tf_itemId.getText().trim();
        String quantityStr = tf_quantity.getText().trim();

        // Validate inputs
        if (inventoryId.isEmpty() || quantityStr.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill all required fields.");
            return;
        }

        if (!inventoryId.matches("\\d+{7}")) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Item ID must be a number and should be 7 digits.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Quantity must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Quantity must be a valid number.");
            return;
        }

        // Check if item exists
        if (!itemExists(Integer.parseInt(inventoryId))) {
            showAlert(Alert.AlertType.WARNING, "Not Found", "Item with ID " + inventoryId + " not found.");
            return;
        }

        // Update quantity in database
        String sql = "UPDATE inventory SET Quantity = Quantity + ?, LastUpdated = CURRENT_TIMESTAMP() WHERE InventoryID = ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quantity);
            pstmt.setInt(2, Integer.parseInt(inventoryId));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item restocked successfully!");
                btnClearAll(null);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to restock item.");
            }
        }
    }

    private void handleAddItem() throws SQLException {
        String itemName = tf_itemName.getText().trim();
        String quantityStr = tf_quantity.getText().trim();
        String description = tf_description.getText().trim();

        // Validate inputs
        if (itemName.isEmpty() || quantityStr.isEmpty() || selectedCategory.equals("Category")) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill all required fields.");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Quantity must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Quantity must be a valid number.");
            return;
        }

        // Check if item already exists
        if (itemNameExists(itemName)) {
            showAlert(Alert.AlertType.WARNING, "Duplicate Item", "An item with this name already exists.");
            return;
        }

        // Insert new item
        String sql = "INSERT INTO inventory(ItemName, Category, Quantity, Description, LastUpdated) " +
                "VALUES(?, ?, ?, ?, CURRENT_TIMESTAMP())";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, itemName);
            pstmt.setString(2, selectedCategory);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, description);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Item added successfully!");
                btnClearAll(null);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add item.");
            }
        }
    }

    private boolean itemExists(int inventoryId) throws SQLException {
        String sql = "SELECT 1 FROM inventory WHERE InventoryID = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, inventoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private boolean itemNameExists(String itemName) throws SQLException {
        String sql = "SELECT 1 FROM inventory WHERE ItemName = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, itemName);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void btnClearAll(ActionEvent event) {
        tf_itemName.clear();
        tf_quantity.clear();
        tf_description.clear();
        tf_itemId.clear();
        mb_category.setText("Category");
        selectedCategory = "Category";
        tfOption.setText("Select Option");
        selectedOption = "Add Item";
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Empty handlers for FXML elements
    @FXML void btnCategory(ActionEvent event) {}
    @FXML void btnOption(ActionEvent event) {}
    @FXML void handleRestocking(ActionEvent event) {}
    @FXML void handleNewItem(ActionEvent event) {}
}