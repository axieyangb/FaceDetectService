package com.jerryxie.detection.service;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class FaceDetector {

    private CascadeClassifier faceDetector;
    File configFile;
    private Logger logger = Logger.getLogger(FaceDetector.class);

    public FaceDetector() {
        nu.pattern.OpenCV.loadLibrary();
        init();
    }

    private void init() {
        faceDetector = new CascadeClassifier();
        try {
            configFile = new ClassPathResource("haarcascade_frontalface_alt.xml").getFile();
            if (!configFile.exists()) {
                logger.error(configFile.getName() + " Not Existed !");
            }
        } catch (IOException e) {
            logger.error(e);
        }
        boolean loadSuccess = faceDetector.load(configFile.getAbsolutePath());
        System.out.println("Load status : " + loadSuccess);
    }

    public int faceNumber(File image) {
        Mat frame = Highgui.imread(image.getAbsolutePath());
        if (frame.empty()) {
            return -1;
        }
        MatOfRect faces = new MatOfRect();
        Mat grayFrame = new Mat();
        // convert the frame in gray scale
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
        // equalize the frame histogram to improve the result
        Imgproc.equalizeHist(grayFrame, grayFrame);
        int absoluteFaceSize = 0;
        int height = grayFrame.rows();
        if (Math.round(height * 0.2f) > 0) {
            absoluteFaceSize = Math.round(height * 0.2f);
        }
        // detect faces
        this.faceDetector.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        return faces.toArray().length;
    }
}
