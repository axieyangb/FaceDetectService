package com.jerryxie.detection.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jerryxie.detection.service.FaceDetectorService;

@RestController
@RequestMapping("/")
public class HomeController {

	@Autowired
	FaceDetectorService faceDetector;
	private Logger logger = Logger.getLogger(HomeController.class);

	@RequestMapping(value = "facenum", produces = "application/json", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Integer> detectFace(
			@RequestParam(name = "image", required = true) @RequestPart("image") MultipartFile file) {
		File img = convertToFile(file);
		int faceNum = faceDetector.getFaceNum(img);
		if (img.exists()) {
			img.delete();
		}
		return new ResponseEntity<>(faceNum, HttpStatus.OK);
	}

	private File convertToFile(MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
		try {
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
			fos.write(file.getBytes());
			fos.close();
		} catch (IOException e) {
			logger.error(e);
		}

		return convFile;
	}

}
