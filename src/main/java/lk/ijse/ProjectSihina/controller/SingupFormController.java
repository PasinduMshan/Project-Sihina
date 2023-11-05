package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.ProjectSihina.dto.SignupDto;
import lk.ijse.ProjectSihina.model.SignUpModel;

import java.io.IOException;
import java.sql.SQLException;

public class SingupFormController {

    @FXML
    private AnchorPane rootNode;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtNIC;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXTextField txtUserName;

    private SignUpModel signUpModel = new SignUpModel();



    @FXML
    void btnRegisterOnAction(ActionEvent event)  {

        String userId = null;
        try {
            userId = signUpModel.generateNextUserId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String Email = txtEmail.getText();
        String NIC = txtNIC.getText();
        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        var dto = new SignupDto(userId, firstName, lastName, Email, NIC, userName, password);

        try {
            boolean isRegister = signUpModel.userRegister(dto);

            if (isRegister) {
                new Alert(Alert.AlertType.CONFIRMATION,"Now you registered!").show();
                clearFields();
            }
        } catch (SQLException e) {
           new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtNIC.setText("");
        txtUserName.setText("");
        txtPassword.setText("");
    }

    /*public String getUserId() {
        try {
          return signUpModel.generateNextUserId();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }*/

    @FXML
    void btnSignInOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Login_Form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();

        stage.setScene(scene);
    }

}
