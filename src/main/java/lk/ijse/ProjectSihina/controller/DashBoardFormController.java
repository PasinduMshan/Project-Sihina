package lk.ijse.ProjectSihina.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import lk.ijse.ProjectSihina.User.UserConnection;
import lk.ijse.ProjectSihina.dto.DashBordScheduleDto;
import lk.ijse.ProjectSihina.dto.Tm.DashBoardScheduleTm;
import lk.ijse.ProjectSihina.model.DashBordModel;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

public class DashBoardFormController {
    @FXML
    private AnchorPane moveNode;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblStudentCount;

    @FXML
    private Label lblSubjectsCount;

    @FXML
    private Label lblTeachersCount;

    @FXML
    private Label lblTime;

    @FXML
    private Label lblTodayIncome;

    @FXML
    private Label lblUserName;
    private Object year;
    private Object month;
    private Object DATE;

    @FXML
    private TableColumn<?, ?> colEndTime;

    @FXML
    private TableColumn<?, ?> colGrade;

    @FXML
    private TableColumn<?, ?> colStartTime;

    @FXML
    private TableColumn<?, ?> colSubject;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private TableView<DashBoardScheduleTm> tblSchedule;

    public void initialize() {
        setDateandTime();
        setUserName();
        setTodayIncome();
        getStudentCount();
        getTeacherCount();
        getSubjectCount();
        setCellValueFactory();
        loadAllDetail();
    }

    private void setCellValueFactory() {
        colStartTime.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        colEndTime.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        colGrade.setCellValueFactory(new PropertyValueFactory<>("Stu_Class"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("Subject"));
        colType.setCellValueFactory(new PropertyValueFactory<>("Type"));
    }

    private void loadAllDetail() {
        ObservableList<DashBoardScheduleTm> obList = FXCollections.observableArrayList();

        try {
            LocalDate date = LocalDate.parse(lblDate.getText());
            List<DashBordScheduleDto> dtoList = DashBordModel.getTodaySchedule(date);

            for (DashBordScheduleDto dto : dtoList) {
                obList.add(new DashBoardScheduleTm(
                        dto.getStartTime(),
                        dto.getEndTime(),
                        dto.getStu_Class(),
                        dto.getSubject(),
                        dto.getType()
                ));
            }
            List<DashBordScheduleDto> todayExams = DashBordModel.getTodayExams(date);

            for (DashBordScheduleDto dtoEx : todayExams) {
                obList.add(new DashBoardScheduleTm(
                        dtoEx.getStartTime(),
                        dtoEx.getEndTime(),
                        dtoEx.getStu_Class(),
                        dtoEx.getSubject(),
                        dtoEx.getType()
                ));
            }

            tblSchedule.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    private void getSubjectCount() {
        try {
            String subjectCount = DashBordModel.getSubjectCount();
            lblSubjectsCount.setText(subjectCount);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void getTeacherCount() {
        try {
            String teacherCount = DashBordModel.getTeacherCount();
            lblTeachersCount.setText(teacherCount);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void getStudentCount() {
        try {
            String studentCount = DashBordModel.getStudentCount();
            lblStudentCount.setText(studentCount);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setTodayIncome() {
        LocalDate date = LocalDate.parse(lblDate.getText());
        try {
            String sumOfAmount = DashBordModel.getSumOfAmount(date);
            lblTodayIncome.setText(sumOfAmount);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setUserName() {
        String userName = UserConnection.getInstance().getUserName();
        lblUserName.setText(userName);
    }

    private void updateTime() {
        LocalTime now = LocalTime.now();
        String formattedTime = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        lblTime.setText(formattedTime);
    }

    private void setDateandTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> updateTime()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        lblDate.setText(String.valueOf(LocalDate.now()));
    }
    @FXML
    void btnAttendanceOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Attendence_Form.fxml"));
        this.moveNode.getChildren().clear();
        this.moveNode.getChildren().add(rootNode);
    }

    @FXML
    void btnClassOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Class_Form.fxml"));
        this.moveNode.getChildren().clear();
        this.moveNode.getChildren().add(rootNode);
    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/DashBoard_Form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void btnExamsOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Exam_Form.fxml"));
        this.moveNode.getChildren().clear();
        this.moveNode.getChildren().add(rootNode);
    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Login_Form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);

    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Payment_Form.fxml"));
        this.moveNode.getChildren().clear();
        this.moveNode.getChildren().add(rootNode);
    }

    @FXML
    void btnScheduleOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Schedule.fxml"));
        this.moveNode.getChildren().clear();
        this.moveNode.getChildren().add(rootNode);
    }

    @FXML
    void btnStudentOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Student_Info_Form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void btnSubjectOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Subjects_Form.fxml"));
        this.moveNode.getChildren().clear();
        this.moveNode.getChildren().add(rootNode);
    }

    @FXML
    void btnTeacherOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Teacher_Form.fxml"));
        this.moveNode.getChildren().clear();
        this.moveNode.getChildren().add(rootNode);
    }

    public void btnAboutOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/About_Us_Form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void btnEmailOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Email_Form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void btnUserSettingOnAction(ActionEvent actionEvent) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/User_Setting.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}