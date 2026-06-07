package com.meloCoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.meloCoding.models.Image;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByProductId(Long id);
}
