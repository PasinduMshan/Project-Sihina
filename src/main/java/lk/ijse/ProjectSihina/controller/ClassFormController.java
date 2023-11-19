package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.ProjectSihina.dto.ClassDto;
import lk.ijse.ProjectSihina.dto.Tm.ClassTm;
import lk.ijse.ProjectSihina.model.ClassModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ClassFormController implements Initializable {

    @FXML
    private AnchorPane moveNode;

    @FXML
    private JFXTextField txtClassName;

    @FXML
    private JFXTextField txtID;

    @FXML
    private TableColumn<?, ?> colClass;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colStuCount;

    @FXML
    private TableColumn<?, ?> colSubject;

    @FXML
    private TableView<ClassTm> tblClass;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       setCellValueFactory();
       loadAllClass();
    }

    private void loadAllClass() {
        ObservableList<ClassTm> obList = FXCollections.observableArrayList();

        try {
            List<ClassDto> dtoList = ClassModel.getAllClass();

            for (ClassDto dto : dtoList) {
                obList.add(new ClassTm(
                        dto.getClassID(),
                        dto.getClassName(),
                        dto.getStu_Count()
                ));
            }
            tblClass.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("Id"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("StuClass"));
        colStuCount.setCellValueFactory(new PropertyValueFactory<>("Student_count"));
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String ClassId = txtID.getText();
        String ClassName = txtClassName.getText();

        if (ClassId.isEmpty() || ClassName.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"Empty Field Have!!!").showAndWait();
            return;
        }

        ClassDto dto = new ClassDto(ClassId, ClassName);

        try {
            boolean isSavedClass = ClassModel.savaClass(dto);

            if (isSavedClass) {
                new Alert(Alert.AlertType.INFORMATION,"Class saved success!!!").showAndWait();
                clearFields();
                loadAllClass();
            } else {
                new Alert(Alert.AlertType.ERROR,"Save Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }


    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String ClassId = txtID.getText();

        if (ClassId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"ID Not Found!!!").showAndWait();
            return;
        }

        try {
            boolean isDeleteClass = ClassModel.isDeleteClass(ClassId);
            if (isDeleteClass) {
                new Alert(Alert.AlertType.INFORMATION,"Delete Class Success!!!").showAndWait();
                clearFields();
                loadAllClass();
            } else {
                new Alert(Alert.AlertType.ERROR,"Delete Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String ClassId = txtID.getText();
        String ClassName = txtClassName.getText();

        if (ClassId.isEmpty() || ClassName.isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Fields Empty!!!").showAndWait();
            return;
        }

        ClassDto dto = new ClassDto(ClassId, ClassName);

        try {
            boolean isUpdated = ClassModel.isUpdate(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION,"Update Success!!!").showAndWait();
                clearFields();
                loadAllClass();
            } else {
               new Alert(Alert.AlertType.ERROR,"Updated Failed!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    @FXML
    public void btnSearchOnAction(ActionEvent actionEvent) {
        String ClassId = txtID.getText();

        try {
            ClassDto dto = ClassModel.searchClass(ClassId);

            if (dto != null) {
                txtID.setText(dto.getClassID());
                txtClassName.setText(dto.getClassName());
            } else {
                new Alert(Alert.AlertType.ERROR,"Class Not Found!!!").showAndWait();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void txtIDSearchOnAction(ActionEvent event) {
        String ClassId = txtID.getText();

        try {
            ClassDto dto = ClassModel.searchClass(ClassId);

            if (dto != null) {
                txtID.setText(dto.getClassID());
                txtClassName.setText(dto.getClassName());
            } else {
                new Alert(Alert.AlertType.ERROR,"Class Not Found!!!").showAndWait();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearFields() {
        txtID.setText("");
        txtClassName.setText("");
    }

}
