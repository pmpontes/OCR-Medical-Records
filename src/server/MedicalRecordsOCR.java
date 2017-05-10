package server;

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

    private final String filePath;
    private final File medicalRecordFile;

    public MedicalRecordsOCR(String filePath) {
        this.filePath = filePath;
        medicalRecordFile = new File(filePath);

        if (!medicalRecordFile.exists()) {
            Log.error("The specified file could not be opened: " + filePath);
            return;
        }
    }

    public Records process() {
        ImgProcessor imgProcessor = new ImgProcessor(filePath);
        ArrayList<Rectangle> cells = imgProcessor.divideTableIntoCells();

        if(cells == null) {
            Log.error("The specified file could not be processed.");
            return null;
        }

        Records patientRecords = new Records();

        String cellText = "";
        for (int i = 0; i < cells.size();) {
            try {
                String date = CharacterRecognitionHandler.getInstance().doOCR(medicalRecordFile, cells.get(i++));

                String age = CharacterRecognitionHandler.getInstance().doOCR(medicalRecordFile, cells.get(i++));

                cellText = CharacterRecognitionHandler.getInstance().doOCR(medicalRecordFile, cells.get(i++));
                float weight = Float.parseFloat(cellText);

                cellText = CharacterRecognitionHandler.getInstance().doOCR(medicalRecordFile, cells.get(i++));
                float height = Float.parseFloat(cellText);

                cellText = CharacterRecognitionHandler.getInstance().doOCR(medicalRecordFile, cells.get(i++));
                float cephalicPerimeter = Float.parseFloat(cellText);

                patientRecords.addRecordEntry(new Entry(date, age, weight, height, cephalicPerimeter));
            } catch (Exception e) {
                Log.error("An error occurred while analysing the file.");
                e.printStackTrace();
            }
        }

        Log.detail(patientRecords.toString());
        return patientRecords;
    }
}