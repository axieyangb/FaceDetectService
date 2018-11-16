package com.jerryxie.detection.configuration;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class OpenCVConfiguration {

    private File configFile;
    private Logger logger = Logger.getLogger(OpenCVConfiguration.class);

    @Bean
    public CascadeClassifier getCascadeClassifier() {
        CascadeClassifier classifier = new CascadeClassifier();
        try {
            configFile = new ClassPathResource("haarcascade_frontalface_alt.xml").getFile();
            if (!configFile.exists()) {
                logger.error(configFile.getName() + " Not Existed !");
            }
        } catch (IOException e) {
            logger.error(e);
        }
        boolean loadSuccess = classifier.load(configFile.getAbsolutePath());
        if (!loadSuccess) {
            logger.error("CascadeClassifier load failed");
        }
        return classifier;

    }
}
