package image.processing;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import tools.Log;

import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.add;
import static org.opencv.core.Core.bitwise_and;
import static org.opencv.core.Core.bitwise_not;
import static org.opencv.imgcodecs.Imgcodecs.*;
import static org.opencv.imgproc.Imgproc.*;

public class ImgProcessor {

    private String fileName = "table.jpg";
    private Mat originalImg = null;
    private Mat processedImg = new Mat();

    public ImgProcessor(String fileName) {
        // Load originalImg image
        this.fileName = fileName;
        originalImg = imread(fileName);

        // TODO check if the test condition is valid (different from c++ implementation)
        // Check if image is loaded fine
        if(originalImg.empty()){
            Log.error("Error loading image.");
        }

        // TODO check if necessary
        // resizing for practical reasons
        resize(originalImg, processedImg, new Size(800, 900));
    }

    private void createBinaryImage(){
        // Transform originalImg image to gray if it is not
        Mat gray = new Mat();

        if (processedImg.channels() == 3) {
            cvtColor(processedImg, gray, Imgproc.COLOR_BGR2GRAY);
        }
        else {
            gray = processedImg;
        }

        // Apply adaptiveThreshold at the bitwise_not of gray (btwn)
        Mat btwn = new Mat(), bw = new Mat();
        bitwise_not(gray, btwn);
        adaptiveThreshold(bw, btwn, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 15, -2);

        processedImg = bw;
    }

    private Mat exctractHorizontalLines(){
        // Create the image that will use to extract the horizontal lines
        Mat horizontal = processedImg.clone();

        // TODO
        int scale = 15; // play with this variable in order to increase/decrease the amount of lines to be detected

        // Specify size on horizontal axis
        int horizontalsize = horizontal.cols() / scale;

        // Create structure element for extracting horizontal lines through morphology operations
        Mat horizontalStructure = getStructuringElement(MORPH_RECT, new Size(horizontalsize,1));

        // Apply morphology operations
        erode(horizontal, horizontal, horizontalStructure, new Point(-1, -1), 100); // TODO check iteration number
        dilate(horizontal, horizontal, horizontalStructure, new Point(-1, -1), 100);

        return horizontal;
    }

    private Mat extractVerticalLines(){
        // Create the image that will use to extract the horizontal lines
        Mat vertical = processedImg.clone();

        // TODO
        int scale = 15; // play with this variable in order to increase/decrease the amount of lines to be detected

        // Specify size on vertical axis
        int verticalsize = vertical.rows() / scale;

        // Create structure element for extracting vertical lines through morphology operations
        Mat verticalStructure = getStructuringElement(MORPH_RECT, new Size( 1,verticalsize));

        // Apply morphology operations
        erode(vertical, vertical, verticalStructure, new Point(-1, -1), 100); // TODO check iteration number
        dilate(vertical, vertical, verticalStructure, new Point(-1, -1), 100);

        return vertical;
    }

    public Mat findCountours() {
        Mat horizontal = exctractHorizontalLines();
        Mat vertical = extractVerticalLines();

        // create a mask which includes the tables
        Mat mask = new Mat();
        add(horizontal, vertical, mask);

        // find the joints between the lines of the tables, we will use this information in order to discriminate tables from pictures (tables will contain more than 4 joints while a picture only 4 (i.e. at the corners))
        Mat joints = new Mat();
        bitwise_and(horizontal, vertical, joints);

        ///////////TODO test everything from here on
        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        findContours(mask, contours, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE, new Point(0, 0));

        List<List<Point>> contours_poly = new ArrayList<>(contours.size());
        List<Rect> boundRect = new ArrayList<>(contours.size());
        List<Mat> rois = new ArrayList<>();

        for (int i = 0; i < contours.size(); i++)
        {
            // find the area of each contour
            double area = contourArea(contours.get(i));

            // filter individual lines of blobs that might exist and they do not represent a table
            if(area < 100) // value is randomly chosen, you will need to find that by yourself with trial and error procedure
                continue;

            approxPolyDP(new MatOfPoint2f(contours.get(i)), new MatOfPoint2f(contours_poly.get(i).toArray()), 3.0, true);
            boundRect.set(i, boundingRect(new Mat(contours_poly[i])));

            // find the number of joints that each table has
            Mat roi = joints(boundRect[i]);

            vector<vector<Point> > joints_contours;
            findContours(roi, joints_contours, RETR_CCOMP, CHAIN_APPROX_SIMPLE);

            // if the number is not more than 5 then most likely it not a table
            if(joints_contours.size() <= 4)
                continue;

            rois.push_back(rsz(boundRect[i]).clone());

//        drawContours( rsz, contours, i, Scalar(0, 0, 255), CV_FILLED, 8, vector<Vec4i>(), 0, Point() );
            rectangle( rsz, boundRect[i].tl(), boundRect[i].br(), Scalar(0, 255, 0), 1, 8, 0 );
        }

        for(size_t i = 0; i < rois.size(); ++i)
        {
        /* Now you can do whatever post process you want
         * with the data within the rectangles/tables. */
            imshow("roi", rois[i]);
            waitKey();
        }

        return joints;
    }




}