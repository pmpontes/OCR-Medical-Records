package image.processing;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import tools.Log;

import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.opencv.core.Core.add;
import static org.opencv.core.Core.bitwise_and;
import static org.opencv.core.Core.bitwise_not;
import static org.opencv.imgcodecs.Imgcodecs.*;
import static org.opencv.imgproc.Imgproc.*;
import static org.opencv.core.Point.*;
import static org.opencv.core.CvType.*;

public class ImgProcessor {

    private static final int VERTEX_MINIMUM_DISTANCE = 4;

    private Mat originalImg = null;
    private Mat processedImg = new Mat();

    private ArrayList<Rectangle> cells;

    public ImgProcessor(String fileName) {
        // Load originalImg image
        originalImg = imread(fileName);
        cells = null;

        // Check if image is loaded fine
        if(originalImg.empty()){
            Log.error("Error loading image.");
        }

        // resizing for practical reasons
        resize(originalImg, processedImg, new Size(2500, 3500));

        //Log.showResult(processedImg.clone());
    }

    private void createBinaryImage(){
        // Transform originalImg image to gray if it is not
        Mat gray = new Mat();

        if (processedImg.channels() == 3) {
            cvtColor(processedImg, gray, COLOR_BGR2GRAY);
        }
        else {
            gray = processedImg.clone();
        }

        // Apply adaptiveThreshold at the bitwise_not of gray (btwn)
        Mat btwn = new Mat(), bw = new Mat();
        bitwise_not(gray, btwn);

        // Block size 103 works well with the example provided, but may be overtrained
        adaptiveThreshold(btwn, bw, 255, ADAPTIVE_THRESH_MEAN_C, THRESH_BINARY, 101, -2);

        processedImg = bw.clone();
    }

    private Mat exctractHorizontalLines(){
        // Create the image that will use to extract the horizontal lines
        Mat horizontal = processedImg.clone();

        // This value works well with the example provided
        int scale = 3; // play with this variable in order to increase/decrease the amount of lines to be detected

        // Specify size on horizontal axis
        int horizontalsize = horizontal.cols() / scale;

        // Create structure element for extracting horizontal lines through morphology operations
        Mat horizontalStructure = getStructuringElement(MORPH_RECT, new Size(horizontalsize,1));

        // Apply morphology operations
        erode(horizontal.clone(), horizontal, horizontalStructure);
        dilate(horizontal.clone(), horizontal, horizontalStructure);
        //erode(vertical, vertical, verticalStructure, new Point(-1, -1), 100); // TODO check if the Point is reall not necessary
        //dilate(vertical, vertical, verticalStructure, new Point(-1, -1), 100);

        return horizontal;
    }

    private Mat extractVerticalLines(){
        // Create the image that will use to extract the horizontal lines
        Mat vertical = processedImg.clone();

        // This value works well with the example provided
        int scale = 3; // play with this variable in order to increase/decrease the amount of lines to be detected

        // Specify size on vertical axis
        int verticalsize = vertical.rows() / scale;

        // Create structure element for extracting vertical lines through morphology operations
        Mat verticalStructure = getStructuringElement(MORPH_RECT, new Size( 1,verticalsize));

        // Apply morphology operations
        erode(vertical, vertical, verticalStructure);
        dilate(vertical, vertical, verticalStructure);

        return vertical;
    }

    private List<Point> extractCellEdges(Mat horizontal, Mat vertical) {
        return null;
    }

    public Mat findContours() {
        createBinaryImage();

        Mat horizontal = exctractHorizontalLines();
        Mat vertical = extractVerticalLines();

        // create a mask which includes the tables
        Mat mask = new Mat();
        add(horizontal, vertical, mask);

        Log.showResult(processedImg);
        Log.showResult(mask);

        // find the joints between the lines of the tables, we will use this information in order to discriminate tables from pictures (tables will contain more than 4 joints while a picture only 4 (i.e. at the corners))
        Mat jointPoints = new Mat();
        bitwise_and(horizontal, vertical, jointPoints);

        Log.showResult(jointPoints);

        this.cells = processVertexes(jointPoints);

        return null;
    }

