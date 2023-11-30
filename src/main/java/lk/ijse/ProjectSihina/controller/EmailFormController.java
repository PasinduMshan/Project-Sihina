package lk.ijse.ProjectSihina.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import lk.ijse.ProjectSihina.dto.ClassDto;
import lk.ijse.ProjectSihina.model.ClassModel;
import lk.ijse.ProjectSihina.model.MailModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class EmailFormController implements Initializable {

    @FXML
    private AnchorPane Node;

    @FXML
    private JFXComboBox<String> cmbGrades;

    @FXML
    private JFXComboBox<String> cmbStuOrTea;

    @FXML
    private Label lblStatus;

    @FXML
    private JFXTextField txtMailAddress;

    @FXML
    private JFXTextArea txtMessage;

    @FXML
    private JFXTextField txtSubjectInMail;

    private List<String> allEmailsByClass;
    private List<String> allEmailFromStudentOrTeacher;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAllClasses();
        loadAllTeachOrStu();
    }

    private void loadAllTeachOrStu() {
        ObservableList<String> obList = FXCollections.observableArrayList();
        obList.add("Student");
        obList.add("Teacher");
        cmbStuOrTea.setItems(obList);
    }

    private void loadAllClasses() {
        ObservableList<String> obList = FXCollections.observableArrayList();

        try {
            List<ClassDto> nameList = ClassModel.getAllClass();

            for (ClassDto dto : nameList) {
                obList.add(dto.getClassName());
            }
            cmbGrades.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    private void getAllEmailTeacherOrStudent() {
        String TeachOrStu = cmbStuOrTea.getValue();
        try {
            allEmailFromStudentOrTeacher = MailModel.getAllEmailFromStudentOrTeacher(TeachOrStu);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }


    private void getAllEmailByGrades()  {
        String  StuClass = cmbGrades.getValue();

        try {
            allEmailsByClass = MailModel.getAllEmailsByClass(StuClass);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    void RefreshOnAction(ActionEvent event) {
        txtMailAddress.setText("");
        txtSubjectInMail.setText("");
        txtMessage.setText("");
        cmbGrades.setValue("");
        cmbStuOrTea.setValue("");
    }

    @FXML
    void btnSendOnAction(ActionEvent event) {
        System.out.println("Start");
        lblStatus.setText("sending...");
        Mail mail = new Mail(); // creating an instance of Mail class
        mail.setMsg(txtMessage.getText()); // email message
        mail.setSubject(txtSubjectInMail.getText()); // email subject

        // Check if sending to a specific email or group
        if (txtMailAddress.getText().isEmpty()) {
            // Sending to a group
            if (cmbGrades.getValue() != null) {
                getAllEmailByGrades();
                mail.setTo(allEmailsByClass);
            } else if (cmbStuOrTea.getValue() != null) {
                getAllEmailTeacherOrStudent();
                mail.setTo(allEmailFromStudentOrTeacher);
            }
        } else {
            // Sending to a specific email
            mail.setTo(txtMailAddress.getText());
        }

        Thread thread = new Thread(mail);
        thread.start();

        System.out.println("end");
        lblStatus.setText("sended");
    }

    public static class Mail implements Runnable {
        private String msg;
        private List<String> to;
        private String subject;

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public void setTo(List<String> to) {
            this.to = to;
        }
        public void setTo(String to) {
            this.to = List.of(to);
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public boolean outMail(String recipient) throws MessagingException {
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

            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(from));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            mimeMessage.setSubject(this.subject);
            mimeMessage.setText(this.msg);

            Transport.send(mimeMessage);
            return true;
        }

        public boolean sendToAllRecipients() {
            for (String recipient : to) {
                try {
                    outMail(recipient);
                } catch (MessagingException e) {
                    e.printStackTrace();
                    return false; // Handle failure to send to one or more recipients
                }
            }
            return true; // All emails sent successfully
        }

        public void run() {
            if (msg != null && to != null && !to.isEmpty()) {
                if (sendToAllRecipients()) {
                    System.out.println("All emails sent successfully!");
                } else {
                    System.out.println("Failed to send one or more emails.");
                }
            } else {
                System.out.println("Not sent. Empty msg or recipient list!");
            }
        }
    }
}
