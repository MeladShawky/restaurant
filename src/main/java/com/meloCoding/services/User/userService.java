package com.meloCoding.services.User;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.meloCoding.dto.UserDto;
import com.meloCoding.exceptions.AlreadyExistsException;
import com.meloCoding.exceptions.ResourceNotFoundException;
import com.meloCoding.models.Role;
import com.meloCoding.models.User;
import com.meloCoding.request.CreateUserRequest;
import com.meloCoding.request.UpdateUserRequest;
import com.meloCoding.security.user.ShopUserDetails;
import com.meloCoding.services.repository.RoleRepository;
import com.meloCoding.services.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class userService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setEmail(req.getEmail());
                    user.setPassword(passwordEncoder.encode(req.getPassword()));
                    user.setAddress(req.getAddress());
                    user.setPhone(req.getPhone());

                    // Assign default ROLE_USER
                    Role userRole = roleRepository.findByName("ROLE_USER")
                            .orElseThrow(() -> new RuntimeException("Default role not found!"));
                    user.getRoles().add(userRole);

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new AlreadyExistsException("Oops! " + request.getEmail() + " already exists!"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    existingUser.setAddress(request.getAddress());
                    existingUser.setPhone(request.getPhone());
                    return userRepository.save(existingUser);
                })
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, () -> {
                    throw new ResourceNotFoundException("User not found!");
                });
    }

    @Override
    public UserDto convertUserToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            throw new ResourceNotFoundException("User not found!");
        ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
        return userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
    }
}
