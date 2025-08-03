import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;

public class AddVisitorController {

    @FXML
    private TextField Address;

    @FXML
    private TextField CNIC;

    @FXML
    private TextField CellNO;

    @FXML
    private TextField Email;

    @FXML
    private MenuButton GENDER;

    @FXML
    private TextField StudentID;

    @FXML
    private TextField VisitorName;

    @FXML
    void btnAdd(ActionEvent event) {
        String name = VisitorName.getText();
        String cnic = CNIC.getText();
        String phone = CellNO.getText();
        String email = Email.getText();
        String address = Address.getText();
        String studentId = StudentID.getText();
        String gender = GENDER.getText();

        if (name.isEmpty() || cnic.isEmpty() || phone.isEmpty() || email.isEmpty()
                || address.isEmpty() || studentId.isEmpty() || gender.equals("Gender")) {
            showAlert("Fill all fields");
            return;
        }

        showAlert("Visitor added:\nName: " + name + "\nCNIC: " + cnic + "\nPhone: " + phone
                + "\nEmail: " + email + "\nAddress: " + address + "\nStudent ID: " + studentId + "\nGender: " + gender);
        clearFields();
    }

    @FXML
    void btnClearAll(ActionEvent event) {
        clearFields();
    }

    @FXML
    void handleGenderSelect(ActionEvent event) {
        MenuItem selected = (MenuItem) event.getSource();
        GENDER.setText(selected.getText());
    }

    @FXML
    void btnAddress(ActionEvent event) {}
    @FXML
    void btnCNIC(ActionEvent event) {}
    @FXML
    void btnCell(ActionEvent event) {}
    @FXML
    void btnEmailStudent(ActionEvent event) {}
    @FXML
    void btnGender(ActionEvent event) {}
    @FXML
    void btnName(ActionEvent event) {}

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notice");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void clearFields() {
        VisitorName.clear();
        CNIC.clear();
        CellNO.clear();
        Email.clear();
        Address.clear();
        StudentID.clear();
        GENDER.setText("Gender");
    }
}
