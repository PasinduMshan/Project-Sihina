package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class AttendanceFormController {

    @FXML
    private JFXComboBox<?> cmbClass;

    @FXML
    private JFXComboBox<?> cmbMonth;

    @FXML
    private TableColumn<?, ?> colAttendance;

    @FXML
    private TableColumn<?, ?> colBarcodeId;

    @FXML
    private TableColumn<?, ?> colClass;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colMonth;

    @FXML
    private TableColumn<?, ?> colStudentName;

    @FXML
    private AnchorPane moveNode;

    @FXML
    private TableView<?> tblAttendance;

    @FXML
    private JFXTextField txtBarcodeId;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtEndTime;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtStartTime;

    @FXML
    private JFXTextField txtStudentName;

    @FXML
    void btnAbsentOnAction(ActionEvent event) {

    }

    @FXML
    void btnAddOnAction(ActionEvent event) {

    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {

    }

    @FXML
    void btnPresentOnAction(ActionEvent event) {

    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

}