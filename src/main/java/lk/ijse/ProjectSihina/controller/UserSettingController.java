package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXPasswordField;
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
import javafx.scene.layout.AnchorPane;
import lk.ijse.ProjectSihina.Other.ArrowKeyPress;
import lk.ijse.ProjectSihina.dto.SubjectDto;
import lk.ijse.ProjectSihina.dto.Tm.userTm;
import lk.ijse.ProjectSihina.dto.UserDto;
import lk.ijse.ProjectSihina.model.SignUpModel;
import lk.ijse.ProjectSihina.model.SubjectModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UserSettingController implements Initializable {

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colFirstName;

    @FXML
    private TableColumn<?, ?> colLastName;

    @FXML
    private TableColumn<?, ?> colNIC;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private AnchorPane moveNode;

    @FXML
    private TableView<userTm> tblUser;

    @FXML
    private JFXPasswordField txtConfirmPassword;

    @FXML
    private JFXTextField txtConfirmUserName;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtFirstName;

    @FXML
    private JFXTextField txtLastName;

    @FXML
    private JFXTextField txtNIC;

    @FXML
    private JFXPasswordField txtNewPassword;

    @FXML
    private JFXTextField txtNewUserName;

    @FXML
    private JFXPasswordField txtNowPassword;

    @FXML
    private JFXTextField txtNowUserName;

    @FXML
    private JFXTextField txtUserId;

    @FXML
    private JFXTextField txtNICTOChange;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        loadAllUser();
        tableListener();
        ArrowKeyPress.switchTextFieldOnArrowPressRight(txtUserId,txtFirstName);
        ArrowKeyPress.switchTextFieldOnArrowPressRight(txtFirstName,txtLastName);
        ArrowKeyPress.switchTextFieldOnArrowPressRight(txtLastName,txtEmail);
        ArrowKeyPress.switchTextFieldOnArrowPressRight(txtEmail,txtNIC);
        ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtNIC,txtEmail);
        ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtEmail,txtLastName);
        ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtLastName,txtFirstName);
        ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtFirstName,txtUserId);
        ArrowKeyPress.switchTextFieldOnArrowPressDown(txtNIC,txtNICTOChange);

        ArrowKeyPress.switchTextFieldOnArrowPressRight(txtNICTOChange,txtNowUserName);
        ArrowKeyPress.switchTextFieldOnArrowPressRightToPassword(txtNowUserName,txtNowPassword);
        ArrowKeyPress.switchTextFieldOnArrowPressDownToPassword(txtNowPassword,txtNewUserName);
        ArrowKeyPress.switchTextFieldOnArrowPressUPToPassword(txtNewUserName,txtNowPassword);
        ArrowKeyPress.switchTextFieldOnArrowPressLeftToPassword(txtNowPassword,txtNowUserName);
        ArrowKeyPress.switchTextFieldOnArrowPressLeft(txtNowUserName,txtNICTOChange);

        ArrowKeyPress.switchTextFieldOnArrowPressRightToPassword(txtNewUserName,txtNewPassword);
        ArrowKeyPress.switchTextFieldOnArrowPressLeftToPassword(txtNewPassword,txtNewUserName);
        ArrowKeyPress.switchTextFieldOnArrowPressDownToPassword(txtNewPassword,txtConfirmUserName);
        ArrowKeyPress.switchTextFieldOnArrowPressUPToPassword(txtConfirmUserName,txtNewPassword);

        ArrowKeyPress.switchTextFieldOnArrowPressRightToPassword(txtConfirmUserName,txtConfirmPassword);
        ArrowKeyPress.switchTextFieldOnArrowPressLeftToPassword(txtConfirmPassword,txtConfirmUserName);
    }

    private void loadAllUser() {
        ObservableList<userTm> obList = FXCollections.observableArrayList();

        try {
            List<UserDto> dtoList = SignUpModel.getAllUsers();

            for (UserDto dto : dtoList) {
                obList.add(new userTm(
                        dto.getUserId(),
                        dto.getFirstName(),
                        dto.getLastName(),
                        dto.getNIC(),
                        dto.getEmail()
                ));
            }
            tblUser.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void setCellValueFactory() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        colNIC.setCellValueFactory(new PropertyValueFactory<>("NIC"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
    }

    @FXML
    void btnChangeCredentialsOnAction(ActionEvent event) {
        String NIC = txtNICTOChange.getText();
        boolean matches = Pattern.matches("^(?:19|20)?\\d{2}[0-9]{10}|[0-9]{9}[x|X|v|V]$", NIC);
        if (!matches) {
            new Alert(Alert.AlertType.ERROR,"Invalid NIC!!").show();
            return;
        }
        String userNameNow = txtNowUserName.getText();
        String passwordNow = txtNowPassword.getText();
        boolean isValidate = validationCredentials(userNameNow, passwordNow);
        if (isValidate) {
            try {
                boolean isMatchedNIC = SignUpModel.checkNIC(NIC);
                if (isMatchedNIC) {
                    boolean isMatch = SignUpModel.checkCredentials(userNameNow, passwordNow, NIC);
                    if (isMatch) {
                        String newUserName = txtNewUserName.getText();
                        String newPassword = txtNewPassword.getText();
                        boolean isValidatedNew = validationCredentials(newUserName, newPassword);
                        if (isValidatedNew) {
                            String confirmUserName = txtConfirmUserName.getText();
                            String confirmPassword = txtConfirmPassword.getText();
                            boolean isValidatedConfirm = validationCredentials(confirmUserName, confirmPassword);
                            if (isValidatedConfirm) {
                                if (newUserName.equals(confirmUserName) && newPassword.equals(confirmPassword)) {
                                    boolean ChangeCredentials = SignUpModel.updateCredentials(newUserName, newPassword, NIC);
                                    if (ChangeCredentials) {
                                        new Alert(Alert.AlertType.INFORMATION, "Change Credentials Success!!!").showAndWait();
                                        clearField();
                                    } else {
                                        new Alert(Alert.AlertType.ERROR, "Change Credentials Failed!!!").showAndWait();
                                    }
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "Your New UserName & Password Not Match with Confirm UserName & Password. Please Check..").show();
                                }
                            }
                        }
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Your UserName & Password Not Matched!!!").show();
                    }
                } else {
                    new Alert(Alert.AlertType.ERROR, "Your NIC Number Not Matched!!!").showAndWait();
                    return;
                }
                System.out.println();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String UserId = txtUserId.getText();
        if (UserId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"User Id is empty!!!").show();
            return;
        }

        try {
            boolean isDeleted = SignUpModel.deleteUser(UserId);
            if (isDeleted) {
                new Alert(Alert.AlertType.INFORMATION,"User Delete Success!!!").showAndWait();
                loadAllUser();
                clearField();
            } else {
                new Alert(Alert.AlertType.ERROR,"User Delete Failed!!!").showAndWait();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void btnHelpOnAction(ActionEvent event) {

    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String NIC = txtNIC.getText();
        if (NIC.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"NIC Field is Empty!!!").show();
            return;
        }

        try {
            UserDto userDto = SignUpModel.searchUser(NIC);
            if (userDto != null) {
                txtUserId.setText(userDto.getUserId());
                txtFirstName.setText(userDto.getFirstName());
                txtLastName.setText(userDto.getLastName());
                txtEmail.setText(userDto.getEmail());
                txtNIC.setText(userDto.getNIC());
            } else {
                new Alert(Alert.AlertType.ERROR,"User Not Found!!!").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String UserId = txtUserId.getText();
        String FirstName = txtFirstName.getText();
        String LastName = txtLastName.getText();
        String email = txtEmail.getText();
        String nic = txtNIC.getText();

        if (UserId.isEmpty()) {
            new Alert(Alert.AlertType.ERROR,"ID Field is Empty!!!").show();
            return;
        }

        boolean isValidate = validateUserDetail(FirstName, LastName, email, nic);
        if (isValidate) {
            UserDto userDto = new UserDto(UserId,FirstName,LastName,email,nic);
            try {
                boolean isUpdated = SignUpModel.updateUser(userDto);
                if (isUpdated) {
                    new Alert(Alert.AlertType.INFORMATION,"User Update Success!!!").showAndWait();
                    loadAllUser();
                } else {
                    new Alert(Alert.AlertType.ERROR,"User Update Failed!!!").showAndWait();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }

    }

    private boolean validateUserDetail(String firstName, String lastName, String email, String NIC) {

        boolean matches = Pattern.matches("[A-Za-z]+", firstName);
        if (!matches) {
            new Alert(Alert.AlertType.ERROR,"Invalid First Name!!").show();
            return false;
        }

        boolean matches1 = Pattern.matches("[A-Za-z]+", lastName);
        if (!matches1) {
            new Alert(Alert.AlertType.ERROR,"Invalid Last Name!!").show();
            return false;
        }

        boolean matches2 = Pattern.matches("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", email);
        if (!matches2) {
            new Alert(Alert.AlertType.ERROR,"Invalid Email!!").show();
            return false;
        }

        boolean matches3 = Pattern.matches("^(?:19|20)?\\d{2}[0-9]{10}|[0-9]{9}[x|X|v|V]$", NIC);
        if (!matches3) {
            new Alert(Alert.AlertType.ERROR,"Invalid NIC!!").show();
            return false;
        }

        return true;
    }

    private boolean validationCredentials(String userName, String password){
        boolean matches4 = Pattern.matches("[a-zA-Z0-9]{5,13}", userName);
        if (!matches4) {
            new Alert(Alert.AlertType.ERROR,"Invalid UserName!!").show();
            return false;
        }

        boolean matches5 = Pattern.matches("^[a-zA-Z0-9]{4,}$", password);
        if (!matches5) {
            new Alert(Alert.AlertType.ERROR,"Invalid Password!!  Only include(A-Z,a-z,0-9) & (at least 4 characters)").show();
            return false;
        }
        return true;
    }

    private void clearField() {
        txtUserId.setText("");
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtNIC.setText("");
        txtNICTOChange.setText("");
        txtNowUserName.setText("");
        txtNowPassword.setText("");
        txtNewUserName.setText("");
        txtNewPassword.setText("");
        txtConfirmUserName.setText("");
        txtConfirmPassword.setText("");
    }

    public void btnRefreshOnAction(ActionEvent actionEvent) {
        clearField();
    }

    private void tableListener(){
        tblUser.getSelectionModel()
                .selectedItemProperty()
                .addListener((observableValue, userTm, t1) -> {
                    txtUserId.setText(t1.getUserId());
                    txtFirstName.setText(t1.getFirstName());
                    txtLastName.setText(t1.getLastName());
                    txtEmail.setText(t1.getEmail());
                    txtNIC.setText(t1.getNIC());
                });
    }
}