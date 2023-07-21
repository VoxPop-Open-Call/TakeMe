package pt.famility.backoffice.util;

import pt.famility.backoffice.web.rest.errors.ImageException;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResizeImage {
    public static byte[] resize(byte[] sourceFile, int width, int height, String contentType) throws ImageException, IOException {

        int scaledWidth = width;
        int scaledHeight = height;
        String fileType;
        Image resizedImg = null;
        BufferedImage bim;
        byte[] result;

        switch (contentType) {
            case "image/jpeg":
                fileType = "jpg";
                break;
            case "image/png":
                fileType = "png";
                break;
            default:
                throw new ImageException("Invalid content type for resizing");
        }

        try(InputStream in = new ByteArrayInputStream(sourceFile)) {
            bim = ImageIO.read(in);
        }

        // Find if the image is in portrait or landscape
        if(bim.getWidth() >= bim.getHeight()) {
            if(bim.getWidth() > width ) { // if larger than defined
                resizedImg = bim.getScaledInstance(width,-1, Image.SCALE_FAST);
                scaledHeight = resizedImg.getHeight(null);
                scaledWidth = width;
            }
        } else {
            if(bim.getHeight() > height ) { //if larger than defined
                resizedImg = bim.getScaledInstance(-1, height, Image.SCALE_FAST);
                scaledWidth = resizedImg.getWidth(null);
                scaledHeight = height;
            }
        }

        if(resizedImg == null) {
            return sourceFile;
        }

        BufferedImage rBimg = new BufferedImage(scaledWidth, scaledHeight, bim.getType());
        // Create Graphics object
        Graphics2D g = rBimg.createGraphics();
        // Draw the resizedImg from 0,0 with no ImageObserver
        g.drawImage(resizedImg,0,0,null);

        // Dispose the Graphics object, we no longer need it
        g.dispose();

        try(ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(rBimg, fileType, baos);
            result = baos.toByteArray();
        }
        return result;
    }
}
