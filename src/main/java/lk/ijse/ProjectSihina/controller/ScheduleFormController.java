package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.ProjectSihina.Other.ArrowKeyPress;
import lk.ijse.ProjectSihina.Other.Days;
import lk.ijse.ProjectSihina.dto.ClassDto;
import lk.ijse.ProjectSihina.dto.ScheduleDto;
import lk.ijse.ProjectSihina.dto.SubjectDto;
import lk.ijse.ProjectSihina.dto.TeacherDto;
import lk.ijse.ProjectSihina.dto.Tm.ScheduleTm;
import lk.ijse.ProjectSihina.model.ClassModel;
import lk.ijse.ProjectSihina.model.ScheduleModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ScheduleFormController implements Initializable {

    @FXML
    private JFXComboBox<String> cmbClass;

    @FXML
    private JFXComboBox<String> cmbDay;

    @FXML
    private JFXComboBox<String> cmbSubject;

    @FXML
    private JFXComboBox<String> cmbTeacherName;

    @FXML
    private TableColumn<?, ?> colClass;

    @FXML
    private TableColumn<?, ?> colDay;

    @FXML
    private TableColumn<?, ?> colSubject;

    @FXML
    private TableColumn<?, ?> colTeacherName;

    @FXML
    private TableColumn<?, ?> colStartTime;

    @FXML
    private TableColumn<?, ?> colEndTime;

    @FXML
    private AnchorPane moveNode;

    @FXML
    private TableView<ScheduleTm> tblSchedule;

    @FXML
    private JFXTextField txtEndTime;

    @FXML
    private JFXTextField txtStartTime;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadAllSchedule();
        loadAllClass();
        loadAllSubject();
        loadAllTeacher();
        loadAllDay();
        ArrowKeyPress.switchTextFieldOnArrowPressRight(txtStartTime,txtEndTime);
        ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtEndTime,txtStartTime);
    }

    private void loadAllDay() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        Days[] days = Days.values();
        for (Days day : days){
            obList.add(String.valueOf(day));
        }
        cmbDay.setItems(obList);
    }

    private void loadAllTeacher() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<TeacherDto> dtoList = ScheduleModel.getAllTeacher();

            for (TeacherDto dto : dtoList) {
                obList.add(dto.getName());
            }
            cmbTeacherName.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadAllSubject() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        try {
            List<SubjectDto> SubList = ScheduleModel.getAllSubject();

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

    private void loadAllSchedule() {
        ObservableList<ScheduleTm> obList = FXCollections.observableArrayList();
        try {
            List<ScheduleDto> dtoList = ScheduleModel.getAllSchedule();

            for (ScheduleDto dto : dtoList) {
                obList.add(new ScheduleTm(
                        dto.getStu_Class(),
                        dto.getSubject(),
                        dto.getTeacher(),
                        dto.getDay(),
                        dto.getStartTime(),
                        dto.getEndTime()
                ));
            }
            tblSchedule.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colClass.setCellValueFactory(new PropertyValueFactory<>("Stu_Class"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("Subject"));
        colTeacherName.setCellValueFactory(new PropertyValueFactory<>("Teacher"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("Day"));
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String classValue = cmbClass.getValue();
        String Subject = cmbSubject.getValue();
        String teacher = cmbTeacherName.getValue();
        String days = cmbDay.getValue();

        LocalTime StartTime = LocalTime.parse(txtStartTime.getText());
        String startTime = String.valueOf(StartTime);
        boolean matches = Pattern.matches("[0-9:]+", startTime);
        if (!matches) {
            new Alert(Alert.AlertType.ERROR,"Invalid Time!!").show();
            return;
        }

        LocalTime EndTime = LocalTime.parse(txtEndTime.getText());
        String endTime = String.valueOf(StartTime);
        boolean matches1 = Pattern.matches("[0-9:]+", endTime);
        if (!matches1) {
            new Alert(Alert.AlertType.ERROR,"Invalid Time!!").show();
            return;
        }

        if (classValue.isEmpty() || Subject.isEmpty() || teacher.isEmpty() || days.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Some Field are Empty!!").show();
            return;
        }


        ScheduleDto dto = new ScheduleDto(classValue, Subject, teacher, days, StartTime, EndTime);

        try {
            boolean isAdd = ScheduleModel.AddSchedule(dto);
            if (isAdd) {
                new Alert(Alert.AlertType.INFORMATION," Schedule Add Success!!!").showAndWait();
                clearField();
                loadAllSchedule();
            } else {
                new Alert(Alert.AlertType.ERROR,"Schedule Add Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String classValue = cmbClass.getValue();
        String Subject = cmbSubject.getValue();
        String teacher = cmbTeacherName.getValue();
        String days = cmbDay.getValue();
        LocalTime StartTime = LocalTime.parse(txtStartTime.getText());
        LocalTime EndTime = LocalTime.parse(txtEndTime.getText());

        ScheduleDto dto = new ScheduleDto(classValue, Subject, teacher, days, StartTime, EndTime);

        try {
            boolean isDelete = ScheduleModel.DeleteShedule(dto);
            if (isDelete) {
                new Alert(Alert.AlertType.INFORMATION, "Delete Success!!!").showAndWait();
                clearField();
                loadAllSchedule();
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

    private void clearField() {
        cmbDay.setValue("");
        cmbClass.setValue("");
        cmbSubject.setValue("");
        cmbTeacherName.setValue("");
        txtStartTime.setText("");
        txtEndTime.setText("");
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        clearField();
    }
}