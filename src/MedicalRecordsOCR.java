import data.Entry;
import data.Records;
import image.processing.ImgProcessor;
import ocr.CharacterRecognitionHandler;
import org.opencv.core.Core;
import tools.Log;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class MedicalRecordsOCR {

    // Compulsory
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private static File medicalRecordFile;

    public static void main(String[] args) {
        if(args.length != 1 && args.length != 2){
            Log.error("Please enter filename.");
            return;
        }

        String filePath = args[0];
        medicalRecordFile = new File(filePath);

        if(!medicalRecordFile.exists()) {
            Log.error("The specified file could not be opened.");
            return;
        }

        ImgProcessor imgProcessor = new ImgProcessor(filePath);
        ArrayList<Rectangle> cells = imgProcessor.divideTableIntoCells();

        if(cells == null) {
            Log.error("The specified file could not be processed.");
            return;
        }

        Records patientRecords = new Records();
        if (args.length==2) {
            patientRecords = new Records(args[1]);
        }

        String cellText = "";
        for (int i = 0; i < cells.size();) {
            try {
                String date = CharacterRecognitionHandler.getInstance().doOCR(medicalRecordFile, cells.get(i++));

                cellText = CharacterRecognitionHandler.getInstance().doOCR(medicalRecordFile, cells.get(i++));
                int age = Integer.parseInt(cellText);

                cellText = CharacterRecognitionHandler.getInstance().doOCR(medicalRecordFile, cells.get(i++));
                int weight = Integer.parseInt(cellText);

                cellText = CharacterRecognitionHandler.getInstance().doOCR(medicalRecordFile, cells.get(i++));
                int cephalicPerimeter = Integer.parseInt(cellText);

                patientRecords.addRecordEntry(new Entry(date, age, weight, cephalicPerimeter));
            } catch (Exception e) {
                e.printStackTrace();
                Log.error("An error occurred while analysing the file.");
            }
        }

        Log.detail(patientRecords.toString());
    }
}
