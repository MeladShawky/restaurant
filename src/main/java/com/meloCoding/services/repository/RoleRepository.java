package com.meloCoding.services.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meloCoding.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
