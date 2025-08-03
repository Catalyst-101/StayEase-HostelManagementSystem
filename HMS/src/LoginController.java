import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class LoginController {

    @FXML
    private PasswordField TF_password;

    @FXML
    private TextField TF_user_email;

    @FXML
    void BTN_login(ActionEvent event) throws IOException {
        String email = TF_user_email.getText().trim();
        String password = TF_password.getText().trim();

        if (validateUser(email, password)) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainpage.fxml"));
            Parent root = loader.load();

            MainPageController mainController = loader.getController();
            mainController.setUserCredentials(email);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Main Page");
            stage.show();
        } else {
            showAlert("Login Failed", "Invalid email or password!");
        }
    }

    private boolean validateUser(String email, String password) {
        String procedureCall = "{CALL ValidateUser(?, ?)}";

        try (Connection conn = DBHelper.getConnection();
             CallableStatement stmt = conn.prepareCall(procedureCall)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // User exists if ResultSet has rows
            }

        } catch (SQLException e) {
            showAlert("Database Error", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    void TF_forgot(MouseEvent event) {
        showAlert("Forgot Password", "Forgot password clicked!");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
