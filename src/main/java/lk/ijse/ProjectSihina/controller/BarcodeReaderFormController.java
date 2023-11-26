package lk.ijse.ProjectSihina.controller;


import com.github.sarxos.webcam.Webcam;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;

import java.awt.image.BufferedImage;

public class BarcodeReaderFormController {


    @FXML
    private Label barcodeLabel;

    @FXML
    private WebView webView;

    private Webcam webcam;


    public void initialize() {
        webcam = Webcam.getDefault();
        webcam.setViewSize(webcam.getViewSizes()[0]);

        readBarcode(webcam);
    }

    private void readBarcode(Webcam webcam) {
        Thread barcodeReaderThread = new Thread(() -> {
            while (true) {
                Result result = readQRCode(webcam.getImage());
                if (result != null) {
                    String barcodeText = result.getText();
                    System.out.println("Barcode detected: " + barcodeText);
                    updateBarcodeLabel(barcodeText);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        barcodeReaderThread.start();
    }

    private void updateBarcodeLabel(String barcodeText) {
        barcodeLabel.setText("Detected Barcode: " + barcodeText);
    }

    private Result readQRCode(BufferedImage image) {
        try {
            if (image != null) {
                BinaryBitmap binaryBitmap = new BinaryBitmap(
                        new HybridBinarizer(new BufferedImageLuminanceSource(image)));
                return new MultiFormatReader().decode(binaryBitmap);
            }
        } catch (ReaderException ignored) {

        }
        return null;
    }
}