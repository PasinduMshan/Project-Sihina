package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXTextField;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.ProjectSihina.model.ForgetPasswordModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

public class ForgetPasswordFormController {

    @FXML
    private AnchorPane Node;

    @FXML
    private Label lblCheckYourEmail;

    @FXML
    private TextField txtOTPBox;


    @FXML
    private JFXTextField txtNIC;

    private String OTP;

    @FXML
    void btnSubmitOnAction(ActionEvent event) throws IOException {
        String NIC = txtNIC.getText();
        String otpBox = txtOTPBox.getText();
        if (otpBox.equals(OTP)) {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/view/Change_Credentials_Form.fxml"));
            Parent rootNode = loader.load();
            ChangeCredentialsFormController changeCredentialsFormController = loader.getController();
            changeCredentialsFormController.initialData(NIC);
            Scene scene = new Scene(rootNode);
            Stage stage = (Stage) this.Node.getScene().getWindow();
            stage.setScene(scene);

        } else {
            new Alert(Alert.AlertType.ERROR,"The OTP you entered does not match!!").showAndWait();
        }
    }

    @FXML
    void btnGetOTPOnAction(ActionEvent event) {
        final String numbers = "0123456789";
        final int OTP_LENGTH = 6;

        String from = "institutesihina@gmail.com"; //sender's email address
        String host = "localhost";

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", 587);
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("institutesihina@gmail.com", "bzga kpfg ixpa cnir");
            }
        });
        try {
            String nic = txtNIC.getText();
            String email = ForgetPasswordModel.getEmail(nic);

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setSubject("Change Password OTP");

            Random random = new Random();
            StringBuilder otp = new StringBuilder();

            for (int i = 0; i < OTP_LENGTH; i++) {
                int index = random.nextInt(numbers.length());
                otp.append(numbers.charAt(index));
            }

            OTP = otp.toString();


            mimeMessage.setText("Welcome to Sihina Institute.\n\nYour OTP is "+OTP);

            Transport.send(mimeMessage);

            new Alert(Alert.AlertType.CONFIRMATION,"Otp Sent to your email successfully!").show();
            lblCheckYourEmail.setText("Check your email for the OTP");

        } catch (MessagingException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

}