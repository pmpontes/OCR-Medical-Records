package tools;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Log {

    private static boolean registerDetails = true;

    public static final void registerDetails(boolean active) {
        registerDetails = active;
    }

    public static final void error(String msg) {
        System.err.println("Error: " + msg);
        System.err.flush();
    }

    public static final void info(String msg) {
        System.out.println("Log:   " + msg);
        System.out.flush();
    }

    public static final void detail(String msg) {
        if(!registerDetails){
            return;
        }

        System.out.println(" (d)  " + msg);
        System.out.flush();
    }

    public static void showResult(Mat img) {
        /*MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}