package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class StudentInfoFormController {

    @FXML
    private TableColumn<?, ?> colBarcodeId;

    @FXML
    private TableColumn<?, ?> colClass;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private Pane paneStudentImage;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<?> tblStudent;

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
    void btnBackOnAction(ActionEvent event) {

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