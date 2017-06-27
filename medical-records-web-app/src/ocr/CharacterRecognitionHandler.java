package ocr;

import java.awt.*;
import java.io.File;

import net.sourceforge.tess4j.*;
import tools.Log;

import javax.imageio.ImageIO;

public class CharacterRecognitionHandler {

    private static CharacterRecognitionHandler handlerInst = null;
    private static Tesseract tessInst = null;

    private CharacterRecognitionHandler(){
        ImageIO.scanForPlugins();
        tessInst = new Tesseract();
        tessInst.setDatapath("G:\\Documents\\GitHub\\OCR-Medical-Records\\medical-records-web-app\\tess\\tessdata");

        // disable dictionaries
        tessInst.setTessVariable("load_freq_dawg", "false");
        tessInst.setTessVariable("load_system_dawg", "false");

        // reset the character set accepted
        tessInst.setTessVariable("tessedit_char_whitelist", "0123456789-.,/");

        handlerInst = this;
    }

    public static CharacterRecognitionHandler getInstance(){
        if(handlerInst == null) {
            new CharacterRecognitionHandler();
        }

        return handlerInst;
    }

    public String doOCR(String filePath){
        try {
            File file = new File(filePath);
            String result= tessInst.doOCR(file);
            return result;
        } catch (TesseractException e) {
            Log.error("Error while performing OCR operation on file " + filePath);
            return null;
        }
    }

    public String doOCR(File file, Rectangle cell){
        try {
            Log.info("doOCR");
            Log.info(cell.toString());
            String result= tessInst.doOCR(file, cell);
            Log.info("done OCR " + result);
            return result;
        } catch (Exception e) {
            Log.error("Error while performing OCR operation on file " + file.getName());
            return null;
        }
    }
}