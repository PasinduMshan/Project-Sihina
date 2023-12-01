package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.ProjectSihina.Other.ArrowKeyPress;
import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.ClassDto;
import lk.ijse.ProjectSihina.dto.ExamDto;
import lk.ijse.ProjectSihina.dto.SubjectDto;
import lk.ijse.ProjectSihina.dto.Tm.ExamTm;
import lk.ijse.ProjectSihina.model.ClassModel;
import lk.ijse.ProjectSihina.model.ExamModel;
import lk.ijse.ProjectSihina.model.PaymentModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class ExamFormController implements Initializable {

    @FXML
    private JFXComboBox<String> cmbClass;

    @FXML
    private JFXComboBox<String> cmbSubject;

    @FXML
    private TableColumn<?, ?> colClass;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colSubject;

    @FXML
    private AnchorPane moveNode;

    @FXML
    private TableView<ExamTm> tblExam;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private JFXTextField txtEndTime;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtStartTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadAllExamToTable();
        loadAllClass();
        loadAllSubject();
        generateExam();
        tableListener();
        ArrowKeyPress.switchTextFieldOnArrowPressDown(txtID,txtDate);
        ArrowKeyPress.switchTextFieldOnArrowPressUP(txtDate,txtID);
        ArrowKeyPress.switchTextFieldOnArrowPressRight(txtDate,txtStartTime);
        ArrowKeyPress.switchTextFieldOnArrowPressRight(txtStartTime,txtEndTime);
        ArrowKeyPress.switchTextFieldOnArrowPressRIGHT(txtEndTime,txtDescription);
        ArrowKeyPress.switchTextFieldOnArrowPressLEFT(txtDescription,txtEndTime);
        ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtEndTime,txtStartTime);
        ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtStartTime,txtDate);
    }

    private void generateExam() {
        try {
            String id = ExamModel.generateExamId();
            txtID.setText(id);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadAllSubject() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<SubjectDto> SubList = PaymentModel.getAllSubject();

            for (SubjectDto dto : SubList) {
                obList.add(dto.getSubject());
            }
            cmbSubject.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadAllClass() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<ClassDto> nameList = ClassModel.getAllClass();

            for (ClassDto dto : nameList) {
                obList.add(dto.getClassName());
            }
            cmbClass.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadAllExamToTable() {
        ObservableList<ExamTm> obList = FXCollections.observableArrayList();

        try {
            List<ExamDto> dtoList = ExamModel.getAllExam();

            for (ExamDto dto : dtoList) {
                obList.add(new ExamTm(
                        dto.getExamId(),
                        dto.getDescription(),
                        dto.getClassName(),
                        dto.getSubject(),
                        String.valueOf(dto.getDate())
                ));
            }
            tblExam.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("ExamId"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("className"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("Subject"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String examId = txtID.getText();
        String className = cmbClass.getValue();
        String subject = cmbSubject.getValue();
        String description = txtDescription.getText();
        LocalDate date = LocalDate.parse(txtDate.getText());
        LocalTime Start_time = LocalTime.parse(txtStartTime.getText());
        LocalTime End_time = LocalTime.parse(txtEndTime.getText());

        ExamDto dto = new ExamDto(examId, className, subject, description, date, Start_time, End_time);

        try {
            boolean isSaved = ExamModel.AddExam(dto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION,"Exam Save Success!!!").showAndWait();
                loadAllExamToTable();
                clearField();
            } else {
                new Alert(Alert.AlertType.ERROR,"Exam Save Failed!!!").showAndWait();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }


    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String examId = txtID.getText();

        try {
            boolean isDeleted = ExamModel.deleteExam(examId);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION,"Delete Success!!!").showAndWait();
                loadAllExamToTable();
                clearField();
            } else {
                new Alert(Alert.AlertType.ERROR,"Delete Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) throws IOException {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("/report/Exam.jrxml");
            JasperDesign load = JRXmlLoader.load(resourceAsStream);
            JasperReport compileReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    compileReport,
                    null,
                    DbConnection.getInstance().getConnection()
            );
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String ExamId = txtID.getText();

        try {
            ExamDto dto = ExamModel.SearchExam(ExamId);
            if (dto != null) {
                txtID.setText(dto.getExamId());
                cmbClass.setValue(dto.getClassName());
                cmbSubject.setValue(dto.getSubject());
                txtDescription.setText(dto.getDescription());
                txtDate.setText(String.valueOf(dto.getDate()));
                txtStartTime.setText(String.valueOf(dto.getStartTime()));
                txtEndTime.setText(String.valueOf(dto.getEndTime()));
            } else {
                new Alert(Alert.AlertType.ERROR,"Exam Not Found!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String examId = txtID.getText();
        String className = cmbClass.getValue();
        String subject = cmbSubject.getValue();
        String description = txtDescription.getText();
        LocalDate date = LocalDate.parse(txtDate.getText());
        LocalTime Start_time = LocalTime.parse(txtStartTime.getText());
        LocalTime End_time = LocalTime.parse(txtEndTime.getText());

        ExamDto dto = new ExamDto(examId, className, subject, description, date, Start_time, End_time);

        try {
            boolean isUpdated = ExamModel.updateExam(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION,"Update Success!!!").showAndWait();
                loadAllExamToTable();
                clearField();
            } else {
                new Alert(Alert.AlertType.ERROR,"Update Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    private void clearField() {
        txtID.setText("");
        cmbClass.setValue("");
        cmbSubject.setValue("");
        txtDescription.setText("");
        txtDate.setText("");
        txtStartTime.setText("");
        txtEndTime.setText("");
        generateExam();
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        clearField();
    }
    private void tableListener(){
        tblExam.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, ExamTm, t1) -> {
                    try {
                        ExamDto dto = ExamModel.SearchExam(t1.getExamId());
                        if (dto != null) {
                            txtID.setText(dto.getExamId());
                            cmbClass.setValue(dto.getClassName());
                            cmbSubject.setValue(dto.getSubject());
                            txtDescription.setText(dto.getDescription());
                            txtDate.setText(String.valueOf(dto.getDate()));
                            txtStartTime.setText(String.valueOf(dto.getStartTime()));
                            txtEndTime.setText(String.valueOf(dto.getEndTime()));
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}