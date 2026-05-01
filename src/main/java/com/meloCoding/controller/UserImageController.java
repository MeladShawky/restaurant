package com.meloCoding.controller;

import java.sql.SQLException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.meloCoding.dto.UserImageDto;
import com.meloCoding.exceptions.ResourceNotFoundException;
import com.meloCoding.models.UserImage;
import com.meloCoding.response.ApiResponse;
import com.meloCoding.services.image.IUserImageService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/user-images")
public class UserImageController {

    private final IUserImageService userImageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> uploadProfileImage(
            @RequestParam MultipartFile file,
            @RequestParam Long userId) {
        try {
            UserImageDto imageDto = userImageService.saveImage(file, userId);
            return ResponseEntity.ok(new ApiResponse("Profile image uploaded successfully", imageDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Failed to upload profile image", e.getMessage()));
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<ByteArrayResource> downloadImage(@PathVariable Long imageId) throws SQLException {
        UserImage image = userImageService.getImageById(imageId);
        ByteArrayResource resource = new ByteArrayResource(
                image.getImage().getBytes(1, (int) image.getImage().length()));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getFileName() + "\"")
                .body(resource);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getProfileImage(@PathVariable Long userId) {
        try {
            UserImage image = userImageService.getImageByUserId(userId);
            UserImageDto imageDto = new UserImageDto();
            imageDto.setImageId(image.getId());
            imageDto.setImageName(image.getFileName());
            imageDto.setDownloadUrl(image.getDownloadUrl());
            return ResponseEntity.ok(new ApiResponse("Profile image fetched successfully", imageDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/user/{userId}/update")
    public ResponseEntity<ApiResponse> updateProfileImage(
            @PathVariable Long userId,
            @RequestParam MultipartFile file) {
        try {
            userImageService.updateImage(file, userId);
            return ResponseEntity.ok(new ApiResponse("Profile image updated successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/user/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteProfileImage(@PathVariable Long userId) {
        try {
            userImageService.deleteImageByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Profile image deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
