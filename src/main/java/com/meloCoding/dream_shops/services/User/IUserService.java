package com.meloCoding.dream_shops.services.User;

import com.meloCoding.dream_shops.dto.UserDto;
import com.meloCoding.dream_shops.models.User;
import com.meloCoding.dream_shops.request.CreateUserRequest;
import com.meloCoding.dream_shops.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User getAuthenticatedUser();
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);
    UserDto convertUserToDto(User user);
}
