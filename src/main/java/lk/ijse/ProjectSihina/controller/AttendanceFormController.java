package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.util.Duration;
import lk.ijse.ProjectSihina.Enums.Months;
import lk.ijse.ProjectSihina.dto.AttendantDto;
import lk.ijse.ProjectSihina.dto.ClassDto;
import lk.ijse.ProjectSihina.dto.SubjectDto;
import lk.ijse.ProjectSihina.dto.Tm.AttendantTm;
import lk.ijse.ProjectSihina.model.AttendantModel;
import lk.ijse.ProjectSihina.model.ClassModel;
import lk.ijse.ProjectSihina.model.PaymentModel;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;


public class AttendanceFormController implements Initializable {

    @FXML
    private JFXComboBox<String> cmbClass;

    @FXML
    private JFXComboBox<String> cmbMonth;

    @FXML
    private JFXComboBox<String> cmbSubject;

    @FXML
    private TableColumn<?, ?> colAttendance;

    @FXML
    private TableColumn<?, ?> colClass;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colMonth;

    @FXML
    private TableColumn<?, ?> colAttendantId;

    @FXML
    private TableColumn<?, ?> colStudentName;

    @FXML
    private AnchorPane moveNode;

    @FXML
    private TableView<AttendantTm> tblAttendance;

    @FXML
    private JFXTextField txtBarcodeId;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtStuId;

    @FXML
    private JFXTextField txtStudentName;

    @FXML
    private JFXTextField txtTime;

    @FXML
    private JFXTextField txtType;

    private Object year;
    private Object month;
    private Object DATE;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateAttId();
        setCellValueFactory();
        loadAllAttendance();
        setDateAndTime();
        loadAllClass();
        loadAllSubject();
        loadAllMonth();
    }
    private void loadAllMonth() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        Months[] months = Months.values();
        for (Months month : months){
            obList.add(String.valueOf(month));
        }
        cmbMonth.setItems(obList);
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

    private void setDateAndTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> updateTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
        Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        DATE = calendar.get(Calendar.DATE);
        txtDate.setText(year + " : " + month + " : " + DATE);
    }

    private void updateTime() {
        LocalTime now = LocalTime.now();
        String formatted = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        txtTime.setText(formatted);
    }

    private void loadAllAttendance() {
        ObservableList<AttendantTm> obList = FXCollections.observableArrayList();

        try {
            List<AttendantDto> dtoList = AttendantModel.getAllAttendance();

            for (AttendantDto dto : dtoList) {
                obList.add(new AttendantTm(
                        dto.getAtt_id(),
                        dto.getStudentName(),
                        dto.getClassName(),
                        dto.getDate(),
                        dto.getMonth(),
                        dto.getType()
                ));
            }
            tblAttendance.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colAttendantId.setCellValueFactory(new PropertyValueFactory<>("AttendantId"));
        colStudentName.setCellValueFactory(new PropertyValueFactory<>("StuName"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("StuClass"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colAttendance.setCellValueFactory(new PropertyValueFactory<>("AttendanceType"));
    }

    @FXML
    void btnAbsentOnAction(ActionEvent event) {
        String AttId = txtID.getText();
        String StuId = txtStuId.getText();
        String BarId = txtBarcodeId.getText();
        String StuName = txtStudentName.getText();
        String StuClass = cmbClass.getValue();
        String Month = cmbMonth.getValue();
        String Subject = cmbSubject.getValue();
        LocalDate date = LocalDate.parse(txtDate.getText());
        LocalTime time = LocalTime.parse(txtTime.getText());
        String type = "Absent";

        AttendantDto dto = new AttendantDto(AttId, StuId, BarId, StuName, StuClass, Month, Subject, date, time,type);

        try {
            boolean isAdd = AttendantModel.AddAttendant(dto);
            if (isAdd) {
                new Alert(Alert.AlertType.INFORMATION,"Add Success!!!").show();
                clearFields();
                generateAttId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Add Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String AttId = txtID.getText();

        try {
            boolean isDeleted = AttendantModel.deleteAttendant(AttId);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION,"Delete Success!!!").showAndWait();
                btnClearOnAction(event);
                generateAttId();
            } else {
                new Alert(Alert.AlertType.ERROR,"Delete Failed!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnPresentOnAction(ActionEvent event) {
        String AttId = txtID.getText();
        String StuId = txtStuId.getText();
        String BarId = txtBarcodeId.getText();
        String StuName = txtStudentName.getText();
        String StuClass = cmbClass.getValue();
        String Month = cmbMonth.getValue();
        String Subject = cmbSubject.getValue();
        LocalDate date = LocalDate.parse(txtDate.getText());
        LocalTime time = LocalTime.parse(txtTime.getText());
        String type = "Present";

        AttendantDto dto = new AttendantDto(AttId, StuId, BarId, StuName, StuClass, Month, Subject, date, time,type);

        try {
            boolean isAdd = AttendantModel.AddAttendant(dto);
            if (isAdd) {
                new Alert(Alert.AlertType.INFORMATION,"Add Success!!!").show();
                clearFields();
                generateAttId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Add Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void generateAttId(){
        try {
            String AttId = AttendantModel.generateNextAttId();
            txtID.setText(AttId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String AttId = txtID.getText();

        try {
            AttendantDto dto = AttendantModel.searchAttendant(AttId);

            if(dto != null) {
                txtID.setText(dto.getAtt_id());
                txtStuId.setText(dto.getStudentId());
                txtBarcodeId.setText(dto.getBarcodeId());
                txtStudentName.setText(dto.getStudentName());
                cmbClass.setValue(dto.getClassName());
                cmbSubject.setValue(dto.getSubject());
                cmbMonth.setValue(dto.getMonth());
                txtDate.setText(String.valueOf(dto.getDate()));
                txtTime.setText(String.valueOf(dto.getTime()));
                txtType.setText(dto.getType());
            } else {
                new Alert(Alert.AlertType.ERROR,"Search Attendance Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String AttId = txtID.getText();
        String StuId = txtStuId.getText();
        String BarId = txtBarcodeId.getText();
        String StuName = txtStudentName.getText();
        String StuClass = cmbClass.getValue();
        String Month = cmbMonth.getValue();
        String Subject = cmbSubject.getValue();
        LocalDate date = LocalDate.parse(txtDate.getText());
        LocalTime time = LocalTime.parse(txtTime.getText());
        String type = txtType.getText();

        AttendantDto dto = new AttendantDto(AttId, StuId, BarId, StuName, StuClass, Month, Subject, date, time,type);

        try {
            boolean isUpdated = AttendantModel.UpdateAttendent(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION,"Update Success!!!").showAndWait();
                btnClearOnAction(event);
                generateAttId();
            } else {
                new Alert(Alert.AlertType.ERROR,"Update Failed").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void btnClearOnAction(ActionEvent actionEvent) {
        txtID.setText("");
        txtStuId.setText("");
        txtBarcodeId.setText("");
        txtStudentName.setText("");
        cmbClass.setValue("");
        cmbSubject.setValue("");
        cmbMonth.setValue("");
        txtDate.setText("");
        txtTime.setText("");
        txtType.setText("");
        generateAttId();
    }

    private void clearFields() {
        txtStuId.setText("");
        txtBarcodeId.setText("");
        txtStudentName.setText("");
        txtType.setText("");
    }

}