package com.meloCoding.dto;

import lombok.Data;

@Data
public class UserImageDto {
    private Long imageId;
    private String imageName;
    private String downloadUrl;
}
