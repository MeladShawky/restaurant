package com.meloCoding.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
    private UserImageDto profileImage;
}
