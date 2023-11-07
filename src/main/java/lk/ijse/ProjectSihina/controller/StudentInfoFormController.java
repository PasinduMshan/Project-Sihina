package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentInfoFormController {

    @FXML
    private AnchorPane rootNode;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtBarcodeID;

    @FXML
    private JFXComboBox<?> txtClass;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtDateOfBirth;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXComboBox<?> txtGender;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtNameWithInitials;

    @FXML
    private JFXTextField txtSubject;

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/DashBoard_Form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();

        stage.setScene(scene);
    }

    @FXML
    void btnBrowseOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

}