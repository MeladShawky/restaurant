package com.meloCoding.dream_shops.models;

import java.sql.Blob;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class UserImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String fileType;
    @Lob
    @JsonIgnore
    private Blob image;
    private String downloadUrl;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
