package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class SubjectFormController {

    @FXML
    private JFXCheckBox checkBoxAL;

    @FXML
    private JFXCheckBox checkBoxGrade10;

    @FXML
    private JFXCheckBox checkBoxGrade11;

    @FXML
    private JFXCheckBox checkBoxGrade6;

    @FXML
    private JFXCheckBox checkBoxGrade7;

    @FXML
    private JFXCheckBox checkBoxGrade8;

    @FXML
    private JFXCheckBox checkBoxGrade9;

    @FXML
    private JFXComboBox<?> cmbTeacherName;

    @FXML
    private TableColumn<?, ?> colClass;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colSubject;

    @FXML
    private TableColumn<?, ?> colTeacherName;

    @FXML
    private AnchorPane moveNode;

    @FXML
    private TableView<?> tblSubject;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtSubject;

    @FXML
    void btnAddOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

}