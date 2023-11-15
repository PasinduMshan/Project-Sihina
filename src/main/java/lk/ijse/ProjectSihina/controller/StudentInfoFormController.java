package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import lk.ijse.ProjectSihina.dto.ClassDto;
import lk.ijse.ProjectSihina.dto.StudentDto;
import lk.ijse.ProjectSihina.model.ClassModel;
import lk.ijse.ProjectSihina.model.StudentModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;


public class StudentInfoFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colBarcodeId;

    @FXML
    private TableColumn<?, ?> colClass;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private Pane paneStudentImage;

    @FXML
    private ImageView imageStudent;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<?> tblStudent;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtBarcodeID;

    @FXML
    private JFXComboBox<String> cmbClass;

    @FXML
    private JFXTextField txtContact;

    @FXML
    private JFXTextField txtDateOfBirth;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXComboBox<String> cmbGender;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtNameWithInitials;

    @FXML
    private JFXTextField txtSubject;

    private File selectedImageFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       loadGender();
       loadAllClass();
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
            throw new RuntimeException(e);
        }
    }

    private void loadGender() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        String male = "MALE";
        String female = "FEMALE";
        obList.add(male);
        obList.add(female);
        cmbGender.setItems(obList);
    }

    @FXML
    void btnBackOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(getClass().getResource("/view/DashBoard_Form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = (Stage) this.rootNode.getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    void btnBrowseOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg", "*.gif", "*.bmp", "*.jpeg")
        );

        Stage stage = (Stage) rootNode.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            imageStudent.setImage(image);
            imageStudent.setPreserveRatio(false);
            imageStudent.setSmooth(true);
        }
        System.out.println(imageStudent);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtID.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"Id Field Empty!!").show();
            return;
        }

        try {
            boolean isDeleted = StudentModel.deleteStudent(id);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION,"Student Deleted Success!!!").showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR,"Deleted Failed!!!").showAndWait();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {

    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) {
        String Id = txtID.getText();
        String BarcodeId = txtBarcodeID.getText();
        String Name = txtNameWithInitials.getText();
        String Address = txtAddress.getText();
        String genderPromptTxt = cmbGender.getPromptText();
        String email = txtEmail.getText();
        LocalDate dob = LocalDate.parse(txtDateOfBirth.getText());
        String contact = txtContact.getText();
        String classPromptTxt = cmbClass.getPromptText();
        String subjects = txtSubject.getText();
        Image studentImage = imageStudent.getImage();

        StudentDto dto = new StudentDto(Id, BarcodeId, Name, Address, genderPromptTxt, email, dob, contact, classPromptTxt, subjects, studentImage);

        try {
            boolean isSavedStudent = StudentModel.saveStudent(dto , selectedImageFile);

            if (isSavedStudent) {
                new Alert(Alert.AlertType.INFORMATION, "Student Save Success!!!").showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR, "Save Failed!!1").showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String ID = txtID.getText();
        String Bar_id = txtBarcodeID.getText();
        String name = txtNameWithInitials.getText();
        String address = txtAddress.getText();
        String gender = cmbGender.getPromptText();
        String email = txtEmail.getText();
        LocalDate dob = LocalDate.parse(txtDateOfBirth.getText());
        String contact = txtContact.getText();
        String stu_class = cmbClass.getPromptText();
        String subject = txtSubject.getText();
        Image studentImage = imageStudent.getImage();

        StudentDto dto = new StudentDto(ID, Bar_id, name, address, gender, email, dob, contact, stu_class, subject, studentImage);

        try {
            boolean isUpdated = StudentModel.updateStudent(dto , selectedImageFile);

            if (isUpdated) {
                new Alert(Alert.AlertType.INFORMATION,"Update Success!!!").showAndWait();
            } else {
                new Alert(Alert.AlertType.ERROR,"Update Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void BarcodeIdSearchOnAction(ActionEvent event) {

    }

    @FXML
    void IdSearchOnAction(ActionEvent event) {
        String id = txtID.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"ID is Empty!!!").showAndWait();
            return;
        }

        try {
            StudentDto dto = StudentModel.searchStudent(id);

            if (dto != null) {
                txtBarcodeID.setText(dto.getBarcodeID());
                txtNameWithInitials.setText(dto.getName());
                txtAddress.setText(dto.getAddress());
                cmbGender.setPromptText(dto.getGender());
                txtEmail.setText(dto.getEmail());
                txtDateOfBirth.setText(String.valueOf(dto.getDob()));
                txtContact.setText(dto.getContact());
                cmbClass.setPromptText(dto.getStu_Class());
                txtSubject.setText(dto.getSubject());
                imageStudent.setImage(dto.getStudentImage());
            } else {
                new Alert(Alert.AlertType.ERROR,"Student Not Found!!!").showAndWait();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}