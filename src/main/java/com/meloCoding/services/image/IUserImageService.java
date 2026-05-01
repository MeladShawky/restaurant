package com.meloCoding.services.image;

import org.springframework.web.multipart.MultipartFile;

import com.meloCoding.dto.UserImageDto;
import com.meloCoding.models.UserImage;

public interface IUserImageService {
    UserImage getImageById(Long id);

    UserImage getImageByUserId(Long userId);

    UserImageDto saveImage(MultipartFile file, Long userId);

    void updateImage(MultipartFile file, Long userId);

    void deleteImageByUserId(Long userId);
}
