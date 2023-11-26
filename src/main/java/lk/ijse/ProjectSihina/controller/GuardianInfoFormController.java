package lk.ijse.ProjectSihina.controller;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.ProjectSihina.dto.GuardianDto;
import lk.ijse.ProjectSihina.dto.StudentDto;
import lk.ijse.ProjectSihina.dto.Tm.GuardianTm;
import lk.ijse.ProjectSihina.model.GuardianModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class GuardianInfoFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colGuardianId;

    @FXML
    private TableColumn<?, ?> colGuardianName;

    @FXML
    private TableColumn<?, ?> colOccupation;

    @FXML
    private TableColumn<?, ?> colStuId;

    @FXML
    private AnchorPane moveNode;

    @FXML
    private TableView<GuardianTm> tblGuardian;

    @FXML
    private JFXTextField txtContactNo;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtGuardianID;

    @FXML
    private JFXTextField txtGuardianName;

    @FXML
    private JFXTextField txtOccupation;

    @FXML
    private JFXTextField txtStudentId;

    private StudentDto studentDto;
    private File selectedImageFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateGuardianID();
        setCellValueFactory();
        loadAllGuardian();

    }

    private void setCellValueFactory() {
        colGuardianId.setCellValueFactory(new PropertyValueFactory<>("GuardId"));
        colGuardianName.setCellValueFactory(new PropertyValueFactory<>("GuardName"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colOccupation.setCellValueFactory(new PropertyValueFactory<>("occupation"));
        colStuId.setCellValueFactory(new PropertyValueFactory<>("StuId"));
    }

    private void loadAllGuardian() {
        ObservableList<GuardianTm> obList = FXCollections.observableArrayList();

        try {
            List<GuardianDto> dtoList = GuardianModel.getAllGuardian();

            for (GuardianDto dto : dtoList) {
                obList.add(new GuardianTm(
                        dto.getGuardId(),
                        dto.getName(),
                        dto.getContact(),
                        dto.getEmail(),
                        dto.getOccupation(),
                        dto.getStuId()
                ));
            }
            tblGuardian.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    public void initialData(StudentDto dto, File selectedImageFile) {
        this.studentDto = dto;
        this.selectedImageFile = selectedImageFile;
    }

    private void generateGuardianID() {
        try {
            String guardianId = GuardianModel.getGuardianId();
            txtGuardianID.setText(guardianId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) throws IOException {
        String GuardId = txtGuardianID.getText();
        String Name = txtGuardianName.getText();
        String contactNo = txtContactNo.getText();
        String email = txtEmail.getText();
        String occupation = txtOccupation.getText();
        String StuId = txtStudentId.getText();

        if (StuId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"Sutudent Id Field is Empty!!!").show();
            return;
        }

        boolean isValidate = validateStudentDetail(Name, contactNo, email, occupation);
        if (isValidate) {
            GuardianDto dto = new GuardianDto(GuardId, Name, contactNo, email, occupation, StuId);
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/Guardian_Info_Form.fxml"));
            Parent rootNode = loader.load();
            RegistrationPayForm registrationPayForm = loader.getController();
            registrationPayForm.initialData(studentDto, selectedImageFile,dto);
            Scene scene = new Scene(rootNode);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        }
    }

    private boolean validateStudentDetail(String name, String ContactNo, String Email, String Occupation) {

        boolean matches = Pattern.matches("[A-Za-z\\s.]+", name);
        if (!matches) {
            new Alert(Alert.AlertType.ERROR,"Invalid Name!!").show();
            return false;
        }

        boolean matches1 = Pattern.matches("^(?:7|0|(?:\\+94))[0-9]{9,10}$", ContactNo);
        if (!matches1) {
            new Alert(Alert.AlertType.ERROR,"Invalid Contact!!").show();
            return false;
        }


        boolean matches2 = Pattern.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Email);
        if (!matches2) {
            new Alert(Alert.AlertType.ERROR,"Invalid Email!!").show();
            return false;
        }

        boolean matches3 = Pattern.matches("[A-Za-z\\s]+", Occupation);
        if (!matches3) {
            new Alert(Alert.AlertType.ERROR,"Invalid occupation!!").show();
            return false;
        }

        return true;
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String ID = txtGuardianID.getText();
        if (ID.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Guardian Id is empty!!!").show();
            return;
        }

        try {
            boolean isDelete = GuardianModel.deleteGuard(ID);
            if (isDelete) {
                new Alert(Alert.AlertType.INFORMATION,"Guardian Delete Success!!!").showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR,"Guardian Delete Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnGuardianContactSearchOnAction(ActionEvent event) {
        String contact = txtContactNo.getText();
        if (contact.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Contact No is empty!!!").show();
            return;
        }

        try {
            GuardianDto dto = GuardianModel.SearchGuardianFromContact(contact);

            if (dto != null) {
                txtGuardianID.setText(dto.getGuardId());
                txtGuardianName.setText(dto.getName());
                txtContactNo.setText(dto.getContact());
                txtEmail.setText(dto.getEmail());
                txtOccupation.setText(dto.getOccupation());
                txtStudentId.setText(dto.getStuId());
            } else {
                new Alert(Alert.AlertType.ERROR,"Guardian Not Found!!!").showAndWait();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnGuardianIDSearchOnAction(ActionEvent event) {
        String id = txtGuardianID.getText();
        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Guardian Id is empty!!!").show();
            return;
        }

        try {
            GuardianDto dto = GuardianModel.SearchGuardianFromId(id);

            if (dto != null) {
                txtGuardianID.setText(dto.getGuardId());
                txtGuardianName.setText(dto.getName());
                txtContactNo.setText(dto.getContact());
                txtEmail.setText(dto.getEmail());
                txtOccupation.setText(dto.getOccupation());
                txtStudentId.setText(dto.getStuId());
            } else {
                new Alert(Alert.AlertType.ERROR,"Guardian Not Found!!!").showAndWait();
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
        String id = txtGuardianID.getText();
        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Guardian Id is empty!!!").show();
            return;
        }

        try {
            GuardianDto dto = GuardianModel.SearchGuardianFromId(id);

            if (dto != null) {
                txtGuardianID.setText(dto.getGuardId());
                txtGuardianName.setText(dto.getName());
                txtContactNo.setText(dto.getContact());
                txtEmail.setText(dto.getEmail());
                txtOccupation.setText(dto.getOccupation());
                txtStudentId.setText(dto.getStuId());
            } else {
                new Alert(Alert.AlertType.ERROR,"Guardian Not Found!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnStudentGuardianSearchOnAction(ActionEvent event) {
        String StuId = txtStudentId.getText();
        if (StuId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Student Id is empty!!!").show();
            return;
        }

        try {
            GuardianDto dto = GuardianModel.SearchGuardianFromStuId(StuId);

            if (dto != null) {
                txtGuardianID.setText(dto.getGuardId());
                txtGuardianName.setText(dto.getName());
                txtContactNo.setText(dto.getContact());
                txtEmail.setText(dto.getEmail());
                txtOccupation.setText(dto.getOccupation());
                txtStudentId.setText(dto.getStuId());
            } else {
                new Alert(Alert.AlertType.ERROR,"Guardian Not Found!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String GuardId = txtGuardianID.getText();
        String Name = txtGuardianName.getText();
        String contactNo = txtContactNo.getText();
        String email = txtEmail.getText();
        String occupation = txtOccupation.getText();
        String StuId = txtStudentId.getText();

        if (StuId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"Sutudent Id Field is Empty!!!").show();
            return;
        }

        boolean isValidate = validateStudentDetail(Name, contactNo, email, occupation);
        if (isValidate) {
            GuardianDto dto = new GuardianDto(GuardId, Name, contactNo, email, occupation, StuId);
            try {
                boolean updateGuardian = GuardianModel.updateGuardian(dto);
                if (updateGuardian) {
                    new Alert(Alert.AlertType.INFORMATION,"Update Success!!!").showAndWait();
                } else {
                    new Alert(Alert.AlertType.ERROR,"Update Failed!!!").showAndWait();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }
}