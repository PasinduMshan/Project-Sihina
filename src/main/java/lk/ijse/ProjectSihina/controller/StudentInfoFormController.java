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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.ProjectSihina.Barcode.QRCodeGenerator;
import lk.ijse.ProjectSihina.Other.ArrowKeyPress;
import lk.ijse.ProjectSihina.db.DbConnection;
import lk.ijse.ProjectSihina.dto.ClassDto;
import lk.ijse.ProjectSihina.dto.StudentDto;
import lk.ijse.ProjectSihina.dto.Tm.StudentTM;
import lk.ijse.ProjectSihina.model.ClassModel;
import lk.ijse.ProjectSihina.model.StudentModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class StudentInfoFormController implements Initializable {

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
    private TableView<StudentTM> tblStudent;

    @FXML
    private JFXTextField txtAddress;

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

    public static File selectedImageFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       loadGender();
       loadAllClass();
       setCellValueFactory();
       loadAllStudent();
       generateStuId();
       tableListener();
       ArrowKeyPress.switchTextFieldOnArrowPressDown(txtID,txtNameWithInitials);
       ArrowKeyPress.switchTextFieldOnArrowPressDown(txtNameWithInitials,txtEmail);
       ArrowKeyPress.switchTextFieldOnArrowPressDown(txtEmail,txtAddress);
       ArrowKeyPress.switchTextFieldOnArrowPressDown(txtAddress,txtSubject);
       ArrowKeyPress.switchTextFieldOnArrowPressDown(txtDateOfBirth,txtContact);
       ArrowKeyPress.switchTextFieldOnArrowPressDown(txtContact,txtAddress);
       ArrowKeyPress.switchTextFieldOnArrowPressRight(txtNameWithInitials,txtDateOfBirth);
       ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtDateOfBirth,txtNameWithInitials);
       ArrowKeyPress.switchTextFieldOnArrowPressRight(txtEmail,txtContact);
       ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtContact,txtEmail);
       ArrowKeyPress.switchTextFieldOnArrowPressUP(txtSubject,txtAddress);
       ArrowKeyPress.switchTextFieldOnArrowPressUP(txtAddress,txtEmail);
       ArrowKeyPress.switchTextFieldOnArrowPressUP(txtEmail,txtNameWithInitials);
       ArrowKeyPress.switchTextFieldOnArrowPressUP(txtNameWithInitials,txtID);
       ArrowKeyPress.switchTextFieldOnArrowPressUP(txtContact,txtDateOfBirth);
       ArrowKeyPress.switchTextFieldOnArrowPressUP(txtDateOfBirth,txtID);
    }



    private void generateStuId() {
        try {
            String StuId = StudentModel.generateStudentId();
            txtID.setText(StuId);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadAllStudent() {
        ObservableList<StudentTM> obList = FXCollections.observableArrayList();

        try {
            List<StudentDto> dtoList = StudentModel.getAllStudent();

            for (StudentDto dto : dtoList) {
                obList.add(new StudentTM(
                        dto.getID(),
                        dto.getName(),
                        dto.getStu_Class(),
                        dto.getEmail(),
                        dto.getContact()
                ));
            }
            tblStudent.setItems(obList);
        } catch (SQLException e) {
           new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colClass.setCellValueFactory(new PropertyValueFactory<>("Stu_Class"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contact"));
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
                loadAllStudent();
                clearFields();
            } else {
                new Alert(Alert.AlertType.ERROR,"Deleted Failed!!!").showAndWait();
            }

        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnPrintOnAction(ActionEvent event)  {
        try {
            InputStream resourceAsStream = getClass().getResourceAsStream("/report/StudentDetails.jrxml");
            JasperDesign load = JRXmlLoader.load(resourceAsStream);
            JasperReport compileReport = JasperCompileManager.compileReport(load);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    compileReport,
                    null,
                    DbConnection.getInstance().getConnection()
            );
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }

    public StudentDto getAllValueInField(){
        String Id = txtID.getText();
        String Name = txtNameWithInitials.getText();
        String Address = txtAddress.getText();
        String genderPromptTxt = cmbGender.getValue();
        String email = txtEmail.getText();
        String dob = txtDateOfBirth.getText();
        String contact = txtContact.getText();
        String classPromptTxt = cmbClass.getValue();
        String subjects = txtSubject.getText();
        Image studentImage = imageStudent.getImage();

        if (Id.isEmpty() || genderPromptTxt.isEmpty() || classPromptTxt.isEmpty() || subjects.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"Some Fields are empty!!!").showAndWait();

        }
        boolean validateDetail = validateStudentDetail(Name, Address, email,dob, contact);
        StudentDto studentDto = null;
        if (validateDetail) {
            LocalDate date = LocalDate.parse(dob);
            studentDto =  new StudentDto(Id, Name, Address, genderPromptTxt, email, date, contact, classPromptTxt, subjects, studentImage);
        }
        return studentDto;
    }

    private boolean validateStudentDetail(String name, String address, String email, String dob, String contact) {

        boolean matches = Pattern.matches("[A-Za-z\\s.]+", name);
        if (!matches) {
            new Alert(Alert.AlertType.ERROR,"Invalid Name!!").show();
            return false;
        }

        boolean matches1 = Pattern.matches("[A-Za-z0-9/,.\\s]+", address);
        if (!matches1) {
            new Alert(Alert.AlertType.ERROR,"Invalid Address!!").show();
            return false;
        }

        boolean matches2 = Pattern.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", email);
        if (!matches2) {
            new Alert(Alert.AlertType.ERROR,"Invalid Email!!").show();
            return false;
        }

        String Dob = String.valueOf(dob);
        boolean matches3 = Pattern.matches("[0-9-]+", Dob);
        if (!matches3) {
            new Alert(Alert.AlertType.ERROR,"Invalid Date Of Birth!!").show();
            return false;
        }

        boolean matches4 = Pattern.matches("^(?:7|0|(?:\\+94))[0-9]{9,10}$", contact);
        if (!matches4) {
            new Alert(Alert.AlertType.ERROR,"Invalid Contact!!").show();
            return false;
        }

        return true;
    }

    @FXML
    void btnRegisterOnAction(ActionEvent event) throws IOException {
        if ((getAllValueInField() != null)) {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/Guardian_Info_Form.fxml"));
            Parent rootNode = loader.load();
            GuardianInfoFormController guardianInfoFormController = loader.getController();
            guardianInfoFormController.initialData(getAllValueInField());
            Scene scene = new Scene(rootNode);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
            clearFields();
        } else {
            new Alert(Alert.AlertType.ERROR,"First You Should Input Details Clearly!!!").show();
            return;
        }
        System.out.println("");
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String ID = txtID.getText();
        String name = txtNameWithInitials.getText();
        String address = txtAddress.getText();
        String gender = cmbGender.getValue();
        String email = txtEmail.getText();
        String dob = txtDateOfBirth.getText();
        String contact = txtContact.getText();
        String stu_class = cmbClass.getValue();
        String subject = txtSubject.getText();
        Image studentImage = imageStudent.getImage();

        if (ID.isEmpty() || gender.isEmpty() || stu_class.isEmpty() || subject.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"Some Fields are empty!!!").showAndWait();
        }
        boolean UpdateValidated = validateStudentDetail(name, address, email, dob, contact);
        if (UpdateValidated) {
            LocalDate date = LocalDate.parse(dob);
            StudentDto dto = new StudentDto(ID, name, address, gender, email, date, contact, stu_class, subject, studentImage);

            try {
                boolean isUpdated = StudentModel.updateStudent(dto);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Update Success!!!").showAndWait();
                    loadAllStudent();
                    clearFields();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Update Failed!!!").showAndWait();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
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
                txtNameWithInitials.setText(dto.getName());
                txtAddress.setText(dto.getAddress());
                cmbGender.setValue(dto.getGender());
                txtEmail.setText(dto.getEmail());
                txtDateOfBirth.setText(String.valueOf(dto.getDob()));
                txtContact.setText(dto.getContact());
                cmbClass.setValue(dto.getStu_Class());
                txtSubject.setText(dto.getSubject());
                imageStudent.setImage(dto.getStudentImage());
            } else {
                new Alert(Alert.AlertType.ERROR,"Student Not Found!!!").showAndWait();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void clearFields() {
        txtID.setText("");
        txtNameWithInitials.setText("");
        txtAddress.setText("");
        cmbGender.setValue("");
        txtEmail.setText("");
        txtDateOfBirth.setText("");
        txtContact.setText("");
        cmbClass.setValue("");
        txtSubject.setText("");
        imageStudent.setImage(null);
        generateStuId();
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        clearFields();
    }

    @FXML
    void btnGuardianOnAction(ActionEvent event) throws IOException {
        Parent rootNode = FXMLLoader.load(this.getClass().getResource("/view/Guardian_Info_Form.fxml"));
        Scene scene = new Scene(rootNode);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void btcGenerateQROnAction(ActionEvent event) {
        String StuId = txtID.getText();
        String name = txtNameWithInitials.getText();
        String qrCodeData = StuId;
        String outputFilePath ="/home/pasindu/Desktop/Final Project Semester 01/QRCode png/" + name + "_" + StuId + ".png";

        QRCodeGenerator.generateQRCode(qrCodeData, outputFilePath);
    }

    private void tableListener(){
        tblStudent.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, StudentTm, t1) -> {
                    try {
                        StudentDto dto = StudentModel.searchStudent(t1.getID());
                        if (dto != null) {
                            txtID.setText(dto.getID());
                            txtNameWithInitials.setText(dto.getName());
                            txtAddress.setText(dto.getAddress());
                            cmbGender.setValue(dto.getGender());
                            txtEmail.setText(dto.getEmail());
                            txtDateOfBirth.setText(String.valueOf(dto.getDob()));
                            txtContact.setText(dto.getContact());
                            cmbClass.setValue(dto.getStu_Class());
                            txtSubject.setText(dto.getSubject());
                            imageStudent.setImage(dto.getStudentImage());
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                });
    }
}