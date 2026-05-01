package com.meloCoding.services.User;

import com.meloCoding.dto.UserDto;
import com.meloCoding.models.User;
import com.meloCoding.request.CreateUserRequest;
import com.meloCoding.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User getAuthenticatedUser();
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);
    UserDto convertUserToDto(User user);
}
