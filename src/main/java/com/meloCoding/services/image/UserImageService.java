package com.meloCoding.services.image;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.meloCoding.dto.UserImageDto;
import com.meloCoding.exceptions.ResourceNotFoundException;
import com.meloCoding.models.User;
import com.meloCoding.models.UserImage;
import com.meloCoding.repository.UserImageRepository;
import com.meloCoding.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserImageService implements IUserImageService {

    private final UserImageRepository userImageRepository;
    private final UserRepository userRepository;

    @Override
    public UserImage getImageById(Long id) {
        return userImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    public UserImage getImageByUserId(Long userId) {
        return userImageRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No profile image found for user: " + userId));
    }

    @Override
    public UserImageDto saveImage(MultipartFile file, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Delete existing profile image if one exists
        userImageRepository.findByUserId(userId).ifPresent(userImageRepository::delete);

        try {
            UserImage userImage = new UserImage();
            userImage.setFileName(file.getOriginalFilename());
            userImage.setFileType(file.getContentType());
            userImage.setImage(new SerialBlob(file.getBytes()));
            userImage.setUser(user);

            String buildDownloadUrl = "/api/v1/user-images/image/download/";

            UserImage savedImage = userImageRepository.save(userImage);
            savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
            userImageRepository.save(savedImage);

            UserImageDto imageDto = new UserImageDto();
            imageDto.setImageId(savedImage.getId());
            imageDto.setImageName(savedImage.getFileName());
            imageDto.setDownloadUrl(savedImage.getDownloadUrl());
            return imageDto;
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateImage(MultipartFile file, Long userId) {
        UserImage userImage = getImageByUserId(userId);
        try {
            userImage.setFileName(file.getOriginalFilename());
            userImage.setFileType(file.getContentType());
            userImage.setImage(new SerialBlob(file.getBytes()));
            userImageRepository.save(userImage);
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void deleteImageByUserId(Long userId) {
        userImageRepository.findByUserId(userId).ifPresentOrElse(userImageRepository::delete, () -> {
            throw new ResourceNotFoundException("No profile image found for user: " + userId);
        });
    }
}
