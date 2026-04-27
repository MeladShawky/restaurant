package com.meloCoding.dream_shops.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.meloCoding.dream_shops.models.Image;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);
}
