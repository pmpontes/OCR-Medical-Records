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

The user must run the server, access the Medical Records OCR web page and select a file corresponding to a child's growth table previously scanned. This image must be at the location specified in MedicalRecordsOCR.FILES\_LOCATION and be either a .PNG, .JPG or PDF.

Upon receiving the request, the application proceeds to analyze the image selected. When this process finishes, the data generated will then presented in a table, displayed in a new web page.

It should be noted that, for efficiency purposes, the analysis process halts when a row is signaled as being empty, and the following rows are not processed.
