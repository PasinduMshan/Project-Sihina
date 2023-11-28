package lk.ijse.ProjectSihina.controller;

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
import lk.ijse.ProjectSihina.dto.UserDto;
import lk.ijse.ProjectSihina.model.SignUpModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SingupFormController implements Initializable {

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
    void btnRegisterOnAction(ActionEvent event) throws IOException {

        String userId = null;
        try {
            userId = signUpModel.generateNextUserId();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String Email = txtEmail.getText();
        String NIC = txtNIC.getText();
        String userName = txtUserName.getText();
        String password = txtPassword.getText();

        boolean isValidateUserDetail = validateUserDetail(firstName, lastName, Email, NIC, userName, password);

        if (isValidateUserDetail) {
            var dto = new UserDto(userId, firstName, lastName, Email, NIC, userName, password);

            try {
                boolean isRegister = signUpModel.userRegister(dto);

                if (isRegister) {
                    clearFields();
                    showInfoAlert("Registration Successful !!!");

                    Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Login_Form.fxml"));

                    Scene scene = new Scene(rootNode);
                    Stage stage = (Stage) this.rootNode.getScene().getWindow();

                    stage.setScene(scene);
                } else {
                    new Alert(Alert.AlertType.ERROR, "Register Not Success!!!").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validateUserDetail(String firstName, String lastName, String email, String NIC, String userName, String password) {

        boolean matches = Pattern.matches("[A-Za-z]+", firstName);
        if (!matches) {
            new Alert(Alert.AlertType.ERROR,"Invalid First Name!!").show();
            return false;
        }

        boolean matches1 = Pattern.matches("[A-Za-z]+", lastName);
        if (!matches1) {
            new Alert(Alert.AlertType.ERROR,"Invalid Last Name!!").show();
            return false;
        }

        boolean matches2 = Pattern.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", email);
        if (!matches2) {
            new Alert(Alert.AlertType.ERROR,"Invalid Email!!").show();
            return false;
        }

        boolean matches3 = Pattern.matches("^(?:19|20)?\\d{2}[0-9]{10}|[0-9]{9}[x|X|v|V]$", NIC);
        if (!matches3) {
            new Alert(Alert.AlertType.ERROR,"Invalid NIC!!").show();
            return false;
        }

        boolean matches4 = Pattern.matches("[a-zA-Z0-9]{5,13}", userName);
        if (!matches4) {
            new Alert(Alert.AlertType.ERROR,"Invalid UserName!!").show();
            return false;
        }

        boolean matches5 = Pattern.matches("^[a-zA-Z0-9]{4,}$", password);
        if (!matches5) {
            new Alert(Alert.AlertType.ERROR,"Invalid Password!!  Only include(A-Z,a-z,0-9) & (at least 4 characters)").show();
            return false;
        }
        return true;
    }

    private void clearFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtNIC.setText("");
        txtUserName.setText("");
        txtPassword.setText("");
    }

    @FXML
    void btnSignInOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Login_Form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();

        stage.setScene(scene);
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void switchTextFieldOnArrowPress(JFXTextField textField, JFXTextField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case DOWN:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }
    public static void switchTextFieldOnArrowPressLeftRight(JFXTextField textField, JFXTextField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case RIGHT:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
                case LEFT:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }
    public static void switchTextFieldOnArrowPressUP(JFXTextField textField, JFXTextField nextTextField) {
        textField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP:
                    nextTextField.requestFocus();
                    event.consume();
                    break;
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switchTextFieldOnArrowPress(txtFirstName,txtLastName);
        switchTextFieldOnArrowPress(txtLastName,txtEmail);
        switchTextFieldOnArrowPress(txtEmail,txtNIC);
        switchTextFieldOnArrowPress(txtNIC,txtUserName);
        switchTextFieldOnArrowPress(txtUserName,txtPassword);

        switchTextFieldOnArrowPressUP(txtPassword,txtUserName);
        switchTextFieldOnArrowPressUP(txtUserName,txtNIC);
        switchTextFieldOnArrowPressUP(txtNIC,txtEmail);
        switchTextFieldOnArrowPressUP(txtEmail,txtLastName);
        switchTextFieldOnArrowPressUP(txtLastName,txtFirstName);

        switchTextFieldOnArrowPressLeftRight(txtLastName,txtFirstName);
        switchTextFieldOnArrowPressLeftRight(txtFirstName,txtLastName);
    }
}
