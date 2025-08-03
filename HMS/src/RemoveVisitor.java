import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveVisitor {
    @FXML
    private TextField visitorIdField;
    @FXML
    private TextField visitorCnicField;
    @FXML
    private Button removeButton;
    @FXML
    private Button clearButton;

    @FXML
    public void initialize() {
        removeButton.setOnAction(event -> handleRemoveVisitor());
        clearButton.setOnAction(event -> clearFields());
    }

    @FXML
    private void handleRemoveVisitor() {
        String visitorId = visitorIdField.getText().trim();
        String cnic = visitorCnicField.getText().trim();

        if (visitorId.isEmpty() && cnic.isEmpty()) {
            showAlert(AlertType.ERROR, "Validation Error", "Please enter either Visitor ID or CNIC.");
            return;
        }

        boolean removed = false;
        if (!visitorId.isEmpty()) {
            removed = removeVisitorById(visitorId);
        } else if (!cnic.isEmpty()) {
            removed = removeVisitorByCnic(cnic);
        }

        if (removed) {
            showAlert(AlertType.INFORMATION, "Success", "Visitor removed successfully.");
            clearFields();
        } else {
            showAlert(AlertType.ERROR, "Failure", "Visitor not found or could not be removed.");
        }
    }

    private boolean removeVisitorById(String visitorId) {
        String query = "DELETE FROM visitor WHERE VisitorID = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(visitorId));
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Invalid Input", "Visitor ID must be a number.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to remove visitor by ID.");
        }
        return false;
    }

    private boolean removeVisitorByCnic(String cnic) {
        String query = "DELETE FROM visitor WHERE cnic = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, cnic);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Failed to remove visitor by CNIC.");
        }
        return false;
    }

    @FXML
    private void clearFields() {
        visitorIdField.clear();
        visitorCnicField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
