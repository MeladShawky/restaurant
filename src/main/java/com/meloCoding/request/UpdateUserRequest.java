package com.meloCoding.request;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String address;
    private String phone;
}
    
