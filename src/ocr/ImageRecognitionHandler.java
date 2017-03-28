package ocr;

import java.io.File;
import net.sourceforge.tess4j.*;
import tools.Log;

public class ImageRecognitionHandler {

    private static ImageRecognitionHandler handlerInst = null;
    private static Tesseract tessInst = null;

    private ImageRecognitionHandler(){
        tessInst = new Tesseract();
        tessInst.setDatapath("tess\\tessdata");
        tessInst.setTessVariable("tessedit_char_whitelist", "AM0123456789-.,/"); // reset the character set accepted
    }

    public static ImageRecognitionHandler getInstance(){
        if(handlerInst == null){
            new ImageRecognitionHandler();
        }

        return handlerInst;
    }

    public String imageOCR(String filePath){
        try {
            File image = new File(filePath);
            String result= tessInst.doOCR(image);
            return result;
        } catch (TesseractException e) {
            Log.error("Error while performing OCR operation on file " + filePath);
            return null;
        }
    }
}