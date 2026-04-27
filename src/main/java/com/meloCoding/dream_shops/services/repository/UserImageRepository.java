package com.meloCoding.dream_shops.services.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.dream_shops.models.UserImage;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    Optional<UserImage> findByUserId(Long userId);
}
