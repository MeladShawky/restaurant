package com.meloCoding.dream_shops.services.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.meloCoding.dream_shops.dto.ImageDto;
import com.meloCoding.dream_shops.models.Image;

public interface IImageService {
    Image getImageById(Long id);

    void deleteImageById(Long id);

    List<ImageDto> saveImages(List<MultipartFile> files, Long productId);

    void updateImage(MultipartFile file, Long imageId);
}
