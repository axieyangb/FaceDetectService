# FaceDetectionService

A Restful Service for face detection. The application is build based on the spring boot framework.

## How to start
```
mvn clean install
java -jar target/FaceDetectionService-0.0.1-SNAPSHOT.jar

```

That's it, at this moment your application is running at port 8004


## Call the service
 You can start test your api using the following command.
```
 curl -X POST http://localhost:8004/facenum -F image=@<image-path>

```
It returns the number of faces detected in the picture
