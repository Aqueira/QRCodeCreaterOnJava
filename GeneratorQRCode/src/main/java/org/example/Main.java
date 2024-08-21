package org.example;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.HashMap;

public class Main {
    public static void main(String[] args){
        QRCodeGenerator generator = new QRCodeGenerator();
        generator.createQRCode("YOUR_FILE_PATH", "URL");
    }
}

class QRCodeGenerator{
    private static final int QR_CODE_SIZE = 300;
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color FOREGROUND_COLOR = Color.BLACK;
    private static final String IMAGE_FORMAT = "png";


    public void createQRCode(final String filePath, final String url){
        try{
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = createBitMatrix(qrCodeWriter, url);
            BufferedImage qrCodeImage = createEmptyImage();
            drawQRCode(qrCodeImage, bitMatrix);
            createFile(qrCodeImage, filePath);
        }catch (IOException | WriterException e) {
            System.err.println("Error to create QRCode: " + e.getMessage());
        }
    }

    private HashMap<EncodeHintType, ErrorCorrectionLevel> createHints() {
        HashMap<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        return hints;
    }

    private BufferedImage createEmptyImage() throws WriterException{
        BufferedImage image = new BufferedImage(QR_CODE_SIZE, QR_CODE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, QR_CODE_SIZE, QR_CODE_SIZE);
        graphics.dispose();
        return image;
    }

    private void drawQRCode(BufferedImage image, BitMatrix bitMatrix){
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(FOREGROUND_COLOR);

        for (int x = 0; x < QR_CODE_SIZE; x++) {
            for (int y = 0; y < QR_CODE_SIZE; y++) {
                if (bitMatrix.get(x, y))
                    graphics.fillRect(x, y, 1, 1);
            }
        }
        graphics.dispose();
    }

    private BitMatrix createBitMatrix(QRCodeWriter qrCodeWriter, String url) throws WriterException {
        HashMap<EncodeHintType, ErrorCorrectionLevel> hints = createHints();
        return qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE, hints);
    }

    private void createFile(BufferedImage image, String filePath) throws IOException {
        File qrCodeFile = new File(filePath);
        ImageIO.write(image, IMAGE_FORMAT, qrCodeFile);
    }
}
