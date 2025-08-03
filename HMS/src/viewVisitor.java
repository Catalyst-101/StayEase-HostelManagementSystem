import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;

public class viewVisitor
{
    @FXML private TextField visitorIdField;
    @FXML private TextField nameField;
    @FXML private TextField cnicField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private Button searchButton;
    @FXML private Button clearButton;

    @FXML
    public void initialize()
    {
        searchButton.setOnAction(event -> searchVisitor());
        clearButton.setOnAction(event -> clearFields());
    }

    @FXML
    private void searchVisitor()
    {
        String input = visitorIdField.getText().trim();

        if (input.isEmpty())
        {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Enter Visitor ID (7 digits) or CNIC (13 digits).");
            return;
        }

        String sql;
        boolean isVisitorId = input.matches("\\d{7}");
        boolean isCnic = input.matches("\\d{13}");

        if (!isVisitorId && !isCnic)
        {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Visitor ID must be 7 digits or CNIC must be 13 digits.");
            return;
        }

        if (isVisitorId)
        {
            sql = "SELECT FirstName, MiddleName, LastName, CNIC, Phone, Email, Gender FROM Visitor WHERE VisitorID = ?";
        }
        else
        {
            sql = "SELECT FirstName, MiddleName, LastName, CNIC, Phone, Email, Gender FROM Visitor WHERE CNIC = ?";
        }

        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, input);
            ResultSet rs = stmt.executeQuery();

            if (rs.next())
            {
                String middle = rs.getString("MiddleName");
                String fullName = rs.getString("FirstName") + (middle.isEmpty() ? " " : " " + middle + " ") + rs.getString("LastName");

                nameField.setText(fullName.trim());
                cnicField.setText(rs.getString("CNIC"));
                phoneField.setText(rs.getString("Phone"));
                emailField.setText(rs.getString("Email"));
                genderComboBox.setValue(rs.getString("Gender"));
            }
            else
            {
                showAlert(Alert.AlertType.ERROR, "Not Found", "Visitor not found.");
            }
        }
        catch (SQLException e)
        {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
        }
    }

    @FXML
    private void clearFields()
    {
        visitorIdField.clear();
        nameField.clear();
        cnicField.clear();
        phoneField.clear();
        emailField.clear();
        genderComboBox.setValue(null);
    }

    private void showAlert(Alert.AlertType type, String title, String content)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
