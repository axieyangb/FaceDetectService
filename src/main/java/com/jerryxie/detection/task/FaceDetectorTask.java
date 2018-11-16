package com.jerryxie.detection.task;

import java.io.File;
import java.util.concurrent.Callable;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class FaceDetectorTask implements Callable<Integer> {
    CascadeClassifier detector;
    String fileName;

    public FaceDetectorTask(CascadeClassifier detector, String fileName) {
        this.detector = detector;
        this.fileName = fileName;
    }

    @Override
    public Integer call() throws Exception {
        File imgFile = new File(this.fileName);
        if (!imgFile.exists()) {
            return -1;
        }
        return faceNumber(imgFile);
    }

    private int faceNumber(File image) {
        Mat frame = Highgui.imread(image.getAbsolutePath());
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
        this.detector.detectMultiScale(grayFrame, faces, 1.1, 2, 0 | Objdetect.CASCADE_SCALE_IMAGE,
                new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        return faces.toArray().length;
    }

}
