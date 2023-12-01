package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.ProjectSihina.Other.ArrowKeyPress;
import lk.ijse.ProjectSihina.User.UserConnection;
import lk.ijse.ProjectSihina.model.LoginModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginFormController implements Initializable {
    @FXML
    private AnchorPane rootNode;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXPasswordField txtPasswordField;

    private LoginModel loginModel = new LoginModel();

    @FXML
    void btnLoginOnAction(ActionEvent event) throws IOException {

        String userName = txtUserName.getText();
        String password = txtPasswordField.getText();

        if (userName.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"Field Not Found!!!").showAndWait();
            return;
        }

        try {
            boolean isLoginSuccess = loginModel.checkCredentials(userName , password);
            if (isLoginSuccess) {
                showInfoAlert("Login Successful !!!", "Welcome, " + userName);

                UserConnection.getInstance().setUserName(userName);
                UserConnection.getInstance().setPassword(password);

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

    @FXML
    void btnPasswordOnAction(ActionEvent event) throws IOException {
        btnLoginOnAction(event);
    }

    @FXML
    void userNameOnAction(ActionEvent event) {

    }

    @FXML
    void btnForgotPasswordOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Forget_Password_Form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrowKeyPress.switchTextFieldOnArrowPressDOWN(txtUserName,txtPasswordField);
        ArrowKeyPress.switchTextFieldOnArrowPressup(txtPasswordField,txtUserName);
    }

}