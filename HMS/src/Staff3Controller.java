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

public class Staff3Controller {

    @FXML
    private TableView<ObservableList<String>> staffTable;

    @FXML
    private TableColumn<ObservableList<String>, String> colStaffId;

    @FXML
    private TableColumn<ObservableList<String>, String> colName;

    @FXML
    private TableColumn<ObservableList<String>, String> colCNIC;

    @FXML
    private TableColumn<ObservableList<String>, String> colPhone;

    @FXML
    private TableColumn<ObservableList<String>, String> colJoiningDate;

    @FXML
    private TableColumn<ObservableList<String>, String> colRole;

    private final ObservableList<ObservableList<String>> staffData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set cell factories
        colStaffId.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colCNIC.setCellFactory(TextFieldTableCell.forTableColumn());
        colPhone.setCellFactory(TextFieldTableCell.forTableColumn());
        colJoiningDate.setCellFactory(TextFieldTableCell.forTableColumn());
        colRole.setCellFactory(TextFieldTableCell.forTableColumn());

        // Set value factories
        colStaffId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(0)));
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(1)));
        colCNIC.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(2)));
        colPhone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(3)));
        colJoiningDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(4)));
        colRole.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().get(5)));

        // Load staff data from database
        loadStaffData();
    }

    private void loadStaffData() {
        staffData.clear();

        String query = "SELECT e.EmployeeID AS StaffID, e.FirstName, e.MiddleName, e.LastName, e.CNIC, e.Phone, e.JoiningDate " +
                       "FROM Employee e ";
                       
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();

                String staffId = rs.getString("StaffID");
                String fname = rs.getString("FirstName");
                String mname = rs.getString("MiddleName");
                String lname = rs.getString("LastName");

                String fullName = fname != null ? fname : "";
                if (mname != null && !mname.trim().isEmpty()) {
                    fullName += " " + mname.trim();
                }
                if (lname != null) {
                    fullName += " " + lname;
                }

                String cnic = rs.getString("CNIC") != null ? rs.getString("CNIC") : "N/A";
                String phone = rs.getString("Phone") != null ? rs.getString("Phone") : "N/A";
                String joiningDate = rs.getString("JoiningDate") != null ? rs.getString("JoiningDate") : "N/A";

                // Determine role by checking which role-specific table the staffId exists in
                String role = getStaffRole(conn, staffId);

                row.add(staffId);
                row.add(fullName.trim());
                row.add(cnic);
                row.add(phone);
                row.add(joiningDate);
                row.add(role);

                staffData.add(row);
            }

            staffTable.setItems(staffData);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load staff data.\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getStaffRole(Connection conn, String staffId) {
        try {
            if (existsInTable(conn, "Caretaker", staffId)) return "Caretaker";
            if (existsInTable(conn, "MessHead", staffId)) return "Mess Head";
            if (existsInTable(conn, "OtherStaff", staffId)) return "Other Staff";
            if (existsInTable(conn, "Manager", staffId)) return "Manager";
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Unknown";
    }

    private boolean existsInTable(Connection conn, String tableName, String staffId) throws SQLException {
        String query = "SELECT 1 FROM " + tableName + " WHERE EmployeeID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
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
