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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lk.ijse.ProjectSihina.Other.ArrowKeyPress;
import lk.ijse.ProjectSihina.dto.TeacherDto;
import lk.ijse.ProjectSihina.dto.Tm.TeacherTm;
import lk.ijse.ProjectSihina.model.teacherModel;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class TeacherFormController implements Initializable {

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colID;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colSubjects;

    @FXML
    private AnchorPane moveNode;

    @FXML
    private Pane paneTeacherImage;

    @FXML
    private TableView<TeacherTm> tblTeacher;

    @FXML
    private JFXTextField txtAdderss;

    @FXML
    private JFXTextField txtContactNo;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtID;

    @FXML
    private JFXTextField txtNameWithInitials;

    @FXML
    private JFXTextField txtSubject;

    @FXML
    private ImageView imageTeacher;

    private File selectedImageFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadAllTeacher();
        generateTeacherId();
        ArrowKeyPress.switchTextFieldOnArrowPressRight(txtID,txtNameWithInitials);
        ArrowKeyPress.switchTextFieldOnArrowPressRight(txtNameWithInitials,txtContactNo);
        ArrowKeyPress.switchTextFieldOnArrowPressRight(txtAdderss,txtEmail);
        ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtContactNo,txtNameWithInitials);
        ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtNameWithInitials,txtID);
        ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtEmail,txtAdderss);
        ArrowKeyPress.switchTextFieldOnArrowPressDown(txtID,txtAdderss);
        ArrowKeyPress.switchTextFieldOnArrowPressDown(txtAdderss,txtSubject);
        ArrowKeyPress.switchTextFieldOnArrowPressDown(txtContactNo,txtEmail);
        ArrowKeyPress.switchTextFieldOnArrowPressDown(txtEmail,txtSubject);
        ArrowKeyPress.switchTextFieldOnArrowPressDown(txtAdderss,txtSubject);
        ArrowKeyPress.switchTextFieldOnArrowPressUP(txtSubject,txtAdderss);
        ArrowKeyPress.switchTextFieldOnArrowPressUP(txtAdderss,txtID);
        ArrowKeyPress.switchTextFieldOnArrowPressUP(txtEmail,txtContactNo);
    }

    private void generateTeacherId() {
        try {
            String id = teacherModel.generateTeacherId();
            txtID.setText(id);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void loadAllTeacher() {
        ObservableList<TeacherTm> obList = FXCollections.observableArrayList();

        try {
            List<TeacherDto> dtoList = teacherModel.getAllTeachers();

            for (TeacherDto dto : dtoList ) {
                obList.add(new TeacherTm(
                        dto.getId(),
                        dto.getName(),
                        dto.getEmail(),
                        dto.getContactNo(),
                        dto.getSubjects()
                ));
            }
            tblTeacher.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        colSubjects.setCellValueFactory(new PropertyValueFactory<>("Subject"));
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String teacherId = txtID.getText();
        String teacherName = txtNameWithInitials.getText();
        String address = txtAdderss.getText();
        String email = txtEmail.getText();
        String contactNo = txtContactNo.getText();
        String subjects = txtSubject.getText();
        Image image = imageTeacher.getImage();

        if (teacherId.isEmpty() || subjects.isEmpty() || image == null){
            new Alert(Alert.AlertType.ERROR,"Empty Field Catch!!!").show();
            return;
        }

        boolean teacherDetail = validateTeacherDetail(teacherName, address, email, contactNo);

        if (teacherDetail) {
            TeacherDto dto = new TeacherDto(teacherId, teacherName, address, email, subjects, contactNo, image);

            try {
                boolean isAdded = teacherModel.addTeacher(dto);

                if (isAdded) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Teacher save success!!!").showAndWait();
                    clearField();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Save Failed!!!").showAndWait();
                }

            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private boolean validateTeacherDetail(String name, String address, String email, String contact) {

        boolean matches = Pattern.matches("[A-Za-z]+", name);
        if (!matches) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name!!").show();
            return false;
        }

        boolean matches1 = Pattern.matches("[A-Za-z0-9/,.]+", address);
        if (!matches1) {
            new Alert(Alert.AlertType.ERROR, "Invalid Address!!").show();
            return false;
        }

        boolean matches2 = Pattern.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", email);
        if (!matches2) {
            new Alert(Alert.AlertType.ERROR, "Invalid Email!!").show();
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
    void btnBrowseOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg","*.gif","*.bmp","*.jpeg")
        );

        Stage stage = (Stage) moveNode.getScene().getWindow();
        selectedImageFile = fileChooser.showOpenDialog(stage);

        if (selectedImageFile != null) {
            Image image = new Image(selectedImageFile.toURI().toString());
            imageTeacher.setImage(image);
            imageTeacher.setPreserveRatio(false);
            imageTeacher.setSmooth(true);
        }
        System.out.println(imageTeacher);
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = txtID.getText();

        if(id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"ID Field is Empty!!").show();
            return;
        }

        try {
            boolean isDeleted = teacherModel.deleteTeacher(id);

            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION,"Teacher Delete Success!!!").showAndWait();
                clearField();
            } else {
                new Alert(Alert.AlertType.ERROR,"Delete Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }

    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String teacherId = txtID.getText();
        String teacherName = txtNameWithInitials.getText();
        String address = txtAdderss.getText();
        String email = txtEmail.getText();
        String contactNo = txtContactNo.getText();
        String subjects = txtSubject.getText();
        Image image = imageTeacher.getImage();

        if (teacherId.isEmpty() || subjects.isEmpty() || image == null){
            new Alert(Alert.AlertType.ERROR,"Empty Field Catch!!!").show();
            return;
        }

        boolean validateDetail = validateTeacherDetail(teacherName, address, email, contactNo);
        if (validateDetail) {
            TeacherDto dto = new TeacherDto(teacherId, teacherName, address, email, subjects, contactNo, image);

            try {
                boolean isUpdated = teacherModel.updateTeacher(dto);

                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION, "Update Success!!!").showAndWait();
                    clearField();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Updated Failed!!1").showAndWait();
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void btnPrintOnAction(ActionEvent event) {
        txtID.setText("");
        txtNameWithInitials.setText("");
        txtContactNo.setText("");
        txtAdderss.setText("");
        txtEmail.setText("");
        txtSubject.setText("");
        generateTeacherId();
    }

    private void clearField() {
        txtID.setText("");
        txtNameWithInitials.setText("");
        txtContactNo.setText("");
        txtAdderss.setText("");
        txtEmail.setText("");
        txtSubject.setText("");
        generateTeacherId();
    }

    public void SearchTeacherOnAction(ActionEvent actionEvent) {
        String id = txtID.getText();

        if (id.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"ID Field is Empty!!!").show();
            return;
        }

        try {
            TeacherDto dto = teacherModel.searchTeacher(id);

            if (dto != null) {
                txtID.setText(dto.getId());
                txtNameWithInitials.setText(dto.getName());
                txtAdderss.setText(dto.getAddress());
                txtEmail.setText(dto.getEmail());
                txtSubject.setText(dto.getSubjects());
                txtContactNo.setText(dto.getContactNo());
                imageTeacher.setImage(dto.getImageTeacher());
            } else {
                new Alert(Alert.AlertType.ERROR,"Teacher Not Found!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }

    }


    public void btnRefreshOnAction(ActionEvent actionEvent) {
        clearField();
    }
}