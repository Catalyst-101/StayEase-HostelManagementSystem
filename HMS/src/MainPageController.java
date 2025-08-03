import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MainPageController {

    @FXML
    private Text name;

    @FXML
    private Text usertype;

    private String userEmail;
    private String userFullName;
    private String userType;

    public void setUserCredentials(String email) {
        this.userEmail = email;
        loadUserData();
        System.out.println("Logged in as: " + userEmail);
    }

    public void loadUserData() {
        String query = "SELECT UserID, FirstName, MiddleName, LastName FROM User WHERE Email = ?";
        try (Connection conn = DBHelper.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userEmail);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String userId = rs.getString("UserID");
                String firstName = rs.getString("FirstName");
                String middleName = rs.getString("MiddleName");
                String lastName = rs.getString("LastName");

                // Handle possible null or empty middle name
                if (middleName == null || middleName.trim().isEmpty()) {
                    userFullName = firstName + " " + lastName;
                } else {
                    userFullName = firstName + " " + middleName + " " + lastName;
                }

                // Check user role
                if (isCaretaker(userId)) {
                    userType = "caretaker";
                } else if (isManager(userId)) {
                    userType = "manager";
                } else {
                    userType = "unknown";
                }

                System.out.println("User: " + userFullName + " | Role: " + userType);
                name.setText(userFullName);
                usertype.setText(userType);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isCaretaker(String userId) {
        String query = "SELECT 1 FROM Caretaker WHERE UserID = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, userId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean isManager(String userId) {
        String query = "SELECT 1 FROM Manager WHERE UserID = ?";
        try (Connection conn = DBHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
    
            stmt.setString(1, userId);
            return stmt.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @FXML
    void btnTakeAttendence(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TakeAttendence.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Attendence");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnViewAtten(ActionEvent event)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ViewAttendance.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("View Attendence");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddItem(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Inventory2.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Item");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddNewStudent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Student1.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add New Student");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddRoom(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Room1.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Room");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddStaff(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Staff1.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Staff");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddVisitor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("add_visitor.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Visitor");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAllItems(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Inventory1.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("View All Items");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }System.out.println("View All Items clicked.");
    }

    @FXML
    void btnAllRooms(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Room5.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("View All Rooms");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAllStaff(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Staff3.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("All Staff");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAllStu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Student4.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("View All Students");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnChangeRoom(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Room4.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Change/Exchange Room");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnCross(MouseEvent event) {
        System.out.println("Cross button clicked. Exiting the app.");
        System.exit(0);
    }

    @FXML
    void btnGenInvoice(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GenInvoice.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Generate Invoice");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnHome(MouseEvent event) {
        System.out.println("Home button clicked. Navigating to home screen.");
    }

    @FXML
    void btnInventory(ActionEvent event) {
        System.out.println("Inventory menu clicked.");
    }

    @FXML
    void btnInvoices(ActionEvent event) {
        System.out.println("Invoices menu clicked.");
    }

    @FXML
    void btnMess(ActionEvent event) {
        System.out.println("Mess clicked.");
    }

    @FXML
    void btnMessOF(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Mess1.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Mess ON/OFF");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnMessStatus(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Mess2.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Mess Status");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnRegVisits(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("visitor_register.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Visitor Register");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnRemoveItem(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Inventory3.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Remove Item");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnRemoveStaff(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Staff4.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Remove Staff");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnRemoveStu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Student7.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Remove Student");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnRemoveVisitor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("remove_visitor.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Remove Visitor");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnRoomAllo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Room3.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Room Allocation");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnRoomStatus(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Room2.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Room Status");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnRooms(ActionEvent event) {
        System.out.println("Rooms menu clicked.");
    }

    @FXML
    void btnSINOUT(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Student3.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Student IN/OUT");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnSettings(ActionEvent event) {
        System.out.println("Settings menu clicked.");
    }

    @FXML
    void btnStaff(ActionEvent event) {
        System.out.println("Staff menu clicked.");
    }

    @FXML
    void btnStaffLeave(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Staff2.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Staff Leave");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnStudents(ActionEvent event) {
        System.out.println("Students menu clicked.");
    }

    @FXML
    void btnViewStaff(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Staff5.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("View Staff");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnViewStu(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Invoice2.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("View Invoice");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnViewStudent(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Student5.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setTitle("View Student");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnViewVisitor(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view_visitor.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("View Visitor");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnVisitors(ActionEvent event) {
        System.out.println("Visitors menu clicked.");
    }

    @FXML
    void btnVisits(ActionEvent event) {
        System.out.println("Visits clicked.");
    }

    @FXML
    void btnAudit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Audit.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("AuditLog");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logout(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("test.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Main Page");
        stage.setResizable(false);
        stage.show();
    }
}
