package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import lk.ijse.ProjectSihina.model.ForgetPasswordModel;

import java.sql.SQLException;
import java.util.regex.Pattern;

public class ChangeCredentialsFormController {

    @FXML
    private AnchorPane Node;

    @FXML
    private JFXTextField txtConfirmPassword;

    @FXML
    private JFXTextField txtConfirmUserName;

    @FXML
    private JFXTextField txtPassword;

    @FXML
    private JFXTextField txtUserName;

    private String NIC;

    public void initialData(String NIC) {
        this.NIC = NIC;
    }

    private void clearField() {
        txtUserName.setText("");
        txtPassword.setText("");
        txtConfirmUserName.setText("");
        txtConfirmPassword.setText("");
    }

    @FXML
    void btnChangeCredentialsOnAction(ActionEvent event) {
        String userName = txtUserName.getText();
        boolean matches = Pattern.matches("[a-zA-Z0-9]{5,13}", userName);
        if (!matches) {
            new Alert(Alert.AlertType.ERROR,"Invalid UserName!!").show();
            return;
        }

        String password = txtPassword.getText();
        boolean matches1 = Pattern.matches("^[a-zA-Z0-9]{4,}$", password);
        if (!matches1) {
            new Alert(Alert.AlertType.ERROR,"Invalid Password!!  Only include(A-Z,a-z,0-9) & (at least 4 characters)").show();
            return;
        }

        String confirmUserName = txtConfirmUserName.getText();
        boolean matches2 = Pattern.matches("[a-zA-Z0-9]{5,13}", confirmUserName);
        if (!matches2) {
            new Alert(Alert.AlertType.ERROR,"Invalid UserName!!").show();
            return;
        }

        String confirmPassword = txtConfirmPassword.getText();
        boolean matches3 = Pattern.matches("[a-zA-Z0-9]{5,13}", confirmPassword);
        if (!matches3) {
            new Alert(Alert.AlertType.ERROR,"Invalid Password!!  Only include(A-Z,a-z,0-9) & (at least 4 characters)").show();
            return;
        }

        if (userName.equals(confirmUserName)) {
            if (password.equals(confirmPassword)) {
                try {
                    boolean isChange = ForgetPasswordModel.changeCredentials(userName, password, NIC);
                    if (isChange) {
                        new Alert(Alert.AlertType.INFORMATION,"Change Success!!").showAndWait();
                        clearField();
                    } else {
                        new Alert(Alert.AlertType.ERROR,"Change Failed!!").show();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }
            } else {
                new Alert(Alert.AlertType.ERROR,"Not Match Password!!").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR,"Not Match UserName!!").show();
        }
    }

}