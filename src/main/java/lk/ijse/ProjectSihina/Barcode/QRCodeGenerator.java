package lk.ijse.ProjectSihina.Barcode;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import javafx.scene.control.Alert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeGenerator {

    public static void generateQRCode(String data, String filePath) {
        int width = 300;
        int height = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = null;
            try {
                bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
            } catch (WriterException e) {
                throw new RuntimeException(e);
            }

            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = bufferedImage.createGraphics();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    graphics.setColor(bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    graphics.fillRect(x, y, 1, 1);
                }
            }

            graphics.dispose();

            File outputFile = new File(filePath);
            ImageIO.write(bufferedImage, "png", outputFile);
            new Alert(Alert.AlertType.INFORMATION,"QR Code generated successfully and saved at: " + filePath).show();
            System.out.println("QR Code generated successfully and saved at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}