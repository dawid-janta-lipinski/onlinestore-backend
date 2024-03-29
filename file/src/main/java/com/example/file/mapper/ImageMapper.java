package com.example.file.mapper;

import com.example.file.model.ImageDTO;
import com.example.file.model.ImageEntity;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {
    public ImageDTO createImageDTOFromImageEntity(ImageEntity image){
        return ImageDTO.builder()
                .uuid(image.getUuid())
                .createdAt(image.getCreatedAt())
                .build();
    }
}
