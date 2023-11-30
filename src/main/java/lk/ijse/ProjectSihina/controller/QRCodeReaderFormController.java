package lk.ijse.ProjectSihina.controller;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import lk.ijse.ProjectSihina.dto.StudentDto;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class QRCodeReaderFormController {

    @FXML
    private AnchorPane miniPanel;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private JFXTextArea txtDateInsertArea;

    private Webcam webcam;
    private WebcamPanel webcamPanel;
    private boolean isReading = false;
    private PaymentFormController payForm;
    private boolean flag = false;

    private AttendanceFormController attForm;

    public void setPaymentFormController(PaymentFormController paymentFormController) {
        this.payForm = paymentFormController;
    }


    public void setAttendanceFormController(AttendanceFormController attendanceFormController) {
        this.attForm = attendanceFormController;
        flag = true;
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        txtDateInsertArea.clear();
    }

    @FXML
    void btnPauseOnAction(ActionEvent event) {
        stopWebcam();
    }

    @FXML
    void btnStartOnAction(ActionEvent event) {
        isReading = !isReading;
        if (isReading) {
            startWebcam();
        } else {
            stopWebcam();
        }
    }

    private void startWebcam() {
        if (webcam == null) {
            Dimension size = WebcamResolution.QVGA.getSize();
            webcam = Webcam.getDefault();
            webcam.setViewSize(size);

            webcamPanel = new WebcamPanel(webcam);
            webcamPanel.setPreferredSize(size);
            webcamPanel.setFPSDisplayed(true);
            webcamPanel.setMirrored(true);

            SwingNode swingNode = new SwingNode();
            swingNode.setContent(webcamPanel);

            miniPanel.getChildren().clear();
            miniPanel.getChildren().add(swingNode);
        }

        Thread thread = new Thread(() -> {
            while (isReading) {
                try {
                    Thread.sleep(1000);
                    BufferedImage image = webcam.getImage();
                    if (image != null) {
                        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
                        try {
                            Result result = new MultiFormatReader().decode(binaryBitmap);
                            Platform.runLater(() -> {
                                if (result != null) {
                                    stopWebcam();
                                    txtDateInsertArea.appendText(result.getText() + "\n");
                                    new Alert(Alert.AlertType.INFORMATION, "Data Scanned Successfully!").show();
                                    if(flag){
                                        attForm.updateTxtID(result.getText());
                                    } else {
                                        payForm.updateTxtID(result.getText());
                                    }
                                } else {
                                    new Alert(Alert.AlertType.ERROR, "No Data Found!").showAndWait();
                                }
                            });
                        } catch (NotFoundException e) {
                            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                        }
                    }
                } catch (InterruptedException | RuntimeException ignored) {

                }
            }
        });
        thread.start();
    }

    private void stopWebcam() {
        if (webcamPanel != null) {
            webcamPanel.stop();
            webcamPanel = null;
        }
        if (webcam != null) {
            webcam.close();
            webcam = null;
        }
        isReading = false;
    }
}
