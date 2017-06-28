package ocr;

import java.awt.*;
import java.io.File;

import net.sourceforge.tess4j.*;
import tools.Log;

import javax.imageio.ImageIO;

public class CharacterRecognitionHandler {

    private static final int SINGLE_LINE_SEGMENTATION_MODE = 7;  // Treat the image as a single text line.
    private static CharacterRecognitionHandler handlerInst = null;
    private static Tesseract tessInst = null;

    private CharacterRecognitionHandler(){
        ImageIO.scanForPlugins();
        tessInst = new Tesseract();
        tessInst.setDatapath("G:\\Documents\\GitHub\\OCR-Medical-Records\\medical-records-web-app\\tess\\tessdata");

        // disable dictionaries
        tessInst.setTessVariable("load_freq_dawg", "false");
        tessInst.setTessVariable("load_system_dawg", "false");
        tessInst.setTessVariable("user_patterns_suffix", "user-patterns");

        // reset the character set accepted
        tessInst.setTessVariable("tessedit_char_whitelist", "md0123456789.,");
        tessInst.setPageSegMode(SINGLE_LINE_SEGMENTATION_MODE);

        handlerInst = this;
    }

    public static CharacterRecognitionHandler getInstance(){
        if(handlerInst == null) {
            new CharacterRecognitionHandler();
        }

        return handlerInst;
    }

    public String doOCR(File file, Rectangle cell){
        return doOCR(file, cell, false);
    }

    public String doOCR(File file, Rectangle cell, boolean acceptLetters){
        if (acceptLetters) {
            // reset the character set accepted
            tessInst.setTessVariable("tessedit_char_whitelist", "md0123456789.,");
        } else {
            // reset the character set accepted
            tessInst.setTessVariable("tessedit_char_whitelist", "0123456789.,");
        }

        try {
            Log.error(cell.toString());
            return tessInst.doOCR(file, cell);
        } catch (Exception e) {
            Log.error("Error while performing OCR operation.");
            return null;
        }
    }
}