    public ArrayList<Rectangle> processVertexes(Mat original) {
        boolean foundVertex = false;
        int currDistanceX = 0;
        int currDistanceY = 0;
        int colNum = 0;
        int rowNum = 0;

        Mat topLeft = new Mat();
        Mat topRight = new Mat();
        Mat bottomLeft = new Mat();
        Mat bottomRight = new Mat();

        Imgproc.filter2D(original, topRight,    -1, kernelGenerator(1));
        Imgproc.filter2D(original, topLeft,     -1, kernelGenerator(2));
        Imgproc.filter2D(original, bottomLeft,  -1, kernelGenerator(3));
        Imgproc.filter2D(original, bottomRight, -1, kernelGenerator(4));

        Mat combinedMat =  topRight.clone();
        add(combinedMat, topLeft, combinedMat);
        add(combinedMat, bottomRight, combinedMat);
        add(combinedMat, bottomLeft, combinedMat);

        Log.showResult(combinedMat);

        int threshold = 1;

        ArrayList<Point> topRightCorners = highPassFilter(topRight, threshold);
        ArrayList<Point> topLeftCorners = highPassFilter(topLeft, threshold);
        ArrayList<Point> bottomRightCorners = highPassFilter(bottomLeft, threshold);
        ArrayList<Point> bottomLeftCorners = highPassFilter(bottomRight, threshold);

        compensateMissingPoints(topRightCorners, topLeftCorners, bottomRightCorners, bottomLeftCorners);

        topLeftCorners = orderVertexLists(5, topLeftCorners);
        topRightCorners = orderVertexLists(5, topRightCorners);
        bottomLeftCorners = orderVertexLists(5, bottomLeftCorners);
        bottomRightCorners = orderVertexLists(5, bottomRightCorners);

        ArrayList<Rectangle> areasOfInterest = retrieveCells(topRightCorners, topLeftCorners,
                                                                    bottomRightCorners, bottomLeftCorners);

        return areasOfInterest;
    }

    private Mat kernelGenerator(int quadrant) {
        Mat result = new Mat(3, 3, CV_32F);
        int negativeWeight = -10;
        int positiveWeight = 0;

        result.put(1,1,3);
        switch(quadrant) {
            case 1:
                result.put(0,0,negativeWeight);
                result.put(0,1,negativeWeight);
                result.put(0,2,negativeWeight);
                result.put(1,2,negativeWeight);
                result.put(2,2,negativeWeight);
                result.put(1,0,positiveWeight);
                result.put(2,0,positiveWeight);
                result.put(2,1,positiveWeight);
                break;
            case 2:
                result.put(0,0,negativeWeight);
                result.put(0,1,negativeWeight);
                result.put(0,2,negativeWeight);
                result.put(1,0,negativeWeight);
                result.put(2,0,negativeWeight);
                result.put(1,2,positiveWeight);
                result.put(2,1,positiveWeight);
                result.put(2,2,positiveWeight);
                break;
            case 3:
                result.put(2,0,negativeWeight);
                result.put(2,1,negativeWeight);
                result.put(2,2,negativeWeight);
                result.put(0,0,negativeWeight);
                result.put(1,0,negativeWeight);
                result.put(1,2,positiveWeight);
                result.put(0,1,positiveWeight);
                result.put(0,2,positiveWeight);
                break;
            case 4:
                result.put(2,1,negativeWeight);
                result.put(2,0,negativeWeight);
                result.put(0,2,negativeWeight);
                result.put(1,2,negativeWeight);
                result.put(2,2,negativeWeight);
                result.put(0,0,positiveWeight);
                result.put(0,1,positiveWeight);
                result.put(1,0,positiveWeight);
                break;
            default:
                break;

        }

        return result;
    }

    private ArrayList<Point> highPassFilter(Mat image, int threshold) {
        ArrayList<Point> result = new ArrayList<>();

        for(int y = 0; y < image.rows(); y++)
            for(int x = 0; x < image.cols(); x++)
                if(image.get(y, x)[0] > threshold) result.add(new Point(x, y));

        return result;
    }


