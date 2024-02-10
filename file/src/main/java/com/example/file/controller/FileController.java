package com.example.file.controller;

import com.example.file.mediator.ImageMediator;
import com.example.file.model.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/image")
public class FileController {
    private ImageMediator imageMediator;

    @PostMapping
    public ResponseEntity<?> saveFile (@RequestBody MultipartFile multipartFile){
        return imageMediator.saveImage(multipartFile);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteFile(@RequestParam String uuid){
        return imageMediator.deleteImage(uuid);
    }

    @GetMapping
    public ResponseEntity<?> getFile(@RequestParam String uuid) {
        return imageMediator.getImage(uuid);
    }

    @PatchMapping
    public ResponseEntity<Response> activateImage(@RequestParam String uuid){
        return imageMediator.activateImage(uuid);
    }
}
