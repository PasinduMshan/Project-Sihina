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
import lk.ijse.ProjectSihina.dto.UserDto;
import lk.ijse.ProjectSihina.model.LoginModel;

import java.io.IOException;
import java.sql.SQLException;

public class LoginFormController {
    @FXML
    private AnchorPane rootNode;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXTextField txtUserName;
    
    private LoginModel loginModel = new LoginModel();

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {

        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        try {
            boolean isLoginSuccess = loginModel.checkCredentials(userName , password);
            if (isLoginSuccess) {
                showInfoAlert("Login Successful !!!", "Welcome, " + userName);

                Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/DashBoard_Form.fxml"));

                Scene scene = new Scene(rootNode);
                Stage stage = (Stage) this.rootNode.getScene().getWindow();

                stage.setScene(scene);

            } else {

               showErrorAlert("Login Failed!!!","Invalid User Name or Password ");
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void btnSignUpOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Singup_Form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();

        stage.setScene(scene);
    }
}