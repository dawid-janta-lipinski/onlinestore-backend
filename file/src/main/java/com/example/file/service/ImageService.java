package com.example.file.service;


import com.example.file.dao.ImageDao;
import com.example.file.mediator.ImageMediator;
import com.example.file.model.ImageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.file.exceptions.ImageNotFoundException;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageDao imageDao;
    private final ImageMediator imageMediator;

    public void saveImage(ImageEntity image) {
        imageDao.saveAndFlush(image);
    }

    public ImageEntity findImageByUuid(String uuid) throws ImageNotFoundException {
        ImageEntity image = imageDao.findImageEntitiesByUuid(uuid).orElse(null);

        if (image == null) throw new ImageNotFoundException("Image not found");

        return image;
    }

    public void activateImage(String uuid) throws ImageNotFoundException {
        ImageEntity image = findImageByUuid(uuid);
        image.setUsed(true);
        saveImage(image);
    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanImages(){
        imageDao.findUnusedImages().forEach(image -> {
            try {
                imageMediator.deleteFileFromFTP(image.getPath());
                imageDao.delete(image);
            } catch (IOException e){
                log.info("Cannot delete "+ image.getUuid());
            }
        });
    }
}
