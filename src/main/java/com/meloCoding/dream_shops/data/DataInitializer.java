package com.meloCoding.dream_shops.data;

import java.util.Set;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.meloCoding.dream_shops.models.Role;
import com.meloCoding.dream_shops.models.User;
import com.meloCoding.dream_shops.services.repository.RoleRepository;
import com.meloCoding.dream_shops.services.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        createDefaultRoles();
        createDefaultUsers();
        createDefaultAdmin();
    }

    private void createDefaultRoles() {
        Set<String> defaultRoles = Set.of("ROLE_USER", "ROLE_ADMIN");
        for (String roleName : defaultRoles) {
            roleRepository.findByName(roleName).orElseGet(() -> {
                Role role = new Role(roleName);
                return roleRepository.save(role);
            });
        }
        System.out.println("Default roles checked/created.");
    }

    private void createDefaultUsers() {
        if (userRepository.count() > 0) {
            return;
        }

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setFirstName("User");
            user.setLastName("" + i);
            user.setEmail("user" + i + "@example.com");
            user.setPassword(passwordEncoder.encode("123456"));
            user.getRoles().add(userRole);
            userRepository.save(user);
        }

        System.out.println("5 default users created successfully.");
    }

    private void createDefaultAdmin() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

        if (userRepository.findByEmail("admin@example.com").isPresent()) {
            return;
        }

        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.getRoles().add(adminRole);
        userRepository.save(admin);

        System.out.println("Default admin created successfully.");
    }
}
