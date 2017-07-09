# OCR-Medical-Records

## Summary

Developed within the scope of a Programming Paradigms course, this project envisioned the implementation of an OCR tool for digitizing medical records, using different programming languages and applying various programming paradigms.

More specifically, this tool should be able to perform OCR on the growth chart kept in a child's individual health record - with handwritten information about the child's height, weight and cephalic perimeter at a given date and age - and display its contents.

Our approach consisted in obtaining the information on a child's growth chart record, by processing a scanned image and performing OCR on each individual cell extracted from the growth chart.

## Getting Started

### Pre-requisites


* OpenCV;
* Tessaract;
* Tomcat.

Useful setup instructions:
* https://medium.com/@aadimator/how-to-set-up-opencv-in-intellij-idea-6eb103c1d45c
* http://www.jbrandsma.com/news/2015/12/07/ocr-with-java-and-tesseract/
* http://tess4j.sourceforge.net/tutorial/
* https://www.jetbrains.com/help/idea/2017.1/creating-and-running-your-first-web-application.html

### Usage

* Run the server;
* Select an image. This image must be at the location specified in MedicalRecordsOCR.FILES_LOCATION.
