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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import lk.ijse.ProjectSihina.Enums.Months;
import lk.ijse.ProjectSihina.dto.ClassDto;
import lk.ijse.ProjectSihina.dto.PaymentDto;
import lk.ijse.ProjectSihina.dto.StudentDto;
import lk.ijse.ProjectSihina.dto.Tm.RegisterTm;
import lk.ijse.ProjectSihina.model.ClassModel;
import lk.ijse.ProjectSihina.model.PaymentModel;
import lk.ijse.ProjectSihina.model.RegisterStudentModel;

import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationPayForm implements Initializable {

    @FXML
    private JFXComboBox<String> cmbClass;

    @FXML
    private JFXComboBox<String> cmbMonth;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colClass;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colStu_Id;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblType;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<RegisterTm> tblPayment;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPayId;

    private StudentDto studentDto;

    private Object year;
    private Object month;
    private Object DATE;

    private String Subject;
    private String Type = "RegistrationFee";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        generatePayId();
        setDateAndTime();
        loadAllMonth();
        loadAllClass();
        loadAllRegistrations();
        lblType.setText(Type);
    }

    private void setCellValueFactory() {
        colID.setCellValueFactory(new PropertyValueFactory<>("PayId"));
        colStu_Id.setCellValueFactory(new PropertyValueFactory<>("StuId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("StuName"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("StuClass"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }

    private void loadAllRegistrations() {
        ObservableList<RegisterTm> obList = FXCollections.observableArrayList();

        try {
            List<PaymentDto> dtoList = RegisterStudentModel.getAllRegisterPayment(Type);

            for (PaymentDto dto : dtoList) {
                obList.add(new RegisterTm(
                        dto.getPayID(),
                        dto.getStuID(),
                        dto.getStuName(),
                        dto.getStuClass(),
                        dto.getDate(),
                        dto.getAmount()
                ));
            }
            tblPayment.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
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

    private void loadAllMonth() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        Months[] months = Months.values();
        for (Months month : months){
            obList.add(String.valueOf(month));
        }
        cmbMonth.setItems(obList);
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
        lblDate.setText(year + " : " + month + " : " + DATE);
    }

    private void updateTime() {
        LocalTime now = LocalTime.now();
        String formatted = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        lblTime.setText(formatted);
    }

    private void generateFieldInRegisterForm() {
        if (StudentInfoFormController.StuDto != null) {
            studentDto = StudentInfoFormController.StuDto;
            txtID.setText(studentDto.getID());
            txtName.setText(studentDto.getName());
            cmbClass.setValue(studentDto.getStu_Class());
            Subject = studentDto.getSubject();
        } else {
            new Alert(Alert.AlertType.ERROR,"Fill out the Student Information Form before registering").show();
        }
    }

    private void generatePayId() {
        try {
            String PayId = PaymentModel.generateNextPayId();
            txtPayId.setText(PayId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String PayId = txtPayId.getText();

        try {
            boolean isDeleted = PaymentModel.DeletePayment(PayId);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION,"Delete Success!!!").showAndWait();
                clearField();
                loadAllRegistrations();
                generatePayId();
            } else {
                new Alert(Alert.AlertType.ERROR,"Delete Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void clearField() throws SQLException {
        txtID.setText("");
        txtName.setText("");
        cmbClass.setValue("");
        txtAmount.setText("");
        cmbMonth.setValue("");
        lblType.setText(Type);
    }

    @FXML
    void btnPayOnAction(ActionEvent event) {
        generateFieldInRegisterForm();
        String PayId = txtPayId.getText();
        String StuId = txtID.getText();
        String StuName = txtName.getText();
        String StuClass = cmbClass.getValue();
        double PayAmount = Double.parseDouble(txtAmount.getText());
        String month = cmbMonth.getValue();
        LocalDate date = LocalDate.parse(lblDate.getText());
        LocalTime time = LocalTime.parse(lblTime.getText());
        String type = lblType.getText();
        String BarId = studentDto.getBarcodeID();

        PaymentDto PayDto = new PaymentDto(PayId,StuId,BarId,StuName,type,StuClass,month,Subject,PayAmount,date,time);

        try {
            boolean isRegisterStudent = RegisterStudentModel.SaveStudentRegisterAndPayment(studentDto, PayDto);
            if (isRegisterStudent) {
                new Alert(Alert.AlertType.INFORMATION, "Student Register Success!!").showAndWait();
                loadAllRegistrations();
                generatePayId();
            } else {
                new Alert(Alert.AlertType.ERROR,"Student Register Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }


    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String PayId = txtPayId.getText();
        try {
            PaymentDto dto = PaymentModel.SearchPaymontId(PayId);
            if (dto != null) {
                txtPayId.setText(dto.getPayID());
                txtID.setText(dto.getStuID());
                txtName.setText(dto.getStuName());
                cmbClass.setValue(dto.getStuClass());
                txtAmount.setText(String.valueOf(dto.getAmount()));
                cmbMonth.setValue(dto.getPayMonth());
                lblDate.setText(String.valueOf(dto.getDate()));
                lblTime.setText(String.valueOf(dto.getTime()));
                lblType.setText(Type);
            } else {
                new Alert(Alert.AlertType.ERROR,"Payment Not Found!!!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

}