    /**
     * Checks 4 lists of points and analyzes if there is any point missing from the same index by checking proximity
     * @return boolean number of missing points that were inserted into the lists
     */
    private int compensateMissingPoints(ArrayList<Point>topRightCorners, ArrayList<Point>topLeftCorners,
                                        ArrayList<Point>bottomRightCorners, ArrayList<Point> bottomLeftCorners) {
        int changes = 0;
        Point missing = null;

        int size = Math.max(Math.max(Math.max(topLeftCorners.size(), topRightCorners.size()), bottomLeftCorners.size()), bottomRightCorners.size());

        for(int i = 0; i < size; i++) {

            if(i >= topLeftCorners.size() || i >= topRightCorners.size() ||
                i >= bottomLeftCorners.size() || i >= bottomRightCorners.size()){

                if(i => topLeftCorners.size()){
                    Point upperRight = topRightCorners.get(i);
                    Point bottomLeft = bottomLeftCorners.get(i);
                    Point bottomRight = bottomRightCorners.get(i);

                    missing = new Point(bottomLeft.x, upperRight.y);
                    topLeftCorners.add(i, missing);
                }
                if(i => topRightCorners.size()){
                    Point upperLeft = topLeftCorners.get(i);
                    Point bottomLeft = bottomLeftCorners.get(i);
                    Point bottomRight = bottomRightCorners.get(i);
                    missing = new Point(bottomRight.x, upperLeft.y);

                    topRightCorners.add(i, missing);
                }
                if(i => bottomLeftCorners.size()){
                    Point upperLeft = topLeftCorners.get(i);
                    Point upperRight = topRightCorners.get(i);
                    Point bottomRight = bottomRightCorners.get(i);

                    missing = new Point(upperLeft.x, bottomRight.y);
                    bottomLeftCorners.add(i, missing);
                }
                if(i => bottomRightCorners.size()){
                    Point upperLeft = topLeftCorners.get(i);
                    Point upperRight = topRightCorners.get(i);
                    Point bottomLeft = bottomLeftCorners.get(i);

                    missing = new Point(upperRight.x, bottomLeft.y);
                    bottomRightCorners.add(i, missing);
                }

                continue;
            }

            Point upperLeft = topLeftCorners.get(i);
            Point upperRight = topRightCorners.get(i);
            Point bottomLeft = bottomLeftCorners.get(i);
            Point bottomRight = bottomRightCorners.get(i);



            double range = 15; // pixel range

            double diff_UL_UR = Math.abs(upperLeft.x - upperRight.x) + Math.abs(upperLeft.y - upperRight.y);
            double diff_BR_UR = Math.abs(bottomRight.x - upperRight.x) + Math.abs(bottomRight.y - upperRight.y);
            double diff_UL_BL = Math.abs(upperLeft.x - bottomLeft.x) + Math.abs(upperLeft.y - bottomLeft.y);
            double diff_BL_BR = Math.abs(bottomLeft.x - bottomRight.x) + Math.abs(bottomLeft.y - bottomRight.y);

            if(diff_BL_BR > range && diff_UL_BL > range) {
                // Bottom left point is incorrect, create new
                missing = new Point(upperLeft.x, bottomRight.y);
                bottomLeftCorners.add(i, missing);
            }
            if(diff_BL_BR > range && diff_BR_UR > range) {
                // Bottom right point is incorrect, create new
                missing = new Point(upperRight.x, bottomLeft.y);
                bottomRightCorners.add(i, missing);
            }
            if(diff_UL_UR > range && diff_UL_BL > range) {
                // Upper left point is incorrect, create new
                missing = new Point(bottomLeft.x, upperRight.y);
                topLeftCorners.add(i, missing);
            }
            if(diff_BL_BR > range && diff_UL_BL > range) {
                // Upper right point is incorrect, create new
                missing = new Point(bottomRight.x, upperLeft.y);
                topRightCorners.add(i, missing);
            }
        }

        return changes;
    }

    private ArrayList<Rectangle> retrieveCells(ArrayList<Point>topRightCorners, ArrayList<Point>topLeftCorners,
                                                      ArrayList<Point>bottomRightCorners, ArrayList<Point> bottomLeftCorners) {

        ArrayList<Rectangle> result = new ArrayList<Rectangle>();

        if(topLeftCorners.size() != topRightCorners.size() ||
                topLeftCorners.size() != bottomRightCorners.size() ||
                topLeftCorners.size() != bottomLeftCorners.size()){
            return null;
        }

        double xDiff = 0; // difference in x values between the previous point and the current one
        Rectangle temp = null;

        boolean starter = true;
        boolean toggleDetection = true;
        int dotWidth = 0;

        for( int i = 0; i < topLeftCorners.size(); i++ ) {
            if(i == 0)
                xDiff = -1;
            else
                xDiff = topLeftCorners.get(i).x - topLeftCorners.get(i-1).x;

            if(xDiff < 0 ) {
                if(i > 0 ) {
                    if(starter) {
                        starter = false;
                        dotWidth = i;
                    }
                }
                continue;
            }

            if(!starter) {
                double width = bottomLeftCorners.get(i-dotWidth).x - bottomRightCorners.get(i-dotWidth-1).x;

                double height = topRightCorners.get(i-1).y - bottomRightCorners.get(i-dotWidth-1).y;

                temp = new Rectangle((int) bottomRightCorners.get(i-dotWidth-1).x, (int) bottomRightCorners.get(i-dotWidth-1).y,
                        (int) width, (int) height);

                result.add(temp);
            }

        }

        return result;
    }

    private ArrayList<Point> orderVertexLists(double range, ArrayList<Point> vertexList) {

        ArrayList<Point> result = new ArrayList<Point>();

        ArrayList<Point> temp;

        Comparator<Point> pointXComparator = new Comparator<Point>(){
            @Override
            public int compare(final Point o1, final Point o2){
                int x1 = (int) o1.x;
                int x2 = (int) o2.x;
                return x1 - x2;
            }
        };

        while(!vertexList.isEmpty()) {
            final double height = vertexList.get(0).y;

            temp = vertexList.stream().filter(s -> Math.abs(s.y - height) < range).collect(Collectors.toCollection(ArrayList::new));

            temp.sort(pointXComparator);

            vertexList.removeAll(temp);

            result.addAll(temp);
        }

        return result;

    }


    public ArrayList<Rectangle> divideTableIntoCells() {
        findContours();

        return cells;
    }
}