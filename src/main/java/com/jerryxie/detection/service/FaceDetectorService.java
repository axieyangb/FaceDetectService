package com.jerryxie.detection.service;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.jerryxie.detection.task.FaceDetectorTask;

@Service
public class FaceDetectorService {

    @Autowired
    CascadeClassifier classifier;

    @Autowired
    ApplicationContext context;

    private final int defaultThreadPoolSize = 50;
    private Logger logger = Logger.getLogger(FaceDetectorService.class);
    private ThreadPoolTaskExecutor executor;

    public FaceDetectorService() {
        nu.pattern.OpenCV.loadLibrary();
        this.executor = loadThreadPool();

    }

    private ThreadPoolTaskExecutor loadThreadPool() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(defaultThreadPoolSize);
        pool.initialize();
        return pool;
    }

    public int getFaceNum(String fileName) {
        Future<Integer> result = this.executor
                .submit(this.context.getBean(FaceDetectorTask.class, this.classifier, fileName));
        try {
            return result.get().intValue();
        } catch (InterruptedException e) {
            logger.error(e);
        } catch (ExecutionException e) {
            logger.error(e);
        }
        return -1;
    }

    public int getFaceNum(File imgFile) {
        return getFaceNum(imgFile.getAbsolutePath());
    }

}
