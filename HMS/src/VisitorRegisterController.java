import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class VisitorRegisterController {

    @FXML
    private VBox visitorListVBox;

    @FXML
    public void initialize() {
        loadVisitorsFromDatabase();
    }

    private void loadVisitorsFromDatabase() {
        try {
            Connection conn = DBHelper.getConnection();
            if (conn == null) {
                System.out.println("Database connection failed.");
                return;
            }

            String query = "SELECT * FROM visitor";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("VisitorID");
                String firstName = rs.getString("FirstName");
                String middleName = rs.getString("MiddleName");
                String lastName = rs.getString("LastName");
                String cnic = rs.getString("CNIC");
                String gender = rs.getString("Gender");
                String email = rs.getString("Email");
                String phone = rs.getString("Phone");
                int studentID = rs.getInt("StudentID");

                String fullName = firstName +
                        (middleName != null && !middleName.isEmpty() ? " " + middleName : "") +
                        " " + lastName;

                addVisitorEntry(id, fullName, cnic, gender, email, phone, studentID);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addVisitorEntry(int id, String fullName, String cnic, String gender,
                                 String email, String phone, int studentID) {
        HBox row = new HBox(20);
        row.setStyle("-fx-background-color: #f9f9f9; -fx-padding: 10; -fx-border-color: #ccc; -fx-border-radius: 5;");

        Label idLabel = new Label("ID: " + id);
        Label nameLabel = new Label("Name: " + fullName);
        Label cnicLabel = new Label("CNIC: " + cnic);
        Label genderLabel = new Label("Gender: " + gender);
        Label emailLabel = new Label("Email: " + email);
        Label phoneLabel = new Label("Phone: " + phone);
        Label studentIDLabel = new Label("Student ID: " + studentID);

        Font font = new Font("Roboto", 12);
        idLabel.setFont(font);
        nameLabel.setFont(font);
        cnicLabel.setFont(font);
        genderLabel.setFont(font);
        emailLabel.setFont(font);
        phoneLabel.setFont(font);
        studentIDLabel.setFont(font);

        genderLabel.setTextFill(getGenderColor(gender));

        row.getChildren().addAll(idLabel, nameLabel, cnicLabel, genderLabel, emailLabel, phoneLabel, studentIDLabel);
        visitorListVBox.getChildren().add(row);
    }

    private Color getGenderColor(String gender) {
        return switch (gender.toLowerCase()) {
            case "male" -> Color.DARKBLUE;
            case "female" -> Color.HOTPINK;
            case "other" -> Color.DARKVIOLET;
            default -> Color.GRAY;
        };
    }
}
