package com.sea.desafioseacorporation.bootstrap;

import com.sea.desafioseacorporation.entity.Role;
import com.sea.desafioseacorporation.entity.UserEntity;
import com.sea.desafioseacorporation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String adminEmail = "admin@admin.com";

        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        UserEntity admin = new UserEntity();
        admin.setName("Administrador");
        admin.setEmail(adminEmail);
        admin.setPasswordHash(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);

        userRepository.save(admin);
    }
}