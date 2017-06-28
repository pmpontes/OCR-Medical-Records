package server;

import data.Entry;
import data.Records;
import image.processing.ImgProcessor;
import ocr.CharacterRecognitionHandler;
import org.json.simple.JSONObject;
import org.opencv.core.Core;
import tools.Log;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class MedicalRecordsOCR {

    private static final int N_ROWS = 18;
    private static final int N_COLUMNS = 5;

    // Compulsory
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    private static final String FILES_LOCATION = "G:\\Documents\\GitHub\\OCR-Medical-Records\\test files\\";
    private final String filePath;
    private File processedMedicalRecordFile;

    public MedicalRecordsOCR(String filePath) {
        this.filePath = FILES_LOCATION + filePath;

        File medicalRecordFile = new File(this.filePath);
        if (!medicalRecordFile.exists()) {
            Log.error("The specified file could not be opened: " + this.filePath);
        }
    }

    public JSONObject process() {
        ImgProcessor imgProcessor = new ImgProcessor(filePath);
        ArrayList<Rectangle> cells = imgProcessor.divideTableIntoCells();
        processedMedicalRecordFile = new File("G:\\Documents\\GitHub\\OCR-Medical-Records\\medical-records-web-app\\processed_img.jpg");

        Records patientRecords = new Records();

        if(cells == null || cells.size() < N_COLUMNS * N_ROWS) {
            Log.error("The specified file could not be processed.");
            return patientRecords.toJSON();
        }

        if (processColumn(0, cells, patientRecords)) {
            processColumn(1, cells, patientRecords);
        }

        return patientRecords.toJSON();
    }

    private boolean processColumn(int columnIndex, ArrayList<Rectangle> cells, Records patientRecords) {
        int i = columnIndex * N_COLUMNS;
        for (; i < cells.size(); i += N_COLUMNS * 2) {
            Entry newEntry = processEntry(cells, i);
            if (newEntry != null) {
                patientRecords.addRecordEntry(newEntry);
            } else {
                return false;
            }
        }

        return true;
    }

    private Entry processEntry(ArrayList<Rectangle> entryCells, int cellIndex) {
        boolean entryFilled;
        try {
            Log.error("index: " + cellIndex);

            String date = CharacterRecognitionHandler.getInstance().doOCR(processedMedicalRecordFile, entryCells.get(cellIndex++)).trim();
            entryFilled = !date.isEmpty();
            Log.detail(date);

            String age = CharacterRecognitionHandler.getInstance().doOCR(processedMedicalRecordFile, entryCells.get(cellIndex++), true).trim();
            entryFilled &= !age.isEmpty();
            Log.detail(age);

            String weight = CharacterRecognitionHandler.getInstance().doOCR(processedMedicalRecordFile, entryCells.get(cellIndex++)).replace(",", ".").replace(" ", "").trim();
            entryFilled &= !weight.isEmpty();
            Log.detail(weight);

            String height = CharacterRecognitionHandler.getInstance().doOCR(processedMedicalRecordFile, entryCells.get(cellIndex++)).replace(",", ".").replace(" ", "").trim();
            entryFilled &= !height.isEmpty();
            Log.detail(height);

            String cephalicPerimeter = CharacterRecognitionHandler.getInstance().doOCR(processedMedicalRecordFile, entryCells.get(cellIndex)).replace(",", ".").replace(" ", "").trim();
            entryFilled &= !cephalicPerimeter.isEmpty();
            Log.detail(cephalicPerimeter);

            return entryFilled ? new Entry(date, age, Float.parseFloat(weight), Float.parseFloat(height), Float.parseFloat(cephalicPerimeter)) : null;
        } catch (Exception e) {
            Log.error("An error occurred. Check input data.");
            return null;
        }
    }
}