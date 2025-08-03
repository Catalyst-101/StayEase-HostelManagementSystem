import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import java.sql.*;

public class ViewInvoiceController {

    @FXML
    private TableColumn<ObservableList<String>, String> colInvoiceId;

    @FXML
    private TableColumn<ObservableList<String>, String> colType;

    @FXML
    private TableColumn<ObservableList<String>, String> colAmount;

    @FXML
    private TableColumn<ObservableList<String>, String> colIssueDate;

    @FXML
    private TableColumn<ObservableList<String>, String> colDueDate;

    @FXML
    private TableColumn<ObservableList<String>, String> colStatus;

    @FXML
    private TableColumn<ObservableList<String>, String> colStudentId;

    @FXML
    private TableView<ObservableList<String>> invoiceTable;

    @FXML
    private TextField tfStudentID;

    private final ObservableList<ObservableList<String>> invoiceData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up the columns to show the corresponding data from the ObservableList
        colInvoiceId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(0)));
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(1)));
        colAmount.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(2)));
        colIssueDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(3)));
        colDueDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(4)));
        colStatus.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(5)));
        colStudentId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(6)));
    }

    // Method to search invoices by Student ID or CNIC
    @FXML
    public void btnAdd(ActionEvent event) {
        // Clear previous results
        invoiceData.clear();

        String studentId = tfStudentID.getText().trim();

        if (studentId.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter a Student ID or CNIC.");
            return;
        }

        // Query to find the student based on CNIC or StudentID
        String query = "SELECT i.InvoiceID, i.InvoiceType, i.Amount, i.IssueDate, i.DueDate, i.Status, s.StudentID " +
                       "FROM Invoice i " +
                       "JOIN Student s ON i.StudentID = s.StudentID " +
                       "JOIN User u ON s.UserID = u.UserID " +
                       "WHERE s.StudentID = ? OR u.CNIC = ?";

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentId);
            stmt.setString(2, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ObservableList<String> row = FXCollections.observableArrayList();
                    row.add(rs.getString("InvoiceID"));
                    row.add(rs.getString("InvoiceType"));
                    row.add(rs.getString("Amount"));
                    row.add(rs.getString("IssueDate"));
                    row.add(rs.getString("DueDate"));
                    row.add(rs.getString("Status"));
                    row.add(rs.getString("StudentID"));

                    invoiceData.add(row);
                }

                if (invoiceData.isEmpty()) {
                    showAlert(Alert.AlertType.INFORMATION, "No Invoices Found", "No invoices found for the provided Student ID or CNIC.");
                }

                invoiceTable.setItems(invoiceData);

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch invoice data: " + e.getMessage());
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Connection Error", "Failed to connect to the database: " + e.getMessage());
        }
    }

    // Show alert message
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void btnClearAll(ActionEvent event) {
        // Clear all data from the table and reset the input field
        invoiceData.clear();
        tfStudentID.clear();

        // Show success message
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "All invoices cleared!", ButtonType.OK);
        alert.setHeaderText("Success");
        alert.showAndWait();
    }

    @FXML
    void btnName(ActionEvent event) {
        // Implement this method if needed
    }
}
