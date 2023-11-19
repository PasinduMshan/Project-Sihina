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
import lk.ijse.ProjectSihina.dto.PaymentDto;
import lk.ijse.ProjectSihina.dto.StudentDto;
import lk.ijse.ProjectSihina.dto.Tm.PaymentTm;
import lk.ijse.ProjectSihina.model.PaymentModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class SearchPaymentsController implements Initializable {

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<?, ?> colPayId;

    @FXML
    private TableColumn<?, ?> colSubject;

    @FXML
    private TableView<PaymentTm> tblPayDetail;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtMonth;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtSubject;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colPayId.setCellValueFactory(new PropertyValueFactory<>("Pay_id"));
        colSubject.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }

    @FXML
    void RefreshOnAction(ActionEvent event) {
        tblPayDetail.setItems(FXCollections.observableArrayList());
        txtID.setText("");
        txtName.setText("");
        txtSubject.setText("");
        txtMonth.setText("");
    }

    @FXML
    void SearchMonthDeatilOnAction(ActionEvent event) {
        String id = txtID.getText();
        String month = txtMonth.getText();
        ObservableList<PaymentTm> obList = FXCollections.observableArrayList();

        try {
            List<PaymentDto> dtoList = PaymentModel.searchStuPays(id,month);

            for (PaymentDto PayDto : dtoList) {
                obList.add(new PaymentTm(
                        PayDto.getPayID(),
                        PayDto.getSubject(),
                        PayDto.getAmount()
                ));
            }
            tblPayDetail.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void SearchOnAction(ActionEvent event) {
        SearchMonthDeatilOnAction(event);
    }

    public void StudentIdSearchOnAction(ActionEvent actionEvent) {
        String id = txtID.getText();

        try {
            StudentDto dto = PaymentModel.searchStu(id);

            if (dto != null) {
                txtID.setText(dto.getID());
                txtName.setText(dto.getName());
                txtSubject.setText(dto.getSubject());
            } else {
                new Alert(Alert.AlertType.INFORMATION,"Student Not Found!!!").showAndWait();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

}