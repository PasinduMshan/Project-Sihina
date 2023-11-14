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
import lk.ijse.ProjectSihina.dto.StudentDto;
import lk.ijse.ProjectSihina.model.StudentModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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
    private JFXComboBox<?> cmbClass;

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
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {

    }

}