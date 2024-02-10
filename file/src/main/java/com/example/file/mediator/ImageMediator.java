package com.example.file.mediator;

import com.example.file.exceptions.FtpConnectionException;
import com.example.file.exceptions.ImageNotFoundException;
import com.example.file.mapper.ImageMapper;
import com.example.file.model.ImageEntity;
import com.example.file.model.Response;
import com.example.file.service.FtpService;
import com.example.file.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ImageMediator {
    private final ImageService imageService;
    private final FtpService ftpService;
    private final ImageMapper imageMapper;

    public ResponseEntity<?> saveImage(MultipartFile file) {
        ImageEntity image;

        try {
            if (file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")+1).equals("png")){
                return ResponseEntity.status(400).body(new Response("Media type not supported. Use .png file."));
            }
            image = ftpService.uploadImageToFtp(file);
            imageService.saveImage(image);
            return ResponseEntity.ok(imageMapper.createImageDTOFromImageEntity(image));
        } catch (FtpConnectionException e){
            return ResponseEntity.status(400).body(new Response("Can't save file"));
        } catch (NullPointerException exception) {
            return ResponseEntity.status(400).body(new Response("Media type not supported. Use .png file."));
        }
    }

    public ResponseEntity<Response> deleteImage(String uuid) {
        boolean deleted;
        try {
            String imagePath = imageService.findImageByUuid(uuid).getPath();
            deleted = ftpService.deleteImage(imagePath);
        } catch (IOException e){
            return ResponseEntity.status(400).body(new Response("Connection issues. Can't delete file"));
        } catch (ImageNotFoundException ex){
            return ResponseEntity.status(400).body(new Response("This image doesn't exist"));
        }
        if(deleted) {
            return ResponseEntity.ok(new Response("Image successfully deleted."));
        } else {
            return ResponseEntity.ok(new Response("Something went wrong. We couldn't delete this image"));
        }
    }

    public ResponseEntity<?> getImage(String uuid) {
        ImageEntity image;
        try {
            image = imageService.findImageByUuid(uuid);
        } catch (ImageNotFoundException e) {
            return ResponseEntity.status(400).body(new Response("This image doesn't exist."));
        }
        ByteArrayOutputStream outputStream;
        try {
            outputStream = ftpService.getFile(image);
        } catch (IOException | FtpConnectionException e){
            return ResponseEntity.status(400).body(new Response("Connection issues. Can't download file"));
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

    public ResponseEntity<Response> activateImage(String uuid) {
        try {
            imageService.activateImage(uuid);
        } catch (ImageNotFoundException e){
            return ResponseEntity.status(400).body(new Response("This image doesn't exist."));
        }
        return ResponseEntity.ok(new Response("Image successfully activated."));
    }

    public void deleteFileFromFTP(String path) throws IOException {
        ftpService.deleteImage(path);
    }
}
