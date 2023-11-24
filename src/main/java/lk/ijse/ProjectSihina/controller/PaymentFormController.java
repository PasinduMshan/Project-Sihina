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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.ProjectSihina.Enums.Months;
import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.ClassDto;
import lk.ijse.ProjectSihina.dto.PaymentDto;
import lk.ijse.ProjectSihina.dto.SubjectDto;
import lk.ijse.ProjectSihina.dto.Tm.PaymentTm;
import lk.ijse.ProjectSihina.model.ClassModel;
import lk.ijse.ProjectSihina.model.PaymentModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class PaymentFormController implements Initializable {
    @FXML
    private AnchorPane Node;

    @FXML
    private JFXComboBox<String> cmbClass;

    @FXML
    private JFXComboBox<String> cmbMonth;

    @FXML
    private JFXComboBox<String> cmbSubject;

    @FXML
    private JFXComboBox<String> cmbType;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colClass;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colMonth;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colSubject;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTime;

    @FXML
    private AnchorPane moveNode;

    @FXML
    private TableView<PaymentTm> tblPayment;

    @FXML
    private JFXTextField txtAmount;

    @FXML
    private JFXTextField txtAttendantCount;

    @FXML
    private JFXTextField txtBarcodeId;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtPayId;
    private Object year;
    private Object month;
    private Object DATE;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generatePayId();
        setDateAndTime();
        setCellValueFactory();
        loadAllPayment();
        loadAllType();
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

    private void loadAllType() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        obList.add("RegistrationFee");
        obList.add("MonthlyFee");
        cmbType.setItems(obList);
    }

    private void loadAllPayment() {
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();

        try {
            List<PaymentDto> dtoList = PaymentModel.getAllPayment();

            for (PaymentDto dto : dtoList) {
                obList.add(new PaymentTm(
                        dto.getPayID(),
                        dto.getStuName(),
                        dto.getStuClass(),
                        dto.getSubject(),
                        dto.getPayMonth(),
                        dto.getAmount()
                ));
            }
            tblPayment.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colID.setCellValueFactory(new PropertyValueFactory<>("Pay_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("Stu_Class"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("Stu_Subject"));
        colMonth.setCellValueFactory(new PropertyValueFactory<>("Pay_Month"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }

    private void generatePayId() {
        try {
            String PayId = PaymentModel.generateNextPayId();
            txtPayId.setText(PayId);
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
        lblDate.setText(year + "-" + month + "-" + DATE);
    }

    private void updateTime() {
        LocalTime now = LocalTime.now();
        String formatted = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        lblTime.setText(formatted);
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String payId = txtPayId.getText();
        String StuId = txtID.getText();
        String BarId = txtBarcodeId.getText();
        String StuName = txtName.getText();
        String type = cmbType.getValue();
        String StuClass = cmbClass.getValue();
        String month = cmbMonth.getValue();
        String subject = cmbSubject.getValue();
        double amount = Double.parseDouble(txtAmount.getText());

        String isAmount = String.valueOf(amount);
        boolean matches = Pattern.matches("[0-9.]+", isAmount);
        if (!matches) {
            new Alert(Alert.AlertType.ERROR,"Invalid Amount!!").show();
            return;
        }

        LocalDate date = LocalDate.parse(lblDate.getText());
        LocalTime time = LocalTime.parse(lblTime.getText());

        if (payId.isEmpty() || StuId.isEmpty() || BarId.isEmpty() || StuId.isEmpty() || type.isEmpty() || StuClass.isEmpty() || month.isEmpty() || subject.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Some Fields Are Empty!!!").showAndWait();
            return;
        }

        PaymentDto dto = new PaymentDto(payId, StuId, BarId, StuName, type, StuClass, month, subject, amount, date, time);

        try {
            boolean isAdd = PaymentModel.AddPayment(dto);

            if (isAdd) {
                new Alert(Alert.AlertType.INFORMATION,"Payment Success!!").showAndWait();
                loadAllPayment();
                clearField();
                generatePayId();
            } else {
                new Alert(Alert.AlertType.ERROR, "Payment Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String PayId = txtPayId.getText();

        if (PayId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"Id Field is Empty!!!").show();
            return;
        }

        try {
            boolean isDeleted = PaymentModel.DeletePayment(PayId);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION,"Delete Success!!!").showAndWait();
                clearField();
                loadAllPayment();
                generatePayId();
            } else {
                new Alert(Alert.AlertType.ERROR,"Delete Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {
        try {
            String payId = txtPayId.getText();
            String StuId = txtID.getText();
            String type = cmbType.getValue();
            String StuClass = cmbClass.getValue();
            String month = cmbMonth.getValue();
            String Amount = txtAmount.getText();

            HashMap hashMap = new HashMap();
            hashMap.put("Pay_id", payId);
            hashMap.put("Stu_id" , StuId);
            hashMap.put("Type", type);
            hashMap.put("Stu_Class", StuClass);
            hashMap.put("Pay_Month", month);
            hashMap.put("Amount", Amount);

            InputStream resourceAsStream = getClass().getResourceAsStream("../report/Payment.jrxml");
            JasperDesign load = JRXmlLoader.load(resourceAsStream);
            JasperReport compileReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    compileReport,
                    hashMap,
                    new JREmptyDataSource()
            );
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String PayId = txtPayId.getText();
        if (PayId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"Id Field is Empty!!!").show();
            return;
        }

        try {
            PaymentDto dto = PaymentModel.SearchPaymontId(PayId);
            if (dto != null) {
                txtPayId.setText(dto.getPayID());
                txtID.setText(dto.getStuID());
                txtBarcodeId.setText(dto.getBarId());
                txtName.setText(dto.getStuName());
                cmbType.setValue(dto.getType());
                cmbClass.setValue(dto.getStuClass());
                cmbMonth.setValue(dto.getPayMonth());
                cmbSubject.setValue(dto.getSubject());
                txtAmount.setText(String.valueOf(dto.getAmount()));
                lblDate.setText(String.valueOf(dto.getDate()));
                lblTime.setText(String.valueOf(dto.getTime()));
            } else {
                new Alert(Alert.AlertType.ERROR,"Payment Not Found!!!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String payId = txtPayId.getText();
        String StuId = txtID.getText();
        String BarId = txtBarcodeId.getText();
        String StuName = txtName.getText();
        String type = cmbType.getValue();
        String StuClass = cmbClass.getValue();
        String month = cmbMonth.getValue();
        String subject = cmbSubject.getValue();

        double amount = Double.parseDouble(txtAmount.getText());

        String isAmount = String.valueOf(amount);
        boolean matches = Pattern.matches("[0-9.]+", isAmount);
        if (!matches) {
            new Alert(Alert.AlertType.ERROR,"Invalid Amount!!").show();
            return;
        }

        LocalDate date = LocalDate.parse(lblDate.getText());
        LocalTime time = LocalTime.parse(lblTime.getText());

        if (payId.isEmpty() || StuId.isEmpty() || BarId.isEmpty() || StuName.isEmpty() || type.isEmpty() || StuClass.isEmpty() || month.isEmpty() || subject.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Some Fields Are Empty!!!").showAndWait();
            return;
        }

        PaymentDto dto = new PaymentDto(payId, StuId, BarId, StuName, type, StuClass, month, subject, amount, date, time);

        try {
            boolean isUpdated = PaymentModel.updatePayment(dto);
            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION,"Update Success!!!").showAndWait();
                clearField();
                loadAllPayment();
                generatePayId();
            } else {
                new Alert(Alert.AlertType.ERROR,"Update Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void SearchStudentDetailOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/search_Payments.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void clearField() {
        txtPayId.setText("");
        txtID.setText("");
        txtBarcodeId.setText("");
        txtName.setText("");
        cmbType.setValue("");
        cmbClass.setValue("");
        cmbMonth.setValue("");
        cmbSubject.setValue("");
        txtAmount.setText("");
        txtAttendantCount.setText("");
    }

    public void AttendantCountOnAction(ActionEvent actionEvent) {
        String Stu_id = txtID.getText();
        String Subject = cmbSubject.getValue();
        String month = cmbMonth.getValue();
        String Stu_class = cmbClass.getValue();
        if (Stu_id.isEmpty() || Subject.isEmpty() || month.isEmpty() || Stu_class.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Id , Subject, Month , Class  Fields Are Empty!!!").showAndWait();
            return;
        }

        try {
            String allAttendant = PaymentModel.getAllAttendant(Stu_id, Subject, month, Stu_class);
            txtAttendantCount.setText(allAttendant);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}