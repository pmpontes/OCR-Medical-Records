import image.processing.ImgProcessor;
import org.opencv.core.Core;

public class Main {

    // Compulsory
    static{ System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }

    public static void main(String[] args) {
        /*if(args.length != 1){
            Log.error("Please enter filename.");
            return;
        }*/

        ImgProcessor img = new ImgProcessor("test files/table2.jpg"); // for test purposes
        img.divideTableIntoCells();
    }
}
