package lk.ijse.ProjectSihina.Barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BarcodeGenerator {
    public static void generateBarcode(String data, String filePath) {
        int width = 300;
        int height = 100;

        Code128Writer code128Writer = new Code128Writer();
            try {
                BitMatrix bitMatrix = code128Writer.encode(data, BarcodeFormat.CODE_128, width, height, null);

                BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                    }
                }

                File outputFile = new File(filePath);
                ImageIO.write(bufferedImage, "png", outputFile);

                System.out.println("Barcode generated successfully and saved at: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
    public static void main(String[] args) {
        // Example of generating a Barcode ID
        String barcodeData = "ID0001"; // Replace with your actual data
        String outputFilePath = "/home/pasindu/Desktop/barcode.png"; // Replace with the desired output path

        generateBarcode(barcodeData, outputFilePath);
    }

}

