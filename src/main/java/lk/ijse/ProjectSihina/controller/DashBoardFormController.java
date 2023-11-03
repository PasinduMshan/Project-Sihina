package lk.ijse.ProjectSihina.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashBoardFormController {

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

    @FXML
    void btnAttendanceOnAction(ActionEvent event) {

    }

    @FXML
    void btnClassOnAction(ActionEvent event) {

    }

    @FXML
    void btnDashboardOnAction(ActionEvent event) {

    }

    @FXML
    void btnExamsOnAction(ActionEvent event) {

    }

    @FXML
    void btnLogOutOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Login_Form.fxml"));

        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();

        stage.setScene(scene);

    }

    @FXML
    void btnPaymentOnAction(ActionEvent event) {

    }

    @FXML
    void btnScheduleOnAction(ActionEvent event) {

    }

    @FXML
    void btnStudentOnAction(ActionEvent event) {

    }

    @FXML
    void btnSubjectOnAction(ActionEvent event) {

    }

    @FXML
    void btnTeacherOnAction(ActionEvent event) {

    }

}