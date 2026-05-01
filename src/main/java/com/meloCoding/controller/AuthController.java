package com.meloCoding.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meloCoding.exceptions.AlreadyExistsException;
import com.meloCoding.models.User;
import com.meloCoding.request.CreateUserRequest;
import com.meloCoding.request.LoginRequest;
import com.meloCoding.response.ApiResponse;
import com.meloCoding.response.JwtResponse;
import com.meloCoding.security.jwt.JwtUtils;
import com.meloCoding.security.user.ShopUserDetails;
import com.meloCoding.services.Cart.ICartService;
import com.meloCoding.services.User.IUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final IUserService userService;
    private final ICartService cartService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateTokenForUser(authentication);

            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
            User user = userService.getUserById(userDetails.getId());
            
            // Create cart for user if not exists
            cartService.initializeCart(user);
            
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);

            return ResponseEntity.ok(new ApiResponse("Login Successful", jwtResponse));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse("Invalid email or password", null));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody CreateUserRequest request) {
        try {
            User user = userService.createUser(request);
            return ResponseEntity.ok(new ApiResponse("Registration Successful", user.getId()));
        } catch (AlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
