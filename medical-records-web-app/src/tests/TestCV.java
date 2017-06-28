package tests;

import image.processing.ImgProcessor;
import tools.Log;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by pedro on 28/06/2017.
 */
public class TestCV {

    public static void main(String args[]){
        ImgProcessor processor = new ImgProcessor("G:\\Documents\\GitHub\\OCR-Medical-Records\\test files\\final.jpg");

        ArrayList<Rectangle> result = processor.divideTableIntoCells();

        Log.detail("Array has: " + result.size() + " elements");
        return;
    }
